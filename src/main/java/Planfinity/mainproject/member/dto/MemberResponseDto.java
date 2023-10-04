package Planfinity.mainproject.member.dto;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.domain.Terminated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class MemberResponseDto {

    @Getter
    public static class Response {
        @JsonProperty(value = "member_id")
        private Long memberId;
        @JsonProperty(value = "email")
        private String email;
        @JsonProperty(value = "password")
        private String password;
        @JsonProperty(value = "nickname")
        private String nickname;
        @JsonProperty(value = "profileImage")
        private String profileImage;
        @JsonProperty(value = "registered_at")
        private String registeredAt;
        @JsonProperty(value = "terminated_at")
        private String terminatedAt;
        @JsonProperty(value = "terminated")
        private Terminated terminated;

        public Response(Member member) {
            this.memberId = member.getMemberId();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.nickname = member.getNickname();
            this.profileImage = member.getProfileImage();
            this.registeredAt = member.getRegisteredAt();
            this.terminatedAt = member.getTerminatedAt();
            this.terminated = member.getTerminated();

        }
    }

}
