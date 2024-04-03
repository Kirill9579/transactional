package ru.test.transactional;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.messaging.MessageChannel;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.test.transactional.entity.Person;
import ru.test.transactional.service.PersonService;

import java.util.UUID;

@SpringBootApplication
public class TransactionalApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(TransactionalApplication.class, args);
        var service = context.getBean(PersonService.class);
        service.save(new Person("Name " + UUID.randomUUID()));
    }
    @Bean
    KafkaTransactionManager customKafkaTransactionManager(BinderFactory binderFactory) {
        KafkaMessageChannelBinder kafka = (KafkaMessageChannelBinder) binderFactory.getBinder("kafka", MessageChannel.class);
        ProducerFactory<byte[], byte[]> transactionalProducerFactory = kafka.getTransactionalProducerFactory();
        KafkaTransactionManager kafkaTransactionManager = new KafkaTransactionManager(transactionalProducerFactory);
        return kafkaTransactionManager;
    }
    @Bean
    public PlatformTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

    @Bean
    public ChainedKafkaTransactionManager chainedKafkaTransactionManager(KafkaTransactionManager kafkaTransactionManager, PlatformTransactionManager transactionManager) {
        return new ChainedKafkaTransactionManager(kafkaTransactionManager, transactionManager);
    }

}