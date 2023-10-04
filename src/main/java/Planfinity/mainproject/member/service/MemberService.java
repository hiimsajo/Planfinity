package Planfinity.mainproject.member.service;

import Planfinity.mainproject.auth.SecurityUtil;
import Planfinity.mainproject.auth.utils.CustomAuthorityUtils;
import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.domain.Terminated;
import Planfinity.mainproject.member.dto.SignupDto;
import Planfinity.mainproject.member.repository.MemberRepository;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final AmazonS3Client amazonS3Client;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                         CustomAuthorityUtils authorityUtils, AmazonS3Client amazonS3Client) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.amazonS3Client = amazonS3Client;
    }

    public Member createMember(SignupDto.Request signupDto) {
        Member member = signupDto.toEntity();

        verifyExistsEmail(member.getEmail());

        // Password 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // DB에 User Role 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        member.setRegisteredAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    public Member findMember() {
        return verifyExistsId(findEmail());
    }

    public Long findEmail() {
        String jwtEmail = SecurityUtil.getCurrentMemberEmail();
        Optional<Member> memberOptional = memberRepository.findByEmail(jwtEmail);
        return memberOptional.map(Member::getMemberId).orElse(null);
    }

    private Member verifyExistsId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    // 이미 존재하는 이메일인지 검증
    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    // 닉네임 변경
    public boolean updateNickname(String newNickname) {
        Member member = verifyExistsId(findMember().getMemberId());

        // 회원이 탈퇴 상태인지 확인
        if (member.getTerminated() == Terminated.TRUE) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_TERMINATED);
        }

        // 현재 닉네임과 입력한 닉네임이 같은지 확인
        if (!newNickname.equals(member.getNickname())) {
            member.setNickname(newNickname);
            memberRepository.save(member);
            return true;
        } else {
            // 현재 닉네임과 새 닉네임이 같은 경우 예외 처리
            throw new BusinessLogicException(ExceptionCode.SAME_NICKNAME);
        }
    }

    // 비밀번호 변경
    public boolean updatePassword(String currentPassword, String newPassword) {
        Member member = verifyExistsId(findMember().getMemberId());

        // 회원이 탈퇴 상태인지 확인
        if (member.getTerminated() == Terminated.TRUE) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_TERMINATED);
        }

        // 현재 비밀번호와 입력한 비밀번호가 일치하는지 확인
        if (passwordEncoder.matches(currentPassword, member.getPassword())) {
            // 입력한 새 비밀번호와 현재 비밀번호가 다른 경우에만 비밀번호 변경
            if (!newPassword.equals(currentPassword)) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                member.setPassword(encodedNewPassword);
                memberRepository.save(member);
                return true;
            } else {
                throw new BusinessLogicException(ExceptionCode.SAME_CURRENT_PASSWORD);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.INVALID_PASSWORD_FORMAT);
        }
    }

    // 회원 탈퇴
    public void terminateMember() {

        Member member = findMember();

        // 이미 탈퇴한 회원이면 예외 처리
        if (member.getTerminated() == Terminated.TRUE) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_TERMINATED);
        }

        // 회원의 권한을 제한하고 탈퇴 시간 업데이트
        member.updateTerminate(Terminated.TRUE);
        member.setTerminatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 권한을 null로 설정 (이전 권한을 삭제)
        member.setRoles(null);

        memberRepository.save(member);
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public String uploadAndUpdateProfileImage(MultipartFile file, String dirName) throws IOException {
        Member member = findMember();
        // 기존 이미지 삭제
        String previousImageUrl = getPreviousImageUrlFromMember(member);
        if (previousImageUrl != null) {
            deleteImageFromS3(previousImageUrl);
        }
        // 새로운 이미지 업로드
        String imageUrl = upload(file, dirName);

        // 이미지 URL 업데이트
        updateProfileImageUrl(member, imageUrl);

        return imageUrl;
    }

    public String upload(MultipartFile file, String dirName) throws IOException {

        String storedFilePath = dirName + "/" + UUID.randomUUID() + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, storedFilePath, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, storedFilePath).toString();
    }
    private void deleteImageFromS3(String imageUrl) {
        String objectKey = getObjectKeyFromUrl(imageUrl);

        if (objectKey != null) {
            try {
                amazonS3Client.deleteObject(bucket, objectKey);
            } catch (AmazonS3Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to delete image from S3: " + e.getMessage());
            }
        } else {
            // 올바르지 않은 이미지 URL
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
    }
    private String getObjectKeyFromUrl(String imageUrl) {
        int bucketIndex = imageUrl.indexOf(bucket);
        if (bucketIndex != -1) {
            return imageUrl.substring(bucketIndex + bucket.length() + 1);
        }
        return null;
    }
    private void updateProfileImageUrl(Member member, String imageUrl) {
        member.setProfileImage(imageUrl);
        memberRepository.save(member);
    }

    private String getPreviousImageUrlFromMember(Member member) {
        return member.getProfileImage();
    }

}
