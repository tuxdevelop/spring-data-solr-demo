package org.tuxdevelop.spring.data.solr.demo.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tuxdevelop.spring.data.solr.demo.api.StoreService;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CommonStoreServiceIT {

    @Autowired
    private StoreService storeService;

    @Test
    public void findByNameSystemIT() {
        final String nameToFind = "Steaming Kettle";
        final Collection<Store> stores = storeService.findByName(nameToFind);
        assertThat(stores).isNotEmpty();
        assertThat(stores).hasSize(1);
    }

}
