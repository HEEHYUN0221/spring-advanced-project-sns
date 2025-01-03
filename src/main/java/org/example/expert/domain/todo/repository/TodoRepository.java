package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

//  @Query(value = "SELECT t FROM Todo t JOIN FETCH t.user u ORDER BY t.modifiedAt DESC"
//      ,countQuery = "SELECT COUNT(t) FROM Todo t")
//  Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

  @Query("SELECT new org.example.expert.domain.todo.dto.response.TodoResponse("
      + "t.id,t.title,t.contents,t.weather,"
      + "new org.example.expert.domain.user.dto.response.UserResponse(u.id, u.email)"
      + ", t.createdAt, t.modifiedAt) "
      + "FROM Todo t JOIN t.user u ORDER BY t.modifiedAt desc")
  Page<TodoResponse> findAllByOrderByModifiedAtDesc(Pageable pageable);

  int countById(Long todoId);
}
