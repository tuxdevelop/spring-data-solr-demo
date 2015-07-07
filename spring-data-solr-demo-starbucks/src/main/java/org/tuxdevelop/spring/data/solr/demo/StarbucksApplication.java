package org.tuxdevelop.spring.data.solr.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
public class StarbucksApplication {

    @Autowired
    private SolrInitializer solrInitializer;

    public static void main(final String[] args) {
        SpringApplication.run(StarbucksApplication.class, args);
    }

    @PostConstruct
    public void init() throws Exception {
        solrInitializer.importStarbucks();
    }
}
