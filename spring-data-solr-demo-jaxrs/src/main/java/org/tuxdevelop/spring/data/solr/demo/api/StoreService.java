package org.tuxdevelop.spring.data.solr.demo.api;


import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Collection;

@Path("/stores")
public interface StoreService {

    static final String SEARCH = "/search";

    @GET
    @Path(SEARCH + "/byName")
    @Produces("application/json")
    Collection<Store> findByName(@QueryParam("name") final String name);


}
