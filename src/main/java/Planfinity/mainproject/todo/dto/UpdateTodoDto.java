package Planfinity.mainproject.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UpdateTodoDto {

    @Getter
    public static class Request {

        @JsonProperty(value = "todo_id")
        private Long todoId;

        @JsonProperty(value = "todo_title")
        private String todoTitle;

        @JsonProperty(value = "todo_content")
        private String todoContent;

        @JsonProperty(value = "todo_schedule_date")
        private String todoScheduleDate;

        public void setTodoId(Long todoId) {
            this.todoId = todoId;
        }

    }

    @Getter
    public static class updateStatus {
        private String status;
    }

}
