package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.tuxdevelop.spring.data.solr.demo.SolrApplication;


@Configuration
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
@ComponentScan(basePackages = "org.tuxdevelop.spring.data.solr.demo", excludeFilters = @ComponentScan.Filter(value =
        SolrApplication.class, type = FilterType.ASSIGNABLE_TYPE))
public class ITConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("application.properties"));
        return propertyPlaceholderConfigurer;
    }
}
