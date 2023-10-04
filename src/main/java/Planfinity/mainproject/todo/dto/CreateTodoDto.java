package Planfinity.mainproject.todo.dto;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateTodoDto {

    @Getter
    public static class Request {

        @JsonProperty(value = "todo_title")
        private String todoTitle;

        @JsonProperty(value = "todo_content")
        private String todoContent;

        @JsonProperty(value = "todo_schedule_date")
        private String todoScheduleDate;

        public Todo toEntity(Member member, TodoGroup todoGroup) {
            LocalDate date = null;
            if (todoScheduleDate != null) {
                date = LocalDate.parse(todoScheduleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            return new Todo(member, todoGroup ,todoTitle, todoContent, date);
        }

    }

}
