package org.tuxdevelop.spring.data.solr.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.model.ProductsViewModel;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;


    @RequestMapping(method = RequestMethod.GET)
    public void init(final Model model) {
        model.addAttribute("products", getProducts());
        model.addAttribute("searchResult", new LinkedList<Store>());
    }

    @RequestMapping(method = RequestMethod.POST)
    public void searchByProducts(@ModelAttribute("products") final ProductsViewModel selectedProducts,
                                 final Model model) {
        final Collection<Store> stores = storeRepository.findByProductsIn(selectedProducts.getSelected());
        model.addAttribute("searchResult", stores);
        model.addAttribute("products", getProducts());
    }


    private ProductsViewModel getProducts() {
        final FacetPage<Store> response = storeRepository.findByFacetOnProducts(new
                PageRequest(0, 100));
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("products");
        final List<FacetFieldEntry> content = facetResultPage.getContent();
        final ProductsViewModel model = new ProductsViewModel();
        model.setProducts(new LinkedList<>());
        model.setSelected(new LinkedList<>());
        for (final FacetFieldEntry facetFieldEntry : content) {
            model.getProducts().add(facetFieldEntry.getValue());
        }
        return model;
    }

}
