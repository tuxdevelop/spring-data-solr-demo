package org.tuxdevelop.spring.data.solr.demo.model;

import lombok.Data;

@Data
public class LocationViewModel {

    private Double latitude;
    private Double longtitude;
    private Double distance;
    private ProductsViewModel productsViewModel;

}
