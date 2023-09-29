package Planfinity.mainproject.comment.domain;

import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.todo.domain.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment_content")
    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public void addTodo(Todo todo) {
        this.todo = todo;
    }

    public Comment(Member member, Todo todo, String commentContent) {
        this.member = member;
        this.todo = todo;
        this.commentContent = commentContent;
    }
    public void changeContent(String commentContent) {
        if(commentContent != null) {
            this.commentContent = commentContent;
        }
    }

}
