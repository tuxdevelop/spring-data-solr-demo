package org.tuxdevelop.spring.data.solr.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;

@Slf4j
@Configuration
public class SolrConfiguration {

    @Value("${solr.solr.home}")
    private String solrHome;

    @Bean
    public EmbeddedSolrServerFactoryBean solrServer() {
        final EmbeddedSolrServerFactoryBean factoryBean = new EmbeddedSolrServerFactoryBean();
        factoryBean.setSolrHome(solrHome);
        return factoryBean;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServer().getObject());
    }

}
