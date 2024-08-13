package com.mp.fintech.config;

import org.springframework.kafka.support.serializer.JsonSerializer;
import com.mp.fintech.constants.ApplicationConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(ApplicationConstants.MP_MSG_TOPIC, 3, (short) 1); //3 - Partitions and 1 replication factor
    }

    @Bean
    public NewTopic createDLQTopic(){
        return new NewTopic(ApplicationConstants.MP_MSG_TOPIC_DLQ, 1, (short) 1); //3 - Partitions and 1 replication factor
    }

    @Bean
    public Map<String,Object> config(){
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,      bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String,Object> factory(){
        return new DefaultKafkaProducerFactory<>(config());
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(factory());
    }

}
