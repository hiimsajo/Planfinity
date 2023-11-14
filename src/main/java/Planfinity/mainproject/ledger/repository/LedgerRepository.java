package Planfinity.mainproject.ledger.repository;

import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long>{
    List<Ledger> findByLedgerGroupAndLedgerScheduleDateBetween(LedgerGroup ledgerGroup, LocalDate startDate, LocalDate endDate);
    List<Ledger> findByLedgerGroup(LedgerGroup ledgerGroup);
}
