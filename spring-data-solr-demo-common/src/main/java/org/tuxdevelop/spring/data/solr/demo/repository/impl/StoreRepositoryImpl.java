package org.tuxdevelop.spring.data.solr.demo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreCrudOperations;

import java.util.Collection;
import java.util.List;

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

    @Override
    public Collection<Store> findStoreByNameFilterQuery(final String name) {
        final SimpleQuery query = new SimpleQuery(new Criteria());
        final FilterQuery filterQuery = new SimpleFilterQuery(new Criteria("name").is(name));
        query.addFilterQuery(filterQuery);
        ScoredPage<Store> result = solrTemplate.queryForPage(query, Store.class);
        return result.getContent();
    }

    @Override
    public FacetPage<Store> findFacetOnNameSolrTemplate(final List<String> products) {
        final String parameter = convertListToParameterString(products);
        final FacetQuery facetQuery = new SimpleFacetQuery(new SimpleStringCriteria("*:*"))
                .setFacetOptions(new FacetOptions("name"));
        final FilterQuery filterQuery = new SimpleFilterQuery(new SimpleStringCriteria("products:" + parameter));
        facetQuery.setPageRequest(new PageRequest(0, 20));
        facetQuery.addFilterQuery(filterQuery);
        return solrTemplate.queryForFacetPage(facetQuery, Store.class);
    }

    private String convertListToParameterString(final List<String> parameterList) {
        final String temp = parameterList.toString();
        final String temp1 = temp.replace("]", "");
        final String result = temp1.replace("[", "");
        return result;
    }
}
