package Planfinity.mainproject.member.controller;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.dto.MemberResponseDto;
import Planfinity.mainproject.member.dto.SignupDto;
import Planfinity.mainproject.member.dto.UpdateNicknameDto;
import Planfinity.mainproject.member.dto.UpdatePasswordDto;
import Planfinity.mainproject.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 일반 회원가입
    @PostMapping("/members")
    public ResponseEntity createMember(@Valid @RequestBody SignupDto.Request requestDto) {
        Member member = memberService.createMember(requestDto);

        return new ResponseEntity(new MemberResponseDto.Response(member), HttpStatus.CREATED);
    }

    // 회원 정보 조회
    @GetMapping("/member")
    public ResponseEntity findMember() {

        Member member = memberService.findMember();

        return new ResponseEntity(new MemberResponseDto.Response(member), HttpStatus.OK);
    }

    // 닉네임 변경
    @PatchMapping("/members/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody @Valid UpdateNicknameDto updateNicknameDto) {
        try {
            boolean nicknameChanged = memberService.updateNickname(updateNicknameDto.getNickname());

            if (nicknameChanged) {
                return ResponseEntity.ok("Nickname changed successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to change nickname");
            }
        } catch (BusinessLogicException e) {
            if (e.getExceptionCode() == ExceptionCode.SAME_NICKNAME) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Current nickname and new nickname are the same");
            } else {
                return ResponseEntity.badRequest().body(e.getExceptionCode().getMessage());
            }
        }
    }

    // 비밀번호 변경
    @PatchMapping("/members/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        try {
            boolean passwordChanged = memberService.updatePassword(
                    updatePasswordDto.getPassword(),
                    updatePasswordDto.getNewPassword());

            if (passwordChanged) {
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to change password");
            }
        } catch (BusinessLogicException e) {
            if (e.getExceptionCode() == ExceptionCode.SAME_CURRENT_PASSWORD) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Current password and new password are the same");
            } else {
                return ResponseEntity.badRequest().body(e.getExceptionCode().getMessage());
            }
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/terminate")
    public ResponseEntity<String> terminateMember() {
        try {
            memberService.terminateMember();
            return ResponseEntity.ok("Member terminated successfully");
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getExceptionCode().getMessage());
        }
    }
}
