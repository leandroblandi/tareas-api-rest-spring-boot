package com.tareas.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareas.api.dto.CreatePersonDTO;
import com.tareas.api.entity.Person;
import com.tareas.api.repository.PersonRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/persons")
public class PersonController {

	@Autowired
	@Qualifier("personRepository")
	private PersonRepository personRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Person>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(personRepository.findAll());
	}

	@PostMapping("/create")
	public ResponseEntity<Person> create(@Valid @RequestBody CreatePersonDTO personDTO) {
		Person person = Person.builder().fullname(personDTO.getFullname()).email(personDTO.getEmail()).active(true)
				.tasks(Collections.emptyList()).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(personRepository.save(person));
	}

	@DeleteMapping("/delete/{personId}")
	public ResponseEntity<Person> delete(@PathVariable Long personId) {

		Person person = personRepository.findById(personId).orElse(null);

		if (person != null) {

			person.setActive(false);
			Person personResult = personRepository.save(person);
			return ResponseEntity.status(HttpStatus.OK).body(personResult);

		}
		return ResponseEntity.badRequest().build();
	}

}
