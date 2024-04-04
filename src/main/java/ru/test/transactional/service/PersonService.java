package ru.test.transactional.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.transactional.entity.Person;
import ru.test.transactional.repo.PersonRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository repo;
    private final StreamBridge bridge;
    private final Runnable run;

    @Transactional("transactionManager")
    public void save(String person) throws InterruptedException {
        log.info("====== Save person =======");
        repo.save(new Person(person));
        log.info("====== send person ========");
        bridge.send("topic", person);
        log.info("====== exception ======= ");
        throw new RuntimeException();
    }

}
