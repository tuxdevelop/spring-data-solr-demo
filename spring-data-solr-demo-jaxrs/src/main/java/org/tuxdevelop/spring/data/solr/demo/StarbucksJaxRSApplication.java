package org.tuxdevelop.spring.data.solr.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@SpringBootApplication
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
public class StarbucksJaxRSApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StarbucksJaxRSApplication.class, args);
    }
}
