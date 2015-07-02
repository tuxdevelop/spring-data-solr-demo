package org.tuxdevelop.spring.data.solr.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
public class SolrDataRestApplication {

	@Autowired
	private SolrInitializer solrInitializer;

	@Autowired
	private StoreRepository storeRepository;

	public static void main(final String[] args) {
		SpringApplication.run(SolrDataRestApplication.class, args);
	}

	@PostConstruct
	public void init() throws Exception {
		solrInitializer.importStarbucks();
	}

	@PreDestroy
	public void deleteAll() {
		storeRepository.deleteAll();
	}
}
