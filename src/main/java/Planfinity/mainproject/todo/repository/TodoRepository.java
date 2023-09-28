package Planfinity.mainproject.todo.repository;

import Planfinity.mainproject.todo.domain.Todo;
import Planfinity.mainproject.todogroup.domain.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByTodoGroupAndTodoScheduleDateBetween(TodoGroup todoGroup, LocalDate startDate, LocalDate endDate);

    List<Todo> findByTodoGroupAndTodoScheduleDateIsNull(TodoGroup todoGroup);

}
