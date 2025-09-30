package com.lucasangeloSpring.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lucasangeloSpring.models.Task;
import com.lucasangeloSpring.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TaskProjection> findByUser_Id(Long userId); // Função do Spring mesmo - Melhor opção
    // Optional<Task> findById(Long id); O Optional é uma classe do Java usada para representar um valor que pode estar presente ou ausente (null) de forma segura. Ele ajuda a evitar NullPointerException e torna o código mais legível ao indicar explicitamente que o valor pode ser opcional.
    
    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :user_id") // Outra forma como se fosse um SQL
    // List<Task> findByUser_Id(@Param("user_id") Long userId);

    // @Query(value = "SELECT * FROM task t WHERE t.user_id = userId", nativeQuery = true) // Via SQL nativo
    // List<Task> findByUser_Id(@Param("user_id") Long userId); 

}
