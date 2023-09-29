package Planfinity.mainproject.todogroup.dto;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class CreateTodoGroupDto {

    @Getter
    public static class Request {

        @JsonProperty(value = "todo_group_title")
        private String todoGroupTitle;

        public TodoGroup toEntity(Member member) {
            return new TodoGroup(member, todoGroupTitle);
        }
    }
}
