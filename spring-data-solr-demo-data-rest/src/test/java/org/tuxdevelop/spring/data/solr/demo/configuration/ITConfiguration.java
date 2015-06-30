package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.data.solr.demo.SolrDataRestApplication;

@Configuration
@EnableAutoConfiguration
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value =
        SolrDataRestApplication.class))
public class ITConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
