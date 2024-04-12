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

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository repo;
    private final StreamBridge bridge;


    @Transactional
    public void save(String person) throws InterruptedException {
        log.info("====== send person ========");
        bridge.send("test-exception", person);
        bridge.send("test-exception", person);
        bridge.send("test-exception", person);
        bridge.send("test-exception", person);
        log.info("====== end ========");
        Thread.sleep(2000);
//        log.info("====== Save person =======");
//        repo.save(new Person(person));
        throw  new RuntimeException();
    }
    @KafkaListener(topics = "test-exception", groupId = "a")
    public void get(String s) {
        System.out.println(s);
    }


}
