package org.tuxdevelop.spring.data.solr.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import java.util.Collection;
import java.util.List;


public interface StoreRepository extends SolrCrudRepository<Store, String>, StoreCrudOperations {

    Collection<Store> findByName(@Param("name") final String name);

    Collection<Store> findByProductsIn(final Collection<String> products);

    Collection<Store> findByProductsInAndLocationNear(final Collection<String> products, final Point point, final
    Distance distance);

    @Query("name:?0")
    Collection<Store> findByNameQuery(@Param("name") final String name);

    @Query(value = "*:*", filters = {"name:=?0"})
    Collection<Store> findByNameFilterQuery(@Param("name") final String name);

    Collection<Store> findByLocationNear(final Point point, final Distance distance);

    @Query(value = "zipCode:?0")
    @Facet(fields = {"products"})
    FacetPage<Store> findByZipCodeFacetOnProducts(@Param("zipCode") final String zipCode, final Pageable page);

    @Query(value = "*:*", filters = "products:?0")
    @Facet(fields = {"name"})
    FacetPage<Store> findFacetOnName(@Param("products") final List<String> products, final Pageable page);
}
