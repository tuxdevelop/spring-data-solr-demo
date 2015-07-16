package org.tuxdevelop.spring.data.solr.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;

import java.util.Collection;
import java.util.List;


public interface StarbucksStoreRepository extends SolrCrudRepository<StarbucksStore, String>, StarbucksStoreCrudOperations {

    /**
     * find stores by name
     *
     * @param name the name of store
     * @return a collection of stores, where the input match
     */
    Collection<StarbucksStore> findByName(@Param("name") final String name);


    /**
     * find all stores whoch provides the given products
     *
     * @param products products query parameter
     * @return a collection of stores, where the input match
     */
    Collection<StarbucksStore> findByProductsIn(@Param("products") final Collection<String> products);
    
    /**
     * find all stores which provides the given products and are next to a geo point and given distance
     *
     * @param products products query parameter
     * @param point    geo location
     * @param distance radius distance to the geo point
     * @return a collection of stores, where the input match
     */
    Collection<StarbucksStore> findByProductsInAndLocationNear(@Param("products") final Collection<String> products, @Param
            ("point") final Point point, final @Param("distance") Distance distance);

    /**
     * find stores by name
     *
     * @param name the name of store
     * @return a collection of stores, where the input match
     */
    @Query("name:?0")
    Collection<StarbucksStore> findByNameQuery(@Param("name") final String name);

    /**
     * find stores by name
     *
     * @param name the name of store
     * @return a collection of stores, where the input match
     */
    @Query(value = "*:*", filters = {"name:=?0"})
    Collection<StarbucksStore> findByNameFilterQuery(@Param("name") final String name);

    /**
     * find all stores which are next to a geo point and given distance
     *
     * @param point    geo location
     * @param distance radius distance to the geo point
     * @return a collection of stores, where the input match
     */
    Collection<StarbucksStore> findByLocationNear(@Param("point") final Point point, @Param("distance") final Distance distance);

    /**
     * find all stores of a give zip code
     *
     * @param zipCode query parameter
     * @param page    input page
     * @return a facet of the products of stores, where the input matches
     */
    @Query(value = "zipCode:?0")
    @Facet(fields = {"products"})
    FacetPage<StarbucksStore> findByZipCodeFacetOnProducts(@Param("zipCode") final String zipCode, final Pageable page);

    /**
     * find all stores
     *
     * @param page input page
     * @return a facet of the products of stores, where the input matches
     */
    @Query(value = "*:*")
    @Facet(fields = {"products"})
    FacetPage<StarbucksStore> findByFacetOnProducts(final Pageable page);

    /**
     * find all stores which provides the given products
     *
     * @param products query param
     * @param page     input page
     * @return a facet of the names of stores, where the input matches
     */
    @Query(value = "*:*", filters = "products:?0")
    @Facet(fields = {"name"})
    FacetPage<StarbucksStore> findFacetOnName(@Param("products") final List<String> products, final Pageable page);
}
