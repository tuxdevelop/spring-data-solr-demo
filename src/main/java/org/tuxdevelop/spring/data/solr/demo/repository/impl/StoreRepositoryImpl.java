package org.tuxdevelop.spring.data.solr.demo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreCrudOperations;

import java.util.Collection;

public class StoreRepositoryImpl implements StoreCrudOperations {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void updateProducts(String id, Collection<String> products) {
        final PartialUpdate partialUpdate = new PartialUpdate("id", id);
        partialUpdate.add("products", products);
        solrTemplate.saveBean(partialUpdate);
        solrTemplate.commit();
    }
}
