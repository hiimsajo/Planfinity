package Planfinity.mainproject.todogroup.repository;

import Planfinity.mainproject.todogroup.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup, Long> {

}
