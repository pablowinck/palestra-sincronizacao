import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bson.Document;

import java.util.List;

@Path("/api/compras")
public class CompraResource {

    @Inject
    ReactiveMongoClient mongoClient;

    @Inject
    EventBus eventBus;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Document>> listAll() {
        ReactiveMongoCollection<Document> collection = mongoClient.getDatabase("compras").getCollection("compras");
        return collection.find().collect().asList();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Compra> create(Compra compra) {
        return Panache.withTransaction(compra::persist)
                .replaceWith(compra)
                .onItem().invoke(compra1 -> {
                    eventBus.send("compra-realizada", compra1.toJson());
                })
                .onItem().transform(compra1 -> compra1);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Compra> findById(String id) {
        return Compra.findById(id);
    }

}