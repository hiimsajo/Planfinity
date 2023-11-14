package Planfinity.mainproject.todo.controller;

import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todo.dto.CreateTodoDto;
import Planfinity.mainproject.todo.dto.GetAllTodoDto.Response;
import Planfinity.mainproject.todo.dto.UpdateTodoDto;
import Planfinity.mainproject.todo.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todogroups/{todo-group-id}/todos")
    public ResponseEntity createTodo(@PathVariable("todo-group-id") @Positive Long todoGroupId,
                                    @Valid @RequestBody CreateTodoDto.Request requestDto){
        Todo todo = todoService.createTodo(todoGroupId, requestDto);

        return new ResponseEntity(new Response(todo), HttpStatus.CREATED);
    }

    @PutMapping("/todogroups/{todo-group-id}/todos/{todo-id}")
    public ResponseEntity patchTodo(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId,
        @Valid @RequestBody UpdateTodoDto.Request requestDto) {

        Todo todo = todoService.updateTodo(todoGroupId, todoId, requestDto);

        return new ResponseEntity(new Response(todo), HttpStatus.OK);
    }

    @PatchMapping("/todogroups/{todo-group-id}/todos/{todo-id}/status")
    public ResponseEntity updateStatusTodo(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId,
        @RequestBody UpdateTodoDto.updateStatus updateStatusDto) {

        Todo todo = todoService.updateStatusTodo(todoGroupId, todoId, updateStatusDto);

        return new ResponseEntity(new Response(todo), HttpStatus.OK);
    }

    @GetMapping("/todogroups/{todo-group-id}/todos/{todo-id}")
    public ResponseEntity getTodo(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId) {

        Todo todo = todoService.getTodo(todoGroupId, todoId);

        return new ResponseEntity(new Response(todo), HttpStatus.OK);
    }

    @GetMapping("/todogroups/{todo-group-id}/todos")
    public ResponseEntity<List<Response>> getTodos(@PathVariable("todo-group-id") @Positive Long todoGroupId) {

        List<Todo> todos = this.todoService.getTodos(todoGroupId);
        List<Response> responses = todos.stream()
            .map((todo -> new Response(todo)))
            .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/todogroups/{todo-group-id}/todos/dates")
    public ResponseEntity<List<Response>> dateGetTodos(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(value = "includeNoDate", defaultValue = "false") boolean includeNoDate) {

        List<Todo> todos = this.todoService.dateGetTodos(todoGroupId, startDate, endDate, includeNoDate);
        List<Response> responses = todos.stream()
            .map((todo -> new Response(todo)))
            .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/todogroups/{todo-group-id}/todos/{todo-id}")
    public ResponseEntity deleteTodo(@PathVariable("todo-group-id") @Positive Long todoGroupId,
        @PathVariable("todo-id") @Positive Long todoId) {

        todoService.deleteTodo(todoGroupId, todoId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
