package Planfinity.mainproject.ledger.repository;

import Planfinity.mainproject.ledger.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
