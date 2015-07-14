package org.tuxdevelop.spring.data.solr.demo.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;

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
        final StarbucksStore starbucksStore = new StarbucksStore();
        starbucksStore.setStreet("Test Street");
        starbucksStore.setCity("Test City");
        starbucksStore.setLocation(new Point(40D, 40D));
        starbucksStore.setName("TestStarbucksStore");
        starbucksStore.setProducts(products);
        starbucksStore.setServices(services);
        starbucksStore.setZipCode("12345");
        final StarbucksStore addedStarbucksStore = storeService.add(starbucksStore);
        assertThat(addedStarbucksStore).isNotNull();
        final StarbucksStore gotStarbucksStore = storeService.get(addedStarbucksStore.getId());
        assertThat(gotStarbucksStore).isNotNull();
        assertThat(gotStarbucksStore.getProducts()).contains("Tux");
        System.err.println(gotStarbucksStore);
        storeService.delete(addedStarbucksStore.getId());
    }

    @Test
    public void getIT() {
        final String id = "1";
        final StarbucksStore starbucksStore = storeService.get(id);
        assertThat(starbucksStore).isNotNull();
        System.err.println(starbucksStore);
    }

    @Test
    public void deleteIT() {
        final Collection<String> products = new LinkedList<>();
        products.add("Tux");
        final Collection<String> services = new LinkedList<>();
        services.add("Live Hacking");
        final StarbucksStore starbucksStore = new StarbucksStore();
        starbucksStore.setStreet("Test Street");
        starbucksStore.setCity("Test City");
        starbucksStore.setLocation(new Point(40D, 40D));
        starbucksStore.setName("TestStarbucksStore");
        starbucksStore.setProducts(products);
        starbucksStore.setServices(services);
        starbucksStore.setZipCode("12345");
        final StarbucksStore addedStarbucksStore = storeService.add(starbucksStore);
        assertThat(addedStarbucksStore).isNotNull();
        storeService.delete(addedStarbucksStore.getId());
        final StarbucksStore gotStarbucksStore = storeService.get(addedStarbucksStore.getId());
        assertThat(gotStarbucksStore).isNull();
    }

    @Test
    public void findByNameSystemIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<StarbucksStore> starbucksStores = storeService.findByName(nameToFind);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }

    @Test
    public void findNearIT() {
        final Double latitude = 43D;
        final Double longtitude = -71D;
        final Double distance = 10D;
        final Collection<StarbucksStore> starbucksStores = storeService.findNear(longtitude, latitude, distance, null);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(2);
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
        final Collection<StarbucksStore> starbucksStores = storeService.findNear(longtitude, latitude, distance, products);
        assertThat(starbucksStores).isNotEmpty();
        assertThat(starbucksStores).hasSize(1);
    }

}
