package org.tuxdevelop.spring.data.solr.demo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;
import org.tuxdevelop.spring.data.solr.demo.repository.StarbucksStoreCrudOperations;

import java.util.Collection;
import java.util.List;

public class StarbucksStoreRepositoryImpl implements StarbucksStoreCrudOperations {

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
    public Collection<StarbucksStore> findStoreByNameFilterQuery(final String name) {
        final SimpleQuery query = new SimpleQuery(new Criteria());
        final FilterQuery filterQuery = new SimpleFilterQuery(new Criteria("name").is(name));
        query.addFilterQuery(filterQuery);
        ScoredPage<StarbucksStore> result = solrTemplate.queryForPage(query, StarbucksStore.class);
        return result.getContent();
    }

    @Override
    public FacetPage<StarbucksStore> findFacetOnNameSolrTemplate(final List<String> products) {
        final String parameter = convertListToParameterString(products);
        final FacetQuery facetQuery = new SimpleFacetQuery(new SimpleStringCriteria("*:*"))
                .setFacetOptions(new FacetOptions("name"));
        final FilterQuery filterQuery = new SimpleFilterQuery(new SimpleStringCriteria("products:" + parameter));
        facetQuery.setPageRequest(new PageRequest(0, 20));
        facetQuery.addFilterQuery(filterQuery);
        return solrTemplate.queryForFacetPage(facetQuery, StarbucksStore.class);
    }

    @Override
    public GroupPage<StarbucksStore> groupByZipCode(final List<String> products) {
        final Field zipCodeField = new SimpleField("zipCode");
        final Query query = new SimpleQuery(new Criteria("products").contains(products));
        final SimpleQuery groupQuery = new SimpleQuery(new SimpleStringCriteria("*:*"));
        final GroupOptions groupOptions = new GroupOptions()
                .addGroupByField(zipCodeField)
                .addGroupByQuery(query);
        groupQuery.setGroupOptions(groupOptions);
        return solrTemplate.queryForGroupPage(groupQuery, StarbucksStore.class);
    }

    @Override
    public GroupPage<StarbucksStore> findByLocationAndgroupByCity(final Point point, final Distance distance, final Integer pageNumber) {
        final Field cityField = new SimpleField("city");
        final Query query = new SimpleQuery(new Criteria("location").near(point, distance));
        final GroupOptions groupOptions = new GroupOptions().addGroupByField(cityField);
        query.setGroupOptions(groupOptions);
        query.setPageRequest(new PageRequest(pageNumber, 20));
        return solrTemplate.queryForGroupPage(query, StarbucksStore.class);
    }

    @Override
    public GroupPage<StarbucksStore> groupByCity() {
        final Field cityField = new SimpleField("city");
        final Query query = new SimpleQuery(new Criteria());
        final GroupOptions groupOptions = new GroupOptions().addGroupByField(cityField);
        query.setGroupOptions(groupOptions);
        query.setPageRequest(new PageRequest(0, 10));
        return solrTemplate.queryForGroupPage(query, StarbucksStore.class);
    }

    private String convertListToParameterString(final List<String> parameterList) {
        final String temp = parameterList.toString();
        final String temp1 = temp.replace("]", "");
        final String result = temp1.replace("[", "");
        return result;
    }


}
