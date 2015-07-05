package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.data.solr.demo.service.StoreServiceBean;

@Configuration
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        register(StoreServiceBean.class);
        register(JacksonFeature.class);
    }

}
