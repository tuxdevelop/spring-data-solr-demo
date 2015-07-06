package org.tuxdevelop.spring.data.solr.demo.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CommonStoreServiceIT {

    @Autowired
    private StoreService storeService;

    @Test
    public void addIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Tux");
        final Collection<String> services = new LinkedList<>();
        services.add("Live Hacking");
        final Store store = new Store();
        store.setStreet("Test Street");
        store.setCity("Test City");
        store.setLocation(new Point(40D, 40D));
        store.setName("TestStarbucksStore");
        store.setProducts(products);
        store.setServices(services);
        store.setZipCode("12345");
        final Store addedStore = storeService.add(store);
        assertThat(addedStore).isNotNull();
        storeService.delete(addedStore.getId());
    }

    @Test
    public void findByNameSystemIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeService.findByName(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

    @Test
    public void findNearIT() {
        final Double latitude = 43D;
        final Double longtitude = -71D;
        final Double distance = 10D;
        final Collection<Store> stores = storeService.findNear(longtitude, latitude, distance, null);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(2);
    }

    @Test
    public void findNearWithProductsIT() {
        final List<String> products = new LinkedList<>();
        products.add("Lunch");
        products.add("Oven-warmed Food");
        products.add("Reserve Amenity");
        final Double latitude = 43D;
        final Double longtitude = -71D;
        final Double distance = 10D;
        final Collection<Store> stores = storeService.findNear(longtitude, latitude, distance, products);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

}
