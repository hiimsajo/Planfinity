package Planfinity.mainproject.ledger.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import Planfinity.mainproject.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Inoutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inoutcome_id")
    private Long inoutcomeId;

    @Column(name = "inoutcome_name", length = 100)
    private String inoutcomeName;

    @OneToMany(mappedBy = "inoutcome")
    private List<Ledger> ledgers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    public void addMember(Member member) {
        this.member = member;
    }

    public Inoutcome(Member member, String inoutcomeName) {
        this.member = member;
        this.inoutcomeName = inoutcomeName;
    }

    public void changeName(String name) {
        if (name != null) {
            this.inoutcomeName = name;
        }
    }

}
