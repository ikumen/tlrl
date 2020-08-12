package com.gnoht.tlrl.search.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ikumen@gnoht.com
 */
@Configuration
@EnableConfigurationProperties({
  SolrConfigProperties.class
})
public class SolrConfig {
  @Bean
  public SolrClient solrClient(SolrConfigProperties configProperties) {
    if (configProperties.isSingleSolrServer()) {
      return new HttpSolrClient.Builder(
        configProperties.baseUrls()[0]).build();
    } else {
      return new LBHttpSolrClient.Builder()
        .withBaseSolrUrls(configProperties.baseUrls())
        .build();
    }
  }
}