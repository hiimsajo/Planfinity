package Planfinity.mainproject.ledger.domain;

import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

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
    private Inoutcome inoutcome;

    public void addInoutcome(Inoutcome inoutcome) {
        this.inoutcome = inoutcome;
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

    public Ledger(Member member, LedgerGroup ledgerGroup, String ledgerTitle, String ledgerContent, Long ledgerAmount,
                  LocalDate ledgerScheduleDate, Category category, Inoutcome inoutcome, Payment payment) {
        this.member = member;
        this.ledgerGroup = ledgerGroup;
        this.ledgerTitle = ledgerTitle;
        this.ledgerContent = ledgerContent;
        this.ledgerAmount = ledgerAmount;
        this.ledgerScheduleDate = ledgerScheduleDate;
        this.category = category;
        this.inoutcome = inoutcome;
        this.payment = payment;
    }

    public void setTitle(final String title){
        if(title != null) {
            this.ledgerTitle = title;
        }
    }

    public void setContent(final String content){
        if(content != null) {
            this.ledgerContent = content;
        }
    }

    public void setAmount(final Long amount){
        if(amount != null) {
            this.ledgerAmount = amount;
        }
    }

    public void setDate(final LocalDate date) {
        this.ledgerScheduleDate = Optional.ofNullable(date).orElse(null);
    }

    public void setLedgerGroup(final LedgerGroup ledgerGroup) {
        if(ledgerGroup != null) {
            this.ledgerGroup = ledgerGroup;
        }
    }

    public void setCategory(Category category) {
        // if(category != null) {
        this.category = Optional.ofNullable(category).orElse(null);
    }

    public void setInoutcome(Inoutcome inoutcome) {
        if(inoutcome != null) {
            this.inoutcome = inoutcome;
        }
    }

    public void setPayment(Payment payment) {
        //if(payment != null) {
        this.payment = Optional.ofNullable(payment).orElse(null);
    }
}
