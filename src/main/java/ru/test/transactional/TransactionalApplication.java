package ru.test.transactional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.test.transactional.service.PersonService;

import java.util.UUID;

@SpringBootApplication
public class TransactionalApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(TransactionalApplication.class, args);
        PersonService service = context.getBean(PersonService.class);
        service.save("Name 1 " + UUID.randomUUID());
    }

}
