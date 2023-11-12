package Planfinity.mainproject.ledgergroup.repository;

import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerGroupRepository extends JpaRepository<LedgerGroup, Long> {

}
