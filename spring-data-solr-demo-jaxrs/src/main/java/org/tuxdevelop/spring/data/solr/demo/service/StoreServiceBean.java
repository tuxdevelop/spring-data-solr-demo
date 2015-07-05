package org.tuxdevelop.spring.data.solr.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;

import java.util.Collection;

@Component
public class StoreServiceBean implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Collection<Store> findByName(String name) {
        return storeRepository.findByName(name);
    }
}
