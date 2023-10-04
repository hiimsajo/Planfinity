package Planfinity.mainproject.todogroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UpdateTodoGroupDto {

    @Getter
    public static class Request {

        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;

        @JsonProperty(value = "todo_group_title")
        private String todoGroupTitle;

        public void setTodoGroupId(Long todoGroupId) {
            this.todoGroupId = todoGroupId;
        }

    }

}
