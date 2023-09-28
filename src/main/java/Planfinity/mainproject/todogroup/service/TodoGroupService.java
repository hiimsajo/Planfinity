package Planfinity.mainproject.todogroup.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.repository.MemberRepository;
import Planfinity.mainproject.member.service.MemberService;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import Planfinity.mainproject.todogroup.dto.CreateTodoGroupDto;
import Planfinity.mainproject.todogroup.dto.UpdateTodoGroupDto;
import Planfinity.mainproject.todogroup.repository.TodoGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TodoGroupService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TodoGroupRepository todoGroupRepository;

    public TodoGroupService(MemberService memberService, MemberRepository memberRepository,
        TodoGroupRepository todoGroupRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.todoGroupRepository = todoGroupRepository;
    }

    @Transactional
    public TodoGroup createTodoGroup(CreateTodoGroupDto.Request requestDto) {
        Member member = memberService.findMember();
        TodoGroup saveTodoGroup = todoGroupRepository.save(requestDto.toEntity(member));

        return saveTodoGroup;
    }

    @Transactional
    public TodoGroup updateTodoGroup(Long todoGroupId, UpdateTodoGroupDto.Request requestDto) {
        Member member = memberService.findMember();
        TodoGroup findTodoGroup = findVerifiedTodoGroup(todoGroupId);

        findTodoGroup.changeTitle(requestDto.getTodoGroupTitle());

        return findTodoGroup;
    }

    @Transactional(readOnly = true)
    public TodoGroup getTodoGroup(Long todoGroupId) {
        Member member = memberService.findMember();
        return findVerifiedTodoGroup(todoGroupId);
    }

    @Transactional(readOnly = true)
    public List<TodoGroup> getTodoGroups() {
        Member member = memberService.findMember();
        List<TodoGroup> todoGroups = this.todoGroupRepository.findAll();
        return todoGroups;
    }

    @Transactional
    public void deleteTodoGroup(Long todoGroupId) {
        Member member = memberService.findMember();
        TodoGroup findTodoGroup = findVerifiedTodoGroup(todoGroupId);
        todoGroupRepository.delete(findTodoGroup);
    }

    @Transactional(readOnly = true)
    public TodoGroup findVerifiedTodoGroup(Long todoGroupId) {
        TodoGroup findTodoGroup = todoGroupRepository.findById(todoGroupId).orElseThrow(() ->
            new BusinessLogicException(ExceptionCode.TODO_GROUP_NOT_FOUND));
        return findTodoGroup;

    }

}
