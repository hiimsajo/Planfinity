package Planfinity.mainproject.todogroup.dto;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InvitationTodoGroupDto {
    @Getter
    public static class Post {

        @NotEmpty
        @JsonProperty(value = "emails")
        private List<EmailDto> emails;

        // EmailDto로부터 email 추출
        public List<String> extractEmails() {
            return emails.stream()
                .map((e) -> e.getEmail())
                .collect(Collectors.toList());
        }


    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        @JsonProperty(value = "member_id")
        private Long memberId;
        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;
        @NotEmpty
        @JsonProperty(value = "emails")
        private List<EmailDto> emails;

        public Response(TodoGroup todoGroup) {
            this.memberId = todoGroup.getMember().getMemberId();
            this.todoGroupId = todoGroup.getTodoGroupId();

            List<Member> members = todoGroup.getTodoGroupMembers().stream()
                .map((gm) -> gm.getMember())
                .collect(Collectors.toList());

            this.emails = members.stream()
                .map((m) -> m.getEmail())
                .map((e) -> new EmailDto(e))
                .collect(Collectors.toList());
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailDto {
        @NotBlank
        private String email;
    }


}
