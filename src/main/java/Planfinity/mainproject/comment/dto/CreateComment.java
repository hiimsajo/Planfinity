package Planfinity.mainproject.comment.dto;

import Planfinity.mainproject.comment.domain.Comment;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class CreateComment {

    @Getter
    public static class Request {
        @JsonProperty(value = "comment_content")
        private String commentContent;

        public Comment toEntity(Member member, Todo todo) {
            return new Comment(member, todo, this.commentContent);
        }


    }
}