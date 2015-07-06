package org.tuxdevelop.spring.data.solr.demo.api;


import org.tuxdevelop.spring.data.solr.demo.domain.Store;

import javax.ws.rs.*;
import java.util.Collection;
import java.util.List;

@Path("/stores")
public interface StoreService {

    String SEARCH = "/search";

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    Store add(final Store store);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") final String id);

    @GET
    @Path(SEARCH + "/byName")
    @Produces("application/json")
    Collection<Store> findByName(@QueryParam("name") final String name);

    @GET
    @Path(SEARCH + "/near")
    @Produces("application/json")
    Collection<Store> findNear(@QueryParam("longtitude") final Double longtitude,
                               @QueryParam("latitude") final Double latitude,
                               @QueryParam("distance") final Double distance,
                               @QueryParam("products") final List<String> products);

}
