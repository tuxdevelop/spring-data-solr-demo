package org.tuxdevelop.spring.data.solr.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;
import org.tuxdevelop.spring.data.solr.demo.repository.StarbucksStoreRepository;

import java.util.Collection;
import java.util.List;

@Component
public class StoreServiceBean implements StoreService {

    @Autowired
    private StarbucksStoreRepository starbucksStoreRepository;

    @Override
    public StarbucksStore add(final StarbucksStore starbucksStore) {
        final long count = starbucksStoreRepository.count();
        final String id = new String("" + (count + 1));
        starbucksStore.setId(id);
        return starbucksStoreRepository.save(starbucksStore);
    }

    @Override
    public StarbucksStore get(final String id) {
        return starbucksStoreRepository.findOne(id);
    }

    @Override
    public void delete(final String id) {
        starbucksStoreRepository.delete(id);
    }

    @Override
    public Collection<StarbucksStore> findByName(String name) {
        return starbucksStoreRepository.findByName(name);
    }

    @Override
    public Collection<StarbucksStore> findNear(final Double longtitude, final Double latitude, final Double distance,
                                      final List<String> products) {
        final Point point = new Point(latitude, longtitude);
        final Distance dist = new Distance(distance);
        final Collection<StarbucksStore> starbucksStores;
        if (products == null || products.isEmpty()) {
            starbucksStores = starbucksStoreRepository.findByLocationNear(point, dist);
        } else {
            starbucksStores = starbucksStoreRepository.findByProductsInAndLocationNear(products, point, dist);
        }
        return starbucksStores;
    }
}
