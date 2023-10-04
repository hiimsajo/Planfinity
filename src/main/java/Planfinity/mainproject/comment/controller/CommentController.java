package Planfinity.mainproject.comment.controller;


import Planfinity.mainproject.comment.domain.Comment;
import Planfinity.mainproject.comment.dto.CreateComment;
import Planfinity.mainproject.comment.dto.GetAllComment.Response;
import Planfinity.mainproject.comment.dto.UpdateComment;
import Planfinity.mainproject.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class CommentController {

    @Value("${cloud.aws.s3.bucket}")
    private String fileUploadBucket;

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/todogroups/{todo-group-id}/todos/{todo-id}/comments")
    public ResponseEntity createComment(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId,
        @Valid @RequestBody CreateComment.Request requestDto) {
        Comment comment = commentService.createComment(todoGroupId, todoId, requestDto);

        return new ResponseEntity(new Response(comment), HttpStatus.CREATED);
    }

    @PatchMapping("/todogroups/{todo-group-id}/todos/{todo-id}/comments/{comment-id}")
    public ResponseEntity updateComment(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId,
        @PathVariable("comment-id") @Positive Long commentId,
        @Valid @RequestBody UpdateComment.Request requestDto) {
        Comment comment = commentService.upDateComment(todoGroupId, todoId, commentId, requestDto);

        return new ResponseEntity(new Response(comment), HttpStatus.OK);
    }

    @GetMapping("/todogroups/{todo-group-id}/todos/{todo-id}/comments")
    public ResponseEntity<List<Response>> getComments(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId) {
        List<Comment> comments = this.commentService.getComments(todoGroupId, todoId);
        List<Response> responses = comments.stream()
            .map((comment -> new Response(comment)))
            .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/todogroups/{todo-group-id}/todos/{todo-id}/comments/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId,
        @PathVariable("comment-id") @Positive Long commentId) {
        commentService.deleteComment(todoGroupId, todoId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
