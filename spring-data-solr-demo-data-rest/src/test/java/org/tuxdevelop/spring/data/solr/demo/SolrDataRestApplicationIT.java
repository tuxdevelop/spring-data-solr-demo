package org.tuxdevelop.spring.data.solr.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.tuxdevelop.spring.data.solr.demo.configuration.ITConfiguration;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ITConfiguration.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SolrDataRestApplicationIT extends ITTests {


    @Autowired
    private EmbeddedWebApplicationContext server;

    @Before
    public void init() {
        baseUrl = HTTP_LOCALHOST + server.getEmbeddedServletContainer().getPort() + DATA_SOLR_STORES;
    }

}
