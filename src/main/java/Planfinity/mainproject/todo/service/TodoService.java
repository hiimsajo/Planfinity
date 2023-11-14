package Planfinity.mainproject.todo.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.service.MemberService;
import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todo.domain.TodoStatus;
import Planfinity.mainproject.todo.dto.CreateTodoDto;
import Planfinity.mainproject.todo.dto.UpdateTodoDto;
import Planfinity.mainproject.todo.repository.TodoRepository;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import Planfinity.mainproject.todogroup.service.TodoGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberService memberService;
    private final TodoGroupService todoGroupService;

    public TodoService(TodoRepository todoRepository, MemberService memberService,
        TodoGroupService todoGroupService) {
        this.todoRepository = todoRepository;
        this.memberService = memberService;
        this.todoGroupService = todoGroupService;
    }

    @Transactional
    public Todo createTodo(Long todoGroupId, CreateTodoDto.Request requestDto) {
        Member member = memberService.findMember();

        TodoGroup todoGroup = todoGroupService.findVerifiedTodoGroup(todoGroupId);
        Todo saveTodo = todoRepository.save(requestDto.toEntity(member, todoGroup));

        return saveTodo;
    }

    @Transactional
    public Todo updateTodo(Long todoGroupId, Long todoId, UpdateTodoDto.Request requestDto) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);

        Todo findTodo = findVerifiedTodo(todoId);

        findTodo.changeTitle(requestDto.getTodoTitle());
        findTodo.changeContent(requestDto.getTodoContent());

        LocalDate scheduleDate = null;
        if (requestDto.getTodoScheduleDate() != null) {
            scheduleDate = LocalDate.parse(requestDto.getTodoScheduleDate());
        }

        findTodo.changeScheduleDate(scheduleDate);

        return findTodo;
    }

    @Transactional
    public Todo updateStatusTodo(Long todoGroupId, Long todoId, UpdateTodoDto.updateStatus updateStatusDto) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);

        Todo findTodo = findVerifiedTodo(todoId);

        TodoStatus todoStatus = TodoStatus.valueOf(updateStatusDto.getStatus().toUpperCase());
        findTodo.updateStatus(todoStatus);

        return findTodo;
    }

    @Transactional(readOnly = true)
    public Todo getTodo(Long todoGroupId, Long todoId) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);

        return findVerifiedTodo(todoId);
    }

    @Transactional(readOnly = true)
    public List<Todo> getTodos(Long todoGroupId) {
        Member member = memberService.findMember();

        TodoGroup todoGroup = todoGroupService.findVerifiedTodoGroup(todoGroupId);

        List<Todo> todos = todoGroup.getTodos();

        return todos;
    }

    @Transactional(readOnly = true)
    public List<Todo> dateGetTodos(Long todoGroupId, LocalDate startDate, LocalDate endDate, boolean includeNoDate) {
        Member member = memberService.findMember();

        TodoGroup todoGroup = todoGroupService.findVerifiedTodoGroup(todoGroupId);

        List<Todo> todos;

        todos = this.todoRepository.findByTodoGroupAndTodoScheduleDateBetween(todoGroup, startDate, endDate);

        if(includeNoDate) {
            todos.addAll(this.todoRepository.findByTodoGroupAndTodoScheduleDateIsNull(todoGroup));
        }

        // NoDate는 NoDate끼리
        return todos.stream()
            .sorted(Comparator.comparing(Todo::getTodoScheduleDate, Comparator.nullsFirst(Comparator.naturalOrder())))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTodo(Long todoGroupId, Long todoId) {
        Member member = memberService.findMember();

        todoGroupService.findVerifiedTodoGroup(todoGroupId);

        Todo findTodo = findVerifiedTodo(todoId);
        todoRepository.delete(findTodo);
    }

    @Transactional(readOnly = true)
    public Todo findVerifiedTodo(Long todoId) {
        Todo findTodo = todoRepository.findById(todoId).orElseThrow(() ->
            new BusinessLogicException(ExceptionCode.TODO_NOT_FOUND));
        return findTodo;
    }
}
