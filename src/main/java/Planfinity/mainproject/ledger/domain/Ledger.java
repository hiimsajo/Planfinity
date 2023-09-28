package Planfinity.mainproject.ledger.domain;

import Planfinity.mainproject.category.domain.Category;
import Planfinity.mainproject.inoutcome.domain.InOutCome;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.payment.domain.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ledger_id")
    private Long ledgerId;

    @Column(name = "ledger_title", length = 100, nullable = false)
    private String ledgerTitle;

    @Column(name = "ledger_content", nullable = false)
    private String ledgerContent;

    @Column(name = "ledger_amount", nullable = false)
    private Long ledgerAmount;

    @Column(name = "ledger_schedule_date", nullable = true)
    private LocalDate ledgerScheduleDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "ledger_group_id")
    private LedgerGroup ledgerGroup;

    public void addLedgerGroup(LedgerGroup ledgerGroup) {
        this.ledgerGroup = ledgerGroup;
    }

    @ManyToOne
    @JoinColumn(name = "inoutcome_id")
    private InOutCome inOutCome;

    public void addInOutCome(InOutCome inOutCome) {
        this.inOutCome = inOutCome;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void addCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public void addPayment(Payment payment) {
        this.payment = payment;
    }
}
