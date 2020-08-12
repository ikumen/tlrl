package com.gnoht.tlrl.search.solr;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = SolrConfigProperties.CONFIG_PREFIX)
public class SolrConfigProperties {
  static final String CONFIG_PREFIX = "app.solr";
  private final String[] baseUrls;

  public SolrConfigProperties(String[] baseUrls) {
    if (baseUrls == null || baseUrls.length == 0)
      throw new IllegalArgumentException(CONFIG_PREFIX + ".baseUrls is missing.");
    this.baseUrls = baseUrls;
  }
  
  public String[] baseUrls() {
    return baseUrls;
  }

  public boolean isSingleSolrServer() {
    return baseUrls.length == 1;
  }  
}