package org.tuxdevelop.spring.data.solr.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;

import java.util.Collection;
import java.util.List;

@Component
public class StoreServiceBean implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Store add(final Store store) {
        final long count = storeRepository.count();
        final String id = new String("" + (count + 1));
        store.setId(id);
        return storeRepository.save(store);
    }

    @Override
    public void delete(final String id) {
        storeRepository.delete(id);
    }

    @Override
    public Collection<Store> findByName(String name) {
        return storeRepository.findByName(name);
    }

    @Override
    public Collection<Store> findNear(final Double longtitude, final Double latitude, final Double distance,
                                      final List<String> products) {
        final Point point = new Point(latitude, longtitude);
        final Distance dist = new Distance(distance);
        final Collection<Store> stores;
        if (products == null || products.isEmpty()) {
            stores = storeRepository.findByLocationNear(point, dist);
        } else {
            stores = storeRepository.findByProductsInAndLocationNear(products, point, dist);
        }
        return stores;
    }
}
