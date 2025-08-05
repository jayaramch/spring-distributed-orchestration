package com.test.distributed.poc.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.test.distributed.poc.kafka.models.PodDetails;

@Service
public class KafkaProducer {

//    private final KafkaTemplate<String, String> kafkaTemplate;
    
    private final KafkaTemplate<String, PodDetails> kafkaProducerTemplate;
    
    private static final String KAFKA_TOPIC = "test-topic";

//    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
    
    public KafkaProducer(KafkaTemplate<String, PodDetails> kafkaProducerTemplate) {
        this.kafkaProducerTemplate = kafkaProducerTemplate;
    }

//    public void sendMessage(String message) {
//        kafkaTemplate.send(KAFKA_TOPIC, message);
//    }
    
    public void sendMessage(PodDetails podDetails) {
    	kafkaProducerTemplate.send(KAFKA_TOPIC, podDetails);
    }
}
