package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.tuxdevelop.spring.data.solr.demo.StarbucksJaxRSApplication;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;

@Configuration
@ComponentScan(excludeFilters = {@ComponentScan.Filter(value = StarbucksJaxRSApplication.class, type = FilterType
        .ASSIGNABLE_TYPE)})
public class SystemITConfiguration {

    @Bean
    public StoreService storeService() {
        return null;
    }
}
