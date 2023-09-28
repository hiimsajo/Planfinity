package Planfinity.mainproject.ledgergroup.domain;

import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.member.domain.Member;
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
public class LedgerGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ledger_group_id")
    private Long ledgerGroupId;

    @Column(name = "ledger_group_title", length = 100)
    private String ledgerGroupTitle;

    @OneToMany(mappedBy = "ledgerGroup")
    private List<Ledger> ledgers = new ArrayList<>();

    @OneToMany(mappedBy = "ledgerGroup")
    private List<LedgerGroupMember> ledgerGroupMembers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }
}
