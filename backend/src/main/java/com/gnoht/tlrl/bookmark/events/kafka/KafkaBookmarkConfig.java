package com.gnoht.tlrl.bookmark.events.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gnoht.tlrl.bookmark.Bookmark;

/**
 * @author ikumen@gnoht.com
 */
@Configuration
@EnableConfigurationProperties({KafkaBookmarkConfig.EventConfigProperties.class})
public class KafkaBookmarkConfig {

  private final String groupId;
  private final String bootstrapServers;
  private final EventConfigProperties eventConfig;

  public KafkaBookmarkConfig(
      @Value("${app.kafka.groupId}") String groupId,
      @Value("${app.kafka.bootstrap-servers}") String bootstrapServers,
      EventConfigProperties eventConfig) {
    this.groupId = groupId;
    this.bootstrapServers = bootstrapServers;
    this.eventConfig = eventConfig;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public EventConfigProperties getEventConfig() {
    return eventConfig;
  }

  @Bean(name = "createdTopic")
  public NewTopic newCreatedTopic(EventConfigProperties eventConfig) {
    return TopicBuilder.name(eventConfig.created())
        .partitions(1).compact().build();
  }

  @Bean(name = "updatedTopic")
  public NewTopic newUpdatedTopic(EventConfigProperties eventConfig) {
    return TopicBuilder.name(eventConfig.updated())
        .partitions(1).compact().build();
  }

  @Bean(name = "archivedTopic")
  public NewTopic newArchivedTopic(EventConfigProperties eventConfig) {
    return TopicBuilder.name(eventConfig.archived())
        .partitions(1).compact().build();
  }

  @Bean(name = "createdTopic")
  public NewTopic newDeletedTopic(EventConfigProperties eventConfig) {
    return TopicBuilder.name(eventConfig.deleted())
        .partitions(1).compact().build();
  }

  private Map<String, Object> producerConfigs() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return configs;
  }

  private Map<String, Object> consumerConfigs() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return configs;
  }

  private ConsumerFactory<String, Bookmark> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
        new StringDeserializer(), new JsonDeserializer<>(Bookmark.class));
  }

  private ProducerFactory<String, List<Bookmark>> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  /**
   * Container factory that provides Kafka Consumer for handling Bookmark payloads.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Bookmark> bookmarkKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Bookmark> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean(name = "bookmarkKafkaTemplate")
  public KafkaTemplate<String, List<Bookmark>> bookmarkKafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @ConstructorBinding
  @ConfigurationProperties(prefix = "app.events.bookmark")
  public static class EventConfigProperties {
    private final String archived;
    private final String created;
    private final String deleted;
    private final String updated;

    public EventConfigProperties(String archived, String created, String deleted, String updated) {
      this.archived = archived;
      this.created = created;
      this.deleted = deleted;
      this.updated = updated;
    }

    public String archived() {
      return archived;
    }

    public String created() {
      return created;
    }

    public String deleted() {
      return deleted;
    }

    public String updated() {
      return updated;
    }
  }
}
