package org.tuxdevelop.spring.data.solr.demo.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.data.solr.demo.configuration.ITConfiguration;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.util.SolrInitializer;

import java.util.Collection;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class StoreRepositoryIT {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private SolrInitializer solrInitializer;

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
    public void findByLocationNearIT() {
        final Point point = new Point(43, -71);
        final Distance distance = new Distance(10);
        final Collection<Store> stores = storeRepository.findByLocationNear(point, distance);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(2);
    }

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

}
