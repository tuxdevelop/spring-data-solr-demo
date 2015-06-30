package org.tuxdevelop.spring.data.solr.demo.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.tuxdevelop.spring.data.solr.demo.domain.Store;
import org.tuxdevelop.spring.data.solr.demo.repository.StoreRepository;

import java.util.*;

@Slf4j
@Component
public class SolrInitializer {

    private static final String LONGTITUDE = "Longitude";
    private static final String LATITUDE = "Latitude";
    private static final String ZIP_CODE = "Zip";
    private static final String NAME = "Name";
    private static final String CITY = "City";
    private static final String STREET_ADDRESS = "Street Address";
    private static final String FEATURES_PRODUCTS = "Features - Products";
    private static final String FEATURES_SERVICES = "Features - Service";

    private static final Double MAX_X = 180.0;
    private static final Double MIN_X = -180.0;

    private static final Double MAX_Y = 90.0;
    private static final Double MIN_Y = -90.0;

    @Autowired
    private StoreRepository storeRepository;

    public void importStarbucks() throws Exception {
        final List<List<Store>> stores = importStores();
        for (List<Store> storeList : stores) {
            try {
                storeRepository.save(storeList);
            } catch (UncategorizedSolrException e) {
                log.error(e.getMessage());
            }
        }

    }

    private List<List<Store>> importStores() throws Exception {
        final ClassPathResource resource = new ClassPathResource("starbucks.csv");
        final Scanner scanner = new Scanner(resource.getInputStream());
        final String line = scanner.nextLine();
        scanner.close();

        final FlatFileItemReader<Store> itemReader = new FlatFileItemReader<Store>();
        itemReader.setResource(resource);

        // DelimitedLineTokenizer defaults to comma as its delimiter
        final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(line.split(","));
        tokenizer.setStrict(false);

        final DefaultLineMapper<Store> lineMapper = new DefaultLineMapper<Store>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(StoreFieldSetMapper.INSTANCE);
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        itemReader.setLinesToSkip(1);
        itemReader.open(new ExecutionContext());
        final List<List<Store>> stores = new LinkedList<>();
        List<Store> storeList = new LinkedList<>();
        Store store;
        int id = 1;
        int currentCount = 1;
        do {
            store = itemReader.read();
            if (store != null) {
                store.setId(String.valueOf(id));
                storeList.add(store);
                id++;
                currentCount++;
            }
            if (currentCount == 1000) {
                stores.add(new LinkedList<>(storeList));
                currentCount = 1;
                storeList = new LinkedList<>();
            }
        } while (store != null);

        return stores;
    }

    private enum StoreFieldSetMapper implements FieldSetMapper<Store> {

        INSTANCE;

        @Override
        public Store mapFieldSet(FieldSet fields) throws BindException {
            final Double longtitudeRead = fields.readDouble(LONGTITUDE);
            final Double latitudeRead = fields.readDouble(LATITUDE);
            final Point location = getLocationPoint(longtitudeRead, latitudeRead);

            final String name = fields.readString(NAME);
            final String zipCode = fields.readString(ZIP_CODE);
            final String city = fields.readString(CITY);
            final String street = fields.readString(STREET_ADDRESS);
            final String productsString = fields.readString(FEATURES_PRODUCTS);
            final Collection<String> products = Arrays.asList(productsString.split(","));
            final String servicesString = fields.readString(FEATURES_SERVICES);
            final Collection<String> services = Arrays.asList(servicesString.split(","));
            return new Store(null, name, zipCode, city, street, products, services, location);
        }

        private Point getLocationPoint(final Double longtitudeRead, final Double latitudeRead) {

            final int longtitudeCompareMax = MAX_Y.compareTo(longtitudeRead);
            final int longtitudeCompareMin = MIN_Y.compareTo(longtitudeRead);

            final int latitudeCompareMax = MAX_X.compareTo(latitudeRead);
            final int latitudeCompareMin = MIN_X.compareTo(latitudeRead);

            final Double longtitude;
            final Double latitude;

            if (longtitudeCompareMax < 0) {
                longtitude = MAX_Y;
            } else if (longtitudeCompareMin > 0) {
                longtitude = MIN_Y;
            } else {
                longtitude = longtitudeRead;
            }

            if (latitudeCompareMax < 0) {
                latitude = MAX_X;
            } else if (latitudeCompareMin > 0) {
                latitude = MIN_X;
            } else {
                latitude = latitudeRead;
            }
            return new Point(latitude, longtitude);
        }
    }


}
