package com.tareas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tareas.api.entity.Task;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository<Task, Long> {

}
