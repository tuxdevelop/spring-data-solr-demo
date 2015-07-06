package org.tuxdevelop.spring.data.solr.demo.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.tuxdevelop.spring.data.solr.demo.configuration.ITConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ITConfiguration.class)
@WebAppConfiguration
@IntegrationTest("server.port:9012")
public class StoreServiceBeanIT extends CommonStoreServiceIT{


}
