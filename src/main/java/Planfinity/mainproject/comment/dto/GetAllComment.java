package Planfinity.mainproject.comment.dto;

import Planfinity.mainproject.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class GetAllComment {

    @Getter
    public static class Response {

        @JsonProperty(value = "member_id")
        private Long memberId;
        @JsonProperty(value = "todo_group_id")
        private Long todoGroupId;
        @JsonProperty(value = "todo_id")
        private Long todoId;
        @JsonProperty(value = "comment_id")
        private Long commentId;
        @JsonProperty(value = "nickname")
        private String nickName;
        @JsonProperty(value = "profile_image")
        private String profileImage;
        @JsonProperty(value = "comment_content")
        private String commentContent;


        public Response(Comment comment) {
            this.memberId = comment.getMember().getMemberId();
            this.todoGroupId = comment.getTodo().getTodoGroup().getTodoGroupId();
            this.todoId = comment.getTodo().getTodoId();
            this.commentId = comment.getCommentId();
            this.nickName = comment.getMember().getNickname();
//            this.profileImage = String.join(File.separator, fileUploadPath, comment.getMember().getProfileImage());
            this.commentContent = comment.getCommentContent();
        }

    }

}
