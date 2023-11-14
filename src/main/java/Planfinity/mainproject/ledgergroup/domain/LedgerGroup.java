package Planfinity.mainproject.ledgergroup.domain;

import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "ledgerGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LedgerGroupMember> ledgerGroupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "ledgerGroup")
    private List<Ledger> ledgers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }

    public LedgerGroup(Member member, String ledgerGroupTitle) {
        this.member = member;
        this.ledgerGroupTitle = ledgerGroupTitle;
    }

    public void setTitle(final String title) {
        if(title != null) {
            this.ledgerGroupTitle = title;
        }
    }


    //전달해준 멤버가 해당 그룹의 생성자인지 확인하는 매서드
    public boolean isOwner(Member member) {
        return this.member.getMemberId() == member.getMemberId();
    }

    //전달받은 멤버들을 해당 그룹에 초대하는 매서드
    public void inviteMembers(List<Member> membersToInvite) {
        this.ledgerGroupMembers.addAll(
                membersToInvite.stream()
                        .map((m) -> new LedgerGroupMember(this, m))
                        .collect(Collectors.toList()));

    }

}


