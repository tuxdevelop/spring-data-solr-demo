package org.tuxdevelop.spring.data.solr.demo.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.data.solr.demo.configuration.SystemITConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SystemITConfiguration.class)
public class StoreServiceBeanSystemIT extends CommonStoreServiceIT{



}
