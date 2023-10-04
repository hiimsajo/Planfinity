package Planfinity.mainproject.payment.domain;

import Planfinity.mainproject.ledger.domain.Ledger;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_name", length = 100)
    private String paymentName;

    @OneToMany(mappedBy = "payment")
    private List<Ledger> ledgers = new ArrayList<>();
}
