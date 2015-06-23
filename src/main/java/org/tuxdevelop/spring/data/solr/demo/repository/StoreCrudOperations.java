package org.tuxdevelop.spring.data.solr.demo.repository;


import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;

public interface StoreCrudOperations {

    void updateProducts(final String id, final Collection<String> products);

    Collection<Store> findStoreByNameFilterQuery(String name);
}
