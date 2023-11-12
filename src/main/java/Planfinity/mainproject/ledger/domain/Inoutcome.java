package Planfinity.mainproject.inoutcome.domain;

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
public class InOutCome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inoutcome_id")
    private Long inOutComeId;

    @Column(name = "inoutcome_name", length = 100)
    private String inOutComeName;

    @OneToMany(mappedBy = "inOutCome")
    private List<Ledger> ledgers = new ArrayList<>();

}
