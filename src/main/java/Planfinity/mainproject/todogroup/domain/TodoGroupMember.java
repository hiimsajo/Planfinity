package Planfinity.mainproject.todogroup.domain;

import Planfinity.mainproject.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class TodoGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "todo_group_member_id")
    private Long todoGroupMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "todo_group_id")
    private TodoGroup todoGroup;

    public void addTodoGroup(TodoGroup todoGroup) {
        this.todoGroup = todoGroup;
    }

    public TodoGroupMember(TodoGroup todoGroup, Member member) {
        this.todoGroup = todoGroup;
        this.member = member;
    }
}
