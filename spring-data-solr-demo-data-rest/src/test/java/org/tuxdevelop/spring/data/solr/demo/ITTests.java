package org.tuxdevelop.spring.data.solr.demo;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

@Slf4j
public abstract class ITTests {

    protected static final String HTTP_LOCALHOST = "http://localhost:";
    protected static final String DATA_SOLR_STORES = "/dataSolr/stores";
    protected static final String SEARCH = "/search";
    protected static final String FIND_BY_NAME = "/findByName";
    protected static final String FIND_BY_NAME_QUERY = "/findByNameQuery";

    @Autowired
    private RestTemplate restTemplate;

    protected String baseUrl;

    @Test
    public void findByIdIT() {
        final Integer id = 1;
        final HttpEntity<?> httpEntity = getHttpEntity();
        final String uri = baseUrl + "/{id}";
        log.info("query url: " + uri);
        final ResponseEntity<Store> response =
                restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
                        Store.class, id);
        System.out.println(response);
    }

    @Test
    public void findByNameIT() {
        final HttpEntity<?> httpEntity = getHttpEntity();
        final String url = baseUrl + SEARCH + FIND_BY_NAME;
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("name", "Steaming Kettle");

        final String uri = uriComponentsBuilder.build().encode().toUriString();
        log.info("query url: " + uri);

        final ResponseEntity<Store> response =
                restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
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

    private HttpEntity<?> getHttpEntity() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(httpHeaders);
    }

}
