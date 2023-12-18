package com.tareas.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareas.api.dto.CreateTaskDTO;
import com.tareas.api.dto.UpdateTaskDTO;
import com.tareas.api.entity.Person;
import com.tareas.api.entity.Task;
import com.tareas.api.repository.PersonRepository;
import com.tareas.api.repository.TaskRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

	@Autowired
	@Qualifier("taskRepository")
	private TaskRepository taskRepository;

	@Autowired
	@Qualifier("personRepository")
	private PersonRepository personRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Task>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAll());
	}

	@PostMapping("/create")
	public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskDTO taskDto) {

		Person person = personRepository.findById(taskDto.getPersonId()).orElse(null);

		// If the person exists
		if (person != null) {

			// Create task
			Task task = Task.builder().taskTitle(taskDto.getTaskTitle()).taskDescription(taskDto.getTaskDescription())
					.active(true).build();

			// Save task
			Task taskResult = taskRepository.save(task);

			// Link task to person and save him
			person.getTasks().add(task);
			personRepository.save(person);

			return ResponseEntity.status(HttpStatus.CREATED).body(taskResult);

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

	@DeleteMapping("/delete/{taskId}")
	public ResponseEntity<?> delete(@PathVariable Long taskId) {

		Task task = taskRepository.findById(taskId).orElse(null);

		if (task != null) {

			taskRepository.delete(task);
			return ResponseEntity.status(HttpStatus.OK).build();

		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PutMapping("/update/{taskId}")
	public ResponseEntity<Task> update(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskDTO taskDto) {

		Task task = taskRepository.findById(taskId).orElse(null);

		if (task != null) {

			task.setTaskTitle(taskDto.getTaskTitle());
			task.setTaskDescription(taskDto.getTaskDescription());
			task.setTime(LocalDateTime.now());

			Task taskResult = taskRepository.save(task);

			return ResponseEntity.status(HttpStatus.CREATED).body(taskResult);

		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/complete/{taskId}")
	public ResponseEntity<Task> complete(@PathVariable Long taskId) {

		Task task = taskRepository.findById(taskId).orElse(null);

		if (task != null && task.getActive()) {

			// Completes the task, set active to false
			// This is useful to Front End, they will decide what shows
			task.setActive(false);
			return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task));

		}
		return ResponseEntity.badRequest().build();
	}
}
