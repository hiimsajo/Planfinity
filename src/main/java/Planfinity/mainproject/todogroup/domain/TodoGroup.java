package Planfinity.mainproject.todogroup.domain;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todo.domain.Todo;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class TodoGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_group_id")
    private Long todoGroupId;

    @Column(name = "todo_group_title", length = 100)
    private String todoGroupTitle;

    @OneToMany(mappedBy = "todoGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoGroupMember> todoGroupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "todoGroup")
    private List<Todo> todos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }

    public TodoGroup(Member member, String todoGroupTitle) {
        this.member = member;
        this.todoGroupTitle = todoGroupTitle;
    }
    public void changeTitle(final String title) {
        if(title != null) {
            this.todoGroupTitle = title;
        }
    }

    // 전달해준 Member가 해당 TodoGroup의 생성자인지 확인하는 메서드
    public boolean isOwner(Member member) {
        return this.member.getMemberId() == member.getMemberId();
    }

    // 전달받은 Member들을 해당 TodoGroup에 초대하는 메서드
    public void invites(List<Member> inviteMembers) {
        this.todoGroupMembers.addAll(
            inviteMembers.stream()
                .map((m) -> new TodoGroupMember(this, m))
                .collect(Collectors.toList()));
    }

}
