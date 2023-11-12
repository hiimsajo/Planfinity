package Planfinity.mainproject.ledger.repository;

import Planfinity.mainproject.ledger.domain.Inoutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InoutcomeRepository extends JpaRepository<Inoutcome, Long> {
}
