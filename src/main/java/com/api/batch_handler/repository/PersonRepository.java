package com.api.batch_handler.repository;

import com.api.batch_handler.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
