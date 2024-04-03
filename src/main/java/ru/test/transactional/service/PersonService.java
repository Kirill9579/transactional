package ru.test.transactional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.transactional.entity.Person;
import ru.test.transactional.repo.PersonRepository;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repo;
    private final StreamBridge bridge;

    @Transactional("chainedKafkaTransactionManager")
    public void save(Person person) {
        repo.save(person);
        bridge.send("topic", person.getName());
        throw new RuntimeException();
    }

}
