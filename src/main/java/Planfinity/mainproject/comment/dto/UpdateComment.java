package Planfinity.mainproject.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UpdateComment {

    @Getter
    public static class Request {

        @JsonProperty(value = "comment_content")
        private String commentContent;

    }
}