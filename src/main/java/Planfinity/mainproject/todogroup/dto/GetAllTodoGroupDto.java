package Planfinity.mainproject.todogroup.dto;

import Planfinity.mainproject.todogroup.domain.TodoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class GetAllTodoGroupDto {
    @Getter
    public static class Response {

        @JsonProperty(value = "member_id")
        private Long memberId;

        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;

        @JsonProperty(value = "todo_group_title")
        private String todoGroupTitle;

        public Response(TodoGroup todoGroup) {
            this.memberId = todoGroup.getMember().getMemberId();
            this.todoGroupId = todoGroup.getTodoGroupId();
            this.todoGroupTitle = todoGroup.getTodoGroupTitle();
        }

    }

}
