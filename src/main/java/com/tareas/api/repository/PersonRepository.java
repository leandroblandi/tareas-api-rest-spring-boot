package com.tareas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tareas.api.entity.Person;

@Repository("personRepository")
public interface PersonRepository extends JpaRepository<Person, Long> {

}
