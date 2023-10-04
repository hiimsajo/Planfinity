package Planfinity.mainproject.todogroup.controller;


import Planfinity.mainproject.todogroup.domain.TodoGroup;
import Planfinity.mainproject.todogroup.dto.CreateTodoGroupDto;
import Planfinity.mainproject.todogroup.dto.GetAllTodoGroupDto.Response;
import Planfinity.mainproject.todogroup.dto.InvitationMemberDto;
import Planfinity.mainproject.todogroup.dto.InvitationTodoGroupDto;
import Planfinity.mainproject.todogroup.dto.UpdateTodoGroupDto;
import Planfinity.mainproject.todogroup.service.TodoGroupService;
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
public class TodoGroupController {

    private final TodoGroupService todoGroupService;

    public TodoGroupController(TodoGroupService todoGroupService) {
        this.todoGroupService = todoGroupService;
    }

    @PostMapping("/todogroups")
    public ResponseEntity createTodoGroup(@Valid @RequestBody CreateTodoGroupDto.Request requestDto) {
        TodoGroup todoGroup = todoGroupService.createTodoGroup(requestDto);

        return new ResponseEntity<>(new Response(todoGroup), HttpStatus.CREATED);
    }

    @PutMapping("/todogroups/{todo-group-id}")
    public ResponseEntity updateTodoGroup(@PathVariable("todo-group-id") @Positive Long todoGroupId,
                                    @Valid @RequestBody UpdateTodoGroupDto.Request requestDto) {

        TodoGroup todoGroup = todoGroupService.updateTodoGroup(todoGroupId, requestDto);

        return new ResponseEntity(new Response(todoGroup), HttpStatus.OK);
    }

    @GetMapping("/todogroups/{todo-group-id}")
    public ResponseEntity getTodoGroup(@PathVariable("todo-group-id") @Positive Long todoGroupId) {
        TodoGroup todoGroup = todoGroupService.getTodoGroup(todoGroupId);
        return new ResponseEntity(new Response(todoGroup), HttpStatus.OK);
    }

    @GetMapping("/todogroups")
    public ResponseEntity<List<Response>> getTodoGroups() {
        List<TodoGroup> todoGroups = this.todoGroupService.getTodoGroups();
        List<Response> responses = todoGroups.stream()
            .map((todoGroup -> new Response(todoGroup)))
            .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/todogroups/{todo-group-id}")
    public ResponseEntity deleteTodoGroup(@PathVariable("todo-group-id") @Positive Long todoGroupId) {
        todoGroupService.deleteTodoGroup(todoGroupId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/todogroups/{todo-group-id}/invitation")
    public ResponseEntity invite(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @Valid @RequestBody InvitationTodoGroupDto.Post invitationTodoGroupDto) {
        TodoGroup todoGroup = todoGroupService.invite(todoGroupId, invitationTodoGroupDto);
        return new ResponseEntity<>(new InvitationTodoGroupDto.Response(todoGroup), HttpStatus.CREATED);
    }

    @GetMapping("/todogroups/{todo-group-id}/members")
    public ResponseEntity inviteMember(@PathVariable("todo-group-id") @Positive Long todoGroupId) {
        TodoGroup todoGroup = todoGroupService.getInviteMember(todoGroupId);
        return new ResponseEntity(new InvitationMemberDto.Response(todoGroup), HttpStatus.OK);
    }

}
