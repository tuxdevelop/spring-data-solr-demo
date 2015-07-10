package org.tuxdevelop.spring.data.solr.demo.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.data.solr.demo.configuration.ITConfiguration;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class StoreRepositoryIT {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private SolrInitializer solrInitializer;

    /*
     * Search IT
     */

    @Test
    public void findByNameIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeRepository.findByName(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

    @Test
    public void findByNameQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeRepository.findByNameQuery(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

    @Test
    public void findByNameFilterQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeRepository.findByNameFilterQuery(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

    @Test
    public void findStoreByNameFilterQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeRepository.findStoreByNameFilterQuery(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }


    @Test
    public void findByLocationNearIT() {
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(10);
        final Collection<Store> stores = storeRepository.findByLocationNear(point, distance);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(2);
        for (Store store : stores) {
            System.out.println(store);
        }
    }

    @Test
    public void findByProductsInIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Lunch");
        products.add("Oven-warmed Food");
        products.add("Reserve Amenity");
        final Collection<Store> stores = storeRepository.findByProductsIn(products);
        assertThat(stores).isNotEmpty();
        System.out.println("Size: " + stores.size());
    }

    @Test
    public void findByPoductsInAndLocationNearIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Lunch");
        products.add("Oven-warmed Food");
        products.add("Reserve Amenity");
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(10);
        final Collection<Store> stores = storeRepository.findByProductsInAndLocationNear(products, point, distance);
        assertThat(stores).hasSize(1);
    }

    /*
     * Solr Operations IT
     */

    @Test
    public void updateProductsIT() {
        final String newProduct = "1&1 Handy Flat!";
        final String id = "1";
        final Store store = storeRepository.findOne(id);
        assertThat(store).isNotNull();
        final Collection<String> products = store.getProducts();
        final Collection<String> newProducts = new LinkedList<>();
        newProducts.add(newProduct);
        storeRepository.updateProducts(id, newProducts);
        final Store updatedStore = storeRepository.findOne(id);
        assertThat(updatedStore).isNotNull();
        final Collection<String> updatedProducts = updatedStore.getProducts();
        assertThat(products).isNotEqualTo(updatedProducts);
        assertThat(updatedProducts).contains(newProduct);
    }

    @Before
    public void init() throws Exception {
        solrInitializer.importStarbucks();
    }

    /*
     * Facetting
     */

    @Test
    public void findByZipCodeFacetOnProductsIT() {
        final String zipCode = "03833-2105";
        final FacetPage<Store> response = storeRepository.findByZipCodeFacetOnProducts(zipCode, new
                PageRequest(0, 20));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getContent()).hasSize(3);
        for (final Store store : response.getContent()) {
            System.err.println(store);
        }
        Page<FacetFieldEntry> page = response.getFacetResultPage("products");
        final int size = page.getSize();
        System.err.println("size:" + size);
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("products");
        List<FacetFieldEntry> content = facetResultPage.getContent();
        System.err.println("content.size:" + content.size());
        for (final FacetFieldEntry facetFieldEntry : content) {
            System.err.println("Found: " + facetFieldEntry.getValue());
        }
    }

    @Test
    public void findFacetOnNameIT() {
        final List<String> products = new LinkedList<>();
        products.add("Lunch");
        final FacetPage<Store> response = storeRepository.findFacetOnName(products, new PageRequest(0, 20));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        for (final Store store : response.getContent()) {
            System.err.println(store);
        }
        final Page<FacetFieldEntry> page = response.getFacetResultPage("name");
        final int size = page.getSize();
        System.err.println("size:" + size);
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("name");
        List<FacetFieldEntry> content = facetResultPage.getContent();
        System.err.println("content.size:" + content.size());
        for (final FacetFieldEntry facetFieldEntry : content) {
            System.err.println("Found: " + facetFieldEntry.getValue());
        }
    }

    @Test
    public void findFacetOnNameSolrTemplateIT() {
        final List<String> products = new LinkedList<>();
        products.add("Lunch");
        final FacetPage<Store> response = storeRepository.findFacetOnNameSolrTemplate(products);
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        for (final Store store : response.getContent()) {
            System.err.println(store);
        }
        final Page<FacetFieldEntry> page = response.getFacetResultPage("name");
        final int size = page.getSize();
        System.err.println("size:" + size);
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("name");
        List<FacetFieldEntry> content = facetResultPage.getContent();
        System.err.println("content.size:" + content.size());
        for (final FacetFieldEntry facetFieldEntry : content) {
            System.err.println("Found: " + facetFieldEntry.getValue());
        }
    }

    @Test
    public void findByFacetOnProductsIT() {
        final FacetPage<Store> response = storeRepository.findByFacetOnProducts(new
                PageRequest(0, 100));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getContent()).hasSize(100);
        for (final Store store : response.getContent()) {
            System.err.println(store);
        }
        final Page<FacetFieldEntry> page = response.getFacetResultPage("products");
        final int size = page.getSize();
        System.err.println("size:" + size);
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("products");
        List<FacetFieldEntry> content = facetResultPage.getContent();
        System.err.println("content.size:" + content.size());
        for (final FacetFieldEntry facetFieldEntry : content) {
            System.err.println("Found: " + facetFieldEntry.getValue());
        }
    }

    /*
     * Grouping
     */

    @Test
    public void groupByZipCodeIT() {
        final String lunch = "Lunch";
        final List<String> products = new LinkedList<>();
        products.add(lunch);
        final GroupPage<Store> response = storeRepository.groupByZipCode(products);
        System.err.println("-----------------------");
        GroupResult<Store> groupResult = response.getGroupResult("zipCode");
        final Page<GroupEntry<Store>> groupEntries = groupResult.getGroupEntries();
        for (final GroupEntry<Store> groupEntry : groupEntries) {
            final String groupValue = groupEntry.getGroupValue();
            System.err.println("groupValue: " + groupValue);
            final List<Store> groupContet = groupEntry.getResult().getContent();
            System.err.println("Members of the group: ");
            for (final Store store : groupContet) {
                System.err.println(store);
            }
        }
    }
}
