package Planfinity.mainproject.member.domain;

import Planfinity.mainproject.comment.domain.Comment;
import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroupMember;
import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import Planfinity.mainproject.todogroup.domain.TodoGroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImage;

    private String registeredAt;

    private String terminatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Terminated terminated = Terminated.FALSE;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TodoGroup> todoGroups = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TodoGroupMember> todoGroupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Ledger> ledgers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LedgerGroup> ledgerGroups = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LedgerGroupMember> ledgerGroupMembers = new ArrayList<>();

    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateTerminate(Terminated terminated) {
        this.terminated = terminated;
    }

}
