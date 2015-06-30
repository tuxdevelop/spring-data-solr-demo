package org.tuxdevelop.spring.data.solr.demo.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;


public interface StoreRepository extends SolrCrudRepository<Store, String>, StoreCrudOperations {

    Collection<Store> findByName(@Param("name")final String name);

    Collection<Store> findByProductsIn(final Collection<String> products);

    Collection<Store> findByProductsInAndLocationNear(final Collection<String> products, final Point point, final
    Distance distance);

    @Query("name:?0")
    Collection<Store> findByNameQuery(@Param("name")final String name);

    @Query(value = "*:*", filters = {"name:=?0"})
    Collection<Store> findByNameFilterQuery(@Param("name") final String name);

    Collection<Store> findByLocationNear(final Point point, final Distance distance);

}
