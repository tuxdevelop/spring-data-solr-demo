package org.tuxdevelop.spring.data.solr.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;
import org.tuxdevelop.spring.data.solr.demo.model.CityGroupModel;
import org.tuxdevelop.spring.data.solr.demo.model.LocationViewModel;
import org.tuxdevelop.spring.data.solr.demo.model.NameViewModel;
import org.tuxdevelop.spring.data.solr.demo.model.ProductsViewModel;
import org.tuxdevelop.spring.data.solr.demo.repository.StarbucksStoreRepository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
public class StoreController {

    @Autowired
    private StarbucksStoreRepository starbucksStoreRepository;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public void initProductsSearch(final Model model) {
        model.addAttribute("products", getProducts());
        model.addAttribute("searchResult", new LinkedList<StarbucksStore>());
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public void initLocationSearch(final Model model) {
        final ProductsViewModel productsViewModel = new ProductsViewModel();
        final LocationViewModel locationViewModel = new LocationViewModel();
        locationViewModel.setProductsViewModel(productsViewModel);
        model.addAttribute("products", getProducts());
        model.addAttribute("location", locationViewModel);
        model.addAttribute("searchResult", new LinkedList<StarbucksStore>());
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public void initNameSearch(final Model model) {
        model.addAttribute("name", new NameViewModel());
        model.addAttribute("searchResult", new LinkedList<StarbucksStore>());
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public void initGroupByCities(final Model model) {
        GroupPage<StarbucksStore> response = starbucksStoreRepository.groupByCity();
        Page<GroupEntry<StarbucksStore>> groupPage = response.getGroupResult("city").getGroupEntries();
        final CityGroupModel cityGroupModel = new CityGroupModel();
        cityGroupModel.setCurrentPage(0);
        cityGroupModel.setGroupEntries(groupPage);
        final LocationViewModel locationViewModel = new LocationViewModel();
        model.addAttribute("location", locationViewModel);
        model.addAttribute("cityGroup", cityGroupModel);
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
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByProductsIn(selectedProductsRequest);
        model.addAttribute("searchResult", starbucksStores);
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
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByProductsInAndLocationNear(selectedProducts, point, distance);
        model.addAttribute("location", locationViewModel);
        model.addAttribute("searchResult", starbucksStores);
        model.addAttribute("products", getProducts());
    }

    @RequestMapping(value = "/names", method = RequestMethod.POST)
    public void searchByName(@ModelAttribute("name") final NameViewModel nameViewModel, final Model model) {
        final String name = nameViewModel.getName();
        final String nameToFind = name != null ? name : "";
        final Collection<StarbucksStore> starbucksStores = starbucksStoreRepository.findByName(nameToFind);
        model.addAttribute("name", nameViewModel);
        model.addAttribute("searchResult", starbucksStores);
    }

    @RequestMapping(value = "/cities", method = RequestMethod.POST)
    public void groupByCities(@ModelAttribute("location") final LocationViewModel locationViewModel, final Model
            model) {
        final Double longtitude = locationViewModel.getLongtitude() != null ? locationViewModel.getLongtitude() : -90;
        final Double latitude = locationViewModel.getLatitude() != null ? locationViewModel.getLatitude() : -90;
        final Double distanceValue = locationViewModel.getDistance() != null ? locationViewModel.getDistance() : 0D;
        final Point point = new Point(latitude, longtitude);
        final Distance distance = new Distance(distanceValue);
        GroupPage<StarbucksStore> response = starbucksStoreRepository.findByLocationAndgroupByCity(point, distance, 0);
        Page<GroupEntry<StarbucksStore>> groupPage = response.getGroupResult("city").getGroupEntries();
        final CityGroupModel cityGroupModel = new CityGroupModel();
        cityGroupModel.setCurrentPage(0);
        cityGroupModel.setGroupEntries(groupPage);
        model.addAttribute("location", locationViewModel);
        model.addAttribute("cityGroup", cityGroupModel);
    }

    @RequestMapping(value = "/citiesNext", method = RequestMethod.POST)
    public String getCitiesNext(@ModelAttribute("location") final LocationViewModel locationViewModel, @RequestParam
            (value = "page") final Integer currentPage, final Model model) {
        final Double longtitude = locationViewModel.getLongtitude() != null ? locationViewModel.getLongtitude() : -90;
        final Double latitude = locationViewModel.getLatitude() != null ? locationViewModel.getLatitude() : -90;
        final Double distanceValue = locationViewModel.getDistance() != null ? locationViewModel.getDistance() : 0D;
        final Point point = new Point(latitude, longtitude);
        final Distance distance = new Distance(distanceValue);
        final CityGroupModel cityGroupModel = new CityGroupModel();
        GroupPage<StarbucksStore> response = starbucksStoreRepository.findByLocationAndgroupByCity(point, distance, currentPage);
        Page<GroupEntry<StarbucksStore>> groupPage = response.getGroupResult("city").getGroupEntries();
        cityGroupModel.setGroupEntries(groupPage);
        cityGroupModel.setCurrentPage(currentPage);
        model.addAttribute("location", locationViewModel);
        model.addAttribute("cityGroup", cityGroupModel);
        return "cities";
    }

    @RequestMapping(value = "/citiesPrev", method = RequestMethod.POST)
    public String getCitiesPrev(@ModelAttribute("location") final LocationViewModel locationViewModel, @RequestParam
            (value = "page") final Integer currentPage, final Model model) {
        final Double longtitude = locationViewModel.getLongtitude() != null ? locationViewModel.getLongtitude() : -90;
        final Double latitude = locationViewModel.getLatitude() != null ? locationViewModel.getLatitude() : -90;
        final Double distanceValue = locationViewModel.getDistance() != null ? locationViewModel.getDistance() : 0D;
        final Point point = new Point(latitude, longtitude);
        final Distance distance = new Distance(distanceValue);
        final CityGroupModel cityGroupModel = new CityGroupModel();
        GroupPage<StarbucksStore> response = starbucksStoreRepository.findByLocationAndgroupByCity(point, distance, currentPage);
        Page<GroupEntry<StarbucksStore>> groupPage = response.getGroupResult("city").getGroupEntries();
        cityGroupModel.setGroupEntries(groupPage);
        cityGroupModel.setCurrentPage(currentPage);
        model.addAttribute("location", locationViewModel);
        model.addAttribute("cityGroup", cityGroupModel);
        return "cities";
    }

    private ProductsViewModel getProducts() {
        final FacetPage<StarbucksStore> response = starbucksStoreRepository.findByFacetOnProducts(new PageRequest(0, 100));
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
