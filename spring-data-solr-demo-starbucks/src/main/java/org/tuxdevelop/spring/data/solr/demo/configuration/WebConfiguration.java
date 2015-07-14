package org.tuxdevelop.spring.data.solr.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration {

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(final ViewControllerRegistry viewControllerRegistry) {
                viewControllerRegistry.addViewController("/").setViewName("index");
                viewControllerRegistry.addViewController("/index").setViewName("index");
                viewControllerRegistry.addViewController("/products").setViewName("products");
                viewControllerRegistry.addViewController("/locations").setViewName("locations");
                viewControllerRegistry.addViewController("/names").setViewName("names");
                viewControllerRegistry.addViewController("/cities").setViewName("cities");
                viewControllerRegistry.addViewController("/citiesNext").setViewName("cities");
                viewControllerRegistry.addViewController("/citiesPrev").setViewName("cities");
            }
        };
    }
}
