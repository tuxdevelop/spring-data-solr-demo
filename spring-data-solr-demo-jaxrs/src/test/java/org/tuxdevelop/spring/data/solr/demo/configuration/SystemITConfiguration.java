package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.tuxdevelop.spring.data.solr.demo.StarbucksJaxRSApplication;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Configuration
@ComponentScan(excludeFilters = {@ComponentScan.Filter(value = StarbucksJaxRSApplication.class, type = FilterType
        .ASSIGNABLE_TYPE), @ComponentScan.Filter(value = ITConfiguration.class, type = FilterType.ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = SolrConfiguration.class, type = FilterType.ASSIGNABLE_TYPE)})
public class SystemITConfiguration {

    private static final String PORT = "9011";

    @Bean
    public StoreService storeService() {
        final ClientConfig cc = new ClientConfig();
        final Client client = ClientBuilder.newClient(cc);
        client.register(new JacksonFeature());
        final WebTarget target = client.target("http://localhost:" + PORT + "/dataSolr/api");
        return WebResourceFactory.newResource(StoreService.class, target);
    }
}
