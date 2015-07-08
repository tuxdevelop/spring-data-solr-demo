package org.tuxdevelop.spring.data.solr.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductsViewModel {

    private List<String> products;
    private List<String> selected;

}
