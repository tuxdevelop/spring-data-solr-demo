package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.tuxdevelop.spring.data.solr.demo.StarbucksJaxRSApplication;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Configuration
@EnableAutoConfiguration
@EnableSolrRepositories(basePackages = "org.tuxdevelop.spring.data.solr.demo.repository")
@ComponentScan(excludeFilters = {@ComponentScan.Filter(value = StarbucksJaxRSApplication.class, type = FilterType
        .ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = SystemITConfiguration.class, type = FilterType.ASSIGNABLE_TYPE)})
@Import(value = {SolrInitializer.class, SolrConfiguration.class})
public class ITConfiguration {

    private static final String PORT = "9012";

    @Autowired
    private SolrInitializer solrInitializer;

    @Bean
    public StoreService storeService() {
        final ClientConfig cc = new ClientConfig();
        final Client client = ClientBuilder.newClient(cc);
        client.register(new JacksonFeature());
        final WebTarget target = client.target("http://localhost:" + PORT + "/dataSolr/api");
        return WebResourceFactory.newResource(StoreService.class, target);
    }

    @PostConstruct
    public void init() throws Exception {
        solrInitializer.importStarbucks();
    }

}
