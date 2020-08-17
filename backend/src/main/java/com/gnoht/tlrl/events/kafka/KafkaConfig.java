package com.gnoht.tlrl.events.kafka;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * Enable and configure the Kafka admin.
 * @author ikumen@gnoht.com
 */
@Configuration
@EnableKafka
public class KafkaConfig {
  private final String bootstrapServers;

  @Inject
  public KafkaConfig(@Value("${app.kafka.bootstrap-servers}") String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return new KafkaAdmin(configs);
  }
}
