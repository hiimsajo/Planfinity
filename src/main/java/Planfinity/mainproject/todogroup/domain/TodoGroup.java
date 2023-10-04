package Planfinity.mainproject.todogroup.domain;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todo.domain.Todo;
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

}
