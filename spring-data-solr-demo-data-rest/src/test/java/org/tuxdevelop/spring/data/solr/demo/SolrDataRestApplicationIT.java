package org.tuxdevelop.spring.data.solr.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.data.solr.demo.configuration.ITConfiguration;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ITConfiguration.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SolrDataRestApplicationIT {


    private static final String HTTP_LOCALHOST = "http://localhost:";
    private static final String DATA_SOLR_STORES = "/dataSolr/stores";
    private static final String SEARCH = "/search";
    private static final String FIND_BY_NAME = "/findByName";
    private static final String FIND_BY_NAME_QUERY = "/findByNameQuery";

    @Autowired
    private EmbeddedWebApplicationContext server;

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl;

    @Test
    public void findByNameIT() {
        final HttpEntity<?> httpEntity = getHttpEntity();
        final String url = baseUrl + SEARCH + FIND_BY_NAME;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("name", "Steaming Kettle");

        log.info("query url: " + uriComponentsBuilder.build().encode().toUri());

        final ResponseEntity<Store> response =
                restTemplate.exchange(uriComponentsBuilder.build().toUri(), HttpMethod.GET, httpEntity,
                        Store.class);
        System.out.println(response);
    }

    @Test
    public void findByNameQueryIT() {
        final HttpEntity<?> httpEntity = getHttpEntity();
        final String url = baseUrl + SEARCH + FIND_BY_NAME_QUERY;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("name", "Woburn, West Cummings Park");

        log.info("query url: " + uriComponentsBuilder.build().toUri());

        final ResponseEntity<Store> response =
                restTemplate.exchange(uriComponentsBuilder.build().toUri(), HttpMethod.GET, httpEntity,
                        Store.class);
        System.out.println(response);
    }


    @Before
    public void init() {
        baseUrl = HTTP_LOCALHOST + server.getEmbeddedServletContainer().getPort() + DATA_SOLR_STORES;
    }

    private HttpEntity<?> getHttpEntity() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(httpHeaders);
    }

}
