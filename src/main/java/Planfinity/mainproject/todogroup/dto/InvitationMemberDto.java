package Planfinity.mainproject.todogroup.dto;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InvitationMemberDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;
        @JsonProperty(value = "owner_id")
        private Long ownerId;
        @JsonProperty(value = "profile_image")
        private String profileImage;
        @JsonProperty(value = "invite_members")
        private List<ProfileDto> inviteMembers;

        public Response(TodoGroup todoGroup) {
            this.todoGroupId = todoGroup.getTodoGroupId();
            this.ownerId = todoGroup.getMember().getMemberId();
            this.profileImage = todoGroup.getMember().getProfileImage();

            List<Member> members = todoGroup.getTodoGroupMembers().stream()
                .map((gm) -> gm.getMember())
                .collect(Collectors.toList());

            this.inviteMembers = members.stream()
                .map((m) -> new ProfileDto(m))
                .collect(Collectors.toList());
        }
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
