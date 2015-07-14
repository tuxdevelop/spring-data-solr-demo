package org.tuxdevelop.spring.data.solr.demo.repository;


import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;

import java.util.Collection;
import java.util.List;

public interface StarbucksStoreCrudOperations {

    void updateProducts(final String id, final Collection<String> products);

    Collection<StarbucksStore> findStoreByNameFilterQuery(@Param("name") final String name);

    FacetPage<StarbucksStore> findFacetOnNameSolrTemplate(@Param("products") final List<String> products);

    GroupPage<StarbucksStore> groupByZipCode(@Param("products") final List<String> products);

    GroupPage<StarbucksStore> findByLocationAndgroupByCity(final Point point, final Distance distance, final Integer
            pageNumber);

    GroupPage<StarbucksStore> groupByCity();
}
