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
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class StarbucksStoreRepositoryIT {

    @Autowired
    private StarbucksStoreRepository starbucksStoreRepository;

    @Autowired
    private SolrInitializer solrInitializer;


    /*
     * init solr
     */

    @Before
    public void init() throws Exception {
        solrInitializer.importStarbucks();
    }

    /*
     * Search IT
     */

    @Test
    public void findByNameIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByName(nameToFind);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }

    @Test
    public void findByNameQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByNameQuery(nameToFind);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }

    @Test
    public void findByNameFilterQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByNameFilterQuery(nameToFind);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }

    @Test
    public void findStoreByNameFilterQueryIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findStoreByNameFilterQuery(nameToFind);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }


    @Test
    public void findByLocationNearIT() {
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(10);
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByLocationNear(point, distance);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(2);
        for (StarbucksStore starbucksStore : starbucksStores) {
            System.out.println(starbucksStore);
        }
    }

    @Test
    public void findByProductsInIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Lunch");
        products.add("Oven-warmed Food");
        products.add("Reserve Amenity");
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByProductsIn(products);
        assertThat(starbucksStores).isNotEmpty();
        System.out.println("Size: " + starbucksStores.size());
    }

    @Test
    public void findByPoductsInAndLocationNearIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Lunch");
        products.add("Oven-warmed Food");
        products.add("Reserve Amenity");
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(10);
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByProductsInAndLocationNear(products, point, distance);
        assertThat(starbucksStores).hasSize(1);
    }

    /*
     * Solr Operations IT
     */

    @Test
    public void updateProductsIT() {
        final String newProduct = "1&1 Handy Flat!";
        final String id = "1";
        final StarbucksStore starbucksStore = starbucksStoreRepository.findOne(id);
        assertThat(starbucksStore).isNotNull();
        final Collection<String> products = starbucksStore.getProducts();
        final Collection<String> newProducts = new LinkedList<>();
        newProducts.add(newProduct);
        starbucksStoreRepository.updateProducts(id, newProducts);
        final StarbucksStore updatedStarbucksStore = starbucksStoreRepository.findOne(id);
        assertThat(updatedStarbucksStore).isNotNull();
        final Collection<String> updatedProducts = updatedStarbucksStore.getProducts();
        assertThat(products).isNotEqualTo(updatedProducts);
        assertThat(updatedProducts).contains(newProduct);
    }

    /*
     * Facetting
     */

    @Test
    public void findByZipCodeFacetOnProductsIT() {
        final String zipCode = "03833-2105";
        final FacetPage<StarbucksStore> response = starbucksStoreRepository.findByZipCodeFacetOnProducts(zipCode, new
                PageRequest(0, 20));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getContent()).hasSize(3);
        for (final StarbucksStore starbucksStore : response.getContent()) {
            System.err.println(starbucksStore);
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
        final FacetPage<StarbucksStore> response = starbucksStoreRepository.findFacetOnName(products, new PageRequest(0, 20));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        for (final StarbucksStore starbucksStore : response.getContent()) {
            System.err.println(starbucksStore);
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
        final FacetPage<StarbucksStore> response = starbucksStoreRepository.findFacetOnNameSolrTemplate(products);
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        for (final StarbucksStore starbucksStore : response.getContent()) {
            System.err.println(starbucksStore);
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
        final FacetPage<StarbucksStore> response = starbucksStoreRepository.findByFacetOnProducts(new
                PageRequest(0, 100));
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getContent()).hasSize(100);
        for (final StarbucksStore starbucksStore : response.getContent()) {
            System.err.println(starbucksStore);
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
    public void groupByCityIT() {
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(100);
        final Integer pageNumber = 0;
        final GroupPage<StarbucksStore> response = starbucksStoreRepository.findByLocationAndgroupByCity(point, distance, pageNumber);
        GroupResult<StarbucksStore> groupResult = response.getGroupResult("city");
        final Page<GroupEntry<StarbucksStore>> groupEntries = groupResult.getGroupEntries();
        for (final GroupEntry<StarbucksStore> groupEntry : groupEntries) {
            final String groupValue = groupEntry.getGroupValue();
            System.err.println("groupValue: " + groupValue);
            final List<StarbucksStore> groupContet = groupEntry.getResult().getContent();
            System.err.println("Members of the group: ");
            for (final StarbucksStore starbucksStore : groupContet) {
                System.err.println(starbucksStore);
            }
        }
    }

    @Test
    public void groupByZipCodeIT() {
        final String lunch = "Lunch";
        final List<String> products = new LinkedList<>();
        products.add(lunch);
        final GroupPage<StarbucksStore> response = starbucksStoreRepository.groupByZipCode(products);
        System.err.println("-----------------------");
        GroupResult<StarbucksStore> groupResult = response.getGroupResult("zipCode");
        final Page<GroupEntry<StarbucksStore>> groupEntries = groupResult.getGroupEntries();
        for (final GroupEntry<StarbucksStore> groupEntry : groupEntries) {
            final String groupValue = groupEntry.getGroupValue();
            System.err.println("groupValue: " + groupValue);
            final List<StarbucksStore> groupContet = groupEntry.getResult().getContent();
            System.err.println("Members of the group: ");
            for (final StarbucksStore starbucksStore : groupContet) {
                System.err.println(starbucksStore);
            }
        }
    }
}
