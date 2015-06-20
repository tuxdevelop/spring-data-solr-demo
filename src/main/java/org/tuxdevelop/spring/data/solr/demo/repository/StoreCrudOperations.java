package org.tuxdevelop.spring.data.solr.demo.repository;


import java.util.Collection;

public interface StoreCrudOperations {

    void updateProducts(final String id, final Collection<String> products);

}
