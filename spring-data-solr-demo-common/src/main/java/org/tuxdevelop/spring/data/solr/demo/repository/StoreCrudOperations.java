package org.tuxdevelop.spring.data.solr.demo.repository;


import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;
import java.util.List;

public interface StoreCrudOperations {

    void updateProducts(final String id, final Collection<String> products);

    Collection<Store> findStoreByNameFilterQuery(@Param("name") String name);

    FacetPage<Store> findFacetOnNameSolrTemplate(@Param("products") final List<String> products);
}
