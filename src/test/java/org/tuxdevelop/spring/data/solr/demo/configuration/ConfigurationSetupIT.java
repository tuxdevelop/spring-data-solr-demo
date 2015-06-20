package org.tuxdevelop.spring.data.solr.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class ConfigurationSetupIT {

    @Autowired
    private SolrInitializer solrInitializer;

    @Test
    public void init() throws Exception {
        solrInitializer.importStarbucks();
        log.info("Context loaded successfully!");
    }

}
