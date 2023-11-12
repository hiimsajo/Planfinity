package Planfinity.mainproject.ledgergroup.invitationDto;


import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class InvitationMemberResponseDto {

    @JsonProperty(value = "ledger_group_id")
    private Long ledgerGroupId;
    @JsonProperty(value = "owner_id")
    private Long ownerId;
    @JsonProperty(value = "profile_image")
    private String profileImage;
    @JsonProperty(value = "invited_members")
    private List<ProfileDto> invitedMembers;

    public InvitationMemberResponseDto(LedgerGroup ledgerGroup) {
        this.ledgerGroupId = ledgerGroup.getLedgerGroupId();
        this.ownerId = ledgerGroup.getMember().getMemberId();
        this.profileImage = ledgerGroup.getMember().getProfileImage();

        List<Member> members = ledgerGroup.getLedgerGroupMembers().stream()
                .map((gm) -> gm.getMember())
                .collect(Collectors.toList());

        this.invitedMembers = members.stream()
                .map((m) -> new ProfileDto(m))
                .collect(Collectors.toList());
    }


    @Getter
    @NoArgsConstructor
    public static class ProfileDto {
        @JsonProperty(value = "member_id")
        private Long memberId;
        @JsonProperty(value = "profile_image")
        private String profileImage;

        public ProfileDto(Member member) {
            this.memberId = member.getMemberId();
            this.profileImage = member.getProfileImage();
        }
    }

}