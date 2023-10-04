package Planfinity.mainproject.comment.service;

import Planfinity.mainproject.comment.domain.Comment;
import Planfinity.mainproject.comment.dto.CreateComment;
import Planfinity.mainproject.comment.dto.UpdateComment;
import Planfinity.mainproject.comment.repository.CommentRepository;
import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.service.MemberService;
import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todo.service.TodoService;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import Planfinity.mainproject.todogroup.service.TodoGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CommentService {

    private final MemberService memberService;
    private final TodoGroupService todoGroupService;
    private final TodoService todoService;
    private final CommentRepository commentRepository;

    public CommentService(MemberService memberService, TodoGroupService todoGroupService,
        TodoService todoService, CommentRepository commentRepository) {
        this.memberService = memberService;
        this.todoGroupService = todoGroupService;
        this.todoService = todoService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment createComment(Long todoGroupId, Long todoId, CreateComment.Request requestDto) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);
        Todo todo = todoService.findVerifiedTodo(todoId);
        Comment saveComment = commentRepository.save(requestDto.toEntity(member,todo));

        return saveComment;
    }

    @Transactional
    public Comment upDateComment(Long todoGroupId, Long todoId, Long commentId, UpdateComment.Request requestDto) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);
        todoService.findVerifiedTodo(todoId);
        Comment comment = findByVerifiedComment(commentId);
        comment.changeContent(requestDto.getCommentContent());

        return comment;
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(Long todoGroupId, Long todoId) {
        Member member = memberService.findMember();

        TodoGroup todoGroup = todoGroupService.findVerifiedTodoGroup(todoGroupId);
        Todo todo = todoService.findVerifiedTodo(todoId);

        List<Comment> comments = todo.getComments();

        return comments;
    }

    @Transactional
    public void deleteComment(Long todoGroupId, Long todoId, Long commentId) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);
        todoService.findVerifiedTodo(todoId);
        Comment findComment = findByVerifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    @Transactional(readOnly = true)
    public Comment findByVerifiedComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

}
