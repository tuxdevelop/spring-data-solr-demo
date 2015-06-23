package org.tuxdevelop.spring.data.solr.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Collection;

@Data
@AllArgsConstructor
@SolrDocument
public class Store {

    @Id
    @Field("id")
    private String id;
    @Field("name")
    private String name;
    @Field("zipCode")
    private String zipCode;
    @Field("city")
    private String city;
    @Field("street")
    private String street;
    @Field("products")
    private Collection<String> products;
    @Field("services")
    private Collection<String> services;
    @Field("location")
    private Point location;

}