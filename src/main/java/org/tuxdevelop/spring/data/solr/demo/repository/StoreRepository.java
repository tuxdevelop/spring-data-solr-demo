package org.tuxdevelop.spring.data.solr.demo.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;


public interface StoreRepository extends SolrCrudRepository<Store, String>, StoreCrudOperations {

    Collection<Store> findByName(final String name);

    @Query("name:?0")
    Collection<Store> findByNameQuery(final String name);

    Collection<Store> findByLocationNear(final Point point, final Distance distance);

}
