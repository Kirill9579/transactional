package ru.test.transactional.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.test.transactional.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
