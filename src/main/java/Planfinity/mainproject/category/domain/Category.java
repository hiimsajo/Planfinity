package Planfinity.mainproject.category.domain;

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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 100)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Ledger> ledgers = new ArrayList<>();
}