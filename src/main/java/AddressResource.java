import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.atomic.AtomicInteger;

@Path("/api/reverse-geocoding")
public class AddressResource {

    private static final String[] ADDRESSES = {
            "Rua A, 123, Cidade X",
            "Avenida B, 456, Cidade Y",
            "Travessa C, 789, Cidade Z"
    };

    private final AtomicInteger counter = new AtomicInteger();

    @GET
    @Produces("text/plain")
    public Uni<Response> reverseGeocoding(@QueryParam("lat") String lat, @QueryParam("lon") String lon) {
        Log.info("Reverse geocoding for lat: " + lat + " and lon: " + lon);
        int index = counter.getAndIncrement() % ADDRESSES.length;
        String address = ADDRESSES[index];
        Log.info("Address found: " + address);
        return Uni.createFrom().item(Response.ok(address).build());
    }

}
