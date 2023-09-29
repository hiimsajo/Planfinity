package Planfinity.mainproject.todo.dto;

import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todo.domain.TodoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class GetAllTodoDto {

    @Getter
    public static class Response {

        @JsonProperty(value = "member_id")
        private Long memberId;
        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;
        @JsonProperty(value = "todo_id")
        private Long todoId;
        @JsonProperty(value = "todo_title")
        private String todoTitle;
        @JsonProperty(value = "todo_content")
        private String todoContent;
        @JsonProperty(value = "todo_schedule_date")
        private String todoScheduleDate;
        @JsonProperty(value = "todo_status")
        private TodoStatus todoStatus;

        public Response(Todo todo) {
            this.memberId = todo.getMember().getMemberId();
            this.todoGroupId = todo.getTodoGroup().getTodoGroupId();
            this.todoId = todo.getTodoId();
            this.todoTitle = todo.getTodoTitle();
            this.todoContent = todo.getTodoContent();
            this.todoScheduleDate = String.valueOf(todo.getTodoScheduleDate());
            this.todoStatus = todo.getTodoStatus();
        }

    }
}
