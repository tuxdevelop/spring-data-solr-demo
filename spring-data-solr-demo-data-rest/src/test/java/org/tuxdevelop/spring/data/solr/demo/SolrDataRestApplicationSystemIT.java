package org.tuxdevelop.spring.data.solr.demo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.data.solr.demo.configuration.SystemITConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SystemITConfiguration.class)
public class SolrDataRestApplicationSystemIT extends ITTests {

	@Before
	public void init() {
		baseUrl = HTTP_LOCALHOST + "9010" + DATA_SOLR_STORES;
	}

}
