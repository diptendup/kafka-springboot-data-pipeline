package com.mp.fintech.config;

import com.mp.fintech.constants.ApplicationConstants;
import com.mp.fintech.service.KafkaMessageListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Bean
    public Map<String,Object> config(){
        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,        bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,                 ApplicationConstants.MP_MSG_GROUP);
        props.put(JsonDeserializer.TRUSTED_PACKAGES,              "com.mp.fintech.entity");
        return props;
    }

    @Bean
    public ConsumerFactory<String,Object> factory(){
        return new DefaultKafkaConsumerFactory<>(config());
    }

    @Bean("kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        String deadLetterTopic = ApplicationConstants.MP_MSG_TOPIC_DLQ;
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(factory());
        factory.setConcurrency(ApplicationConstants.SET_CONCURRENCY_LEVEL);

        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate, (record, ex) -> {
                    log.info("Exception {} occurred while processing record at the consumer side {}", ex.getMessage(), deadLetterTopic);
                    return new TopicPartition(deadLetterTopic, -1);
                });
        CommonErrorHandler errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer, new FixedBackOff(10L, 5L));
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

}
