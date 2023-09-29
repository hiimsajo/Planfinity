package Planfinity.mainproject.todo.domain;

import Planfinity.mainproject.comment.domain.Comment;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @Column(name = "todo_title", length = 100, nullable = false)
    private String todoTitle;

    @Column(name = "todo_content", nullable = false)
    private String todoContent;

    @Nullable
    @Column(name = "todo_schedule_date")
    private LocalDate todoScheduleDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "todo_status", nullable = false)
    private TodoStatus todoStatus = TodoStatus.INCOMPLETE;

    @OneToMany(mappedBy = "todo")
    private List<Comment> comments = new ArrayList<>();

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

    public Todo(Member member, TodoGroup todoGroup, String todoTitle, String todoContent, LocalDate todoScheduleDate) {
        this.member = member;
        this.todoGroup = todoGroup;
        this.todoTitle = todoTitle;
        this.todoContent = todoContent;
        this.todoScheduleDate = todoScheduleDate;
    }

    public void changeTitle(final String title) {
        if(title != null) {
            this.todoTitle = title;
        }
    }
    public void changeContent(final String content) {
        if(content != null) {
            this.todoContent = content;
        }
    }

    public void changeScheduleDate(final LocalDate scheduledate) {
        this.todoScheduleDate = Optional.ofNullable(scheduledate).orElse(null);
    }

    public void updateStatus(TodoStatus todoStatus) {
        this.todoStatus = todoStatus;
    }




}
