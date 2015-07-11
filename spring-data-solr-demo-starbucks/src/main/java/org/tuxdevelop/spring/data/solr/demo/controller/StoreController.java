package org.tuxdevelop.spring.data.solr.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.model.LocationViewModel;
import org.tuxdevelop.spring.data.solr.demo.model.NameViewModel;
import org.tuxdevelop.spring.data.solr.demo.model.ProductsViewModel;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public void initProductsSearch(final Model model) {
        model.addAttribute("products", getProducts());
        model.addAttribute("searchResult", new LinkedList<Store>());
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public void initLocationSearch(final Model model) {
        final ProductsViewModel productsViewModel = new ProductsViewModel();
        final LocationViewModel locationViewModel = new LocationViewModel();
        locationViewModel.setProductsViewModel(productsViewModel);
        model.addAttribute("products", getProducts());
        model.addAttribute("location", locationViewModel);
        model.addAttribute("searchResult", new LinkedList<Store>());
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public void initNameSearch(final Model model) {
        model.addAttribute("name", new NameViewModel());
        model.addAttribute("searchResult", new LinkedList<Store>());
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public void searchByProducts(@ModelAttribute("products") final ProductsViewModel selectedProducts,
                                 final Model model) {
        final Collection<String> selectedProductsRequest;
        if (selectedProducts.getSelected() != null) {
            selectedProductsRequest = selectedProducts.getSelected();
        } else {
            selectedProductsRequest = getProducts().getProducts();
        }
        final Collection<Store> stores = storeRepository.findByProductsIn(selectedProductsRequest);
        model.addAttribute("searchResult", stores);
        model.addAttribute("products", getProducts());
    }

    @RequestMapping(value = "/locations", method = RequestMethod.POST)
    public void searchByLocation(@ModelAttribute("products") final LocationViewModel locationViewModel,
                                 final Model model) {
        final Double longtitude = locationViewModel.getLongtitude() != null ? locationViewModel.getLongtitude() : -90;
        final Double latitude = locationViewModel.getLatitude() != null ? locationViewModel.getLatitude() : -90;
        final Double distanceValue = locationViewModel.getDistance() != null ? locationViewModel.getDistance() : 0D;
        final Point point = new Point(latitude, longtitude);
        final Distance distance = new Distance(distanceValue);
        final Collection<String> selectedProducts;
        if (locationViewModel.getProductsViewModel().getSelected() != null) {
            selectedProducts = locationViewModel.getProductsViewModel().getSelected();
        } else {
            selectedProducts = getProducts().getProducts();
        }
        final Collection<Store> stores = storeRepository.findByProductsInAndLocationNear(selectedProducts, point, distance);
        model.addAttribute("location", locationViewModel);
        model.addAttribute("searchResult", stores);
        model.addAttribute("products", getProducts());
    }

    @RequestMapping(value = "/names", method = RequestMethod.POST)
    public void searchByName(@ModelAttribute("name") final NameViewModel nameViewModel, final Model model) {
        final String name = nameViewModel.getName();
        final String nameToFind = name != null ? name : "";
        final Collection<Store> stores = storeRepository.findByName(nameToFind);
        model.addAttribute("name", nameViewModel);
        model.addAttribute("searchResult", stores);
    }

    private ProductsViewModel getProducts() {
        final FacetPage<Store> response = storeRepository.findByFacetOnProducts(new PageRequest(0, 100));
        final Page<FacetFieldEntry> facetResultPage = response.getFacetResultPage("products");
        final List<FacetFieldEntry> content = facetResultPage.getContent();
        final ProductsViewModel model = new ProductsViewModel();
        model.setProducts(new LinkedList<String>());
        model.setSelected(new LinkedList<String>());
        for (final FacetFieldEntry facetFieldEntry : content) {
            model.getProducts().add(facetFieldEntry.getValue());
        }
        return model;
    }

}
