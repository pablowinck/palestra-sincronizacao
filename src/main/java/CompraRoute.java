import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CompraRoute extends RouteBuilder {

    @ConfigProperty(name = "api.url")
    String apiUrl;

    @Override
    public void configure() {
        from("vertx:compra-realizada")
                .unmarshal().json(JsonLibrary.Jackson, Compra.class)
                .log("buscando compra com id: ${body.getId()}")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .toD(apiUrl + "/api/compras/${body.getId()}")
                .unmarshal().json(JsonLibrary.Jackson, Compra.class)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .enrich(apiUrl + "/api/reverse-geocoding?lat=${body.getLatitude()}&lon=${body.getLongitude()}", new CompraGeocondingAggregationStrategy())
                .log("inserindo: ${body}")
                .to("mongodb:mongoClient?database=compras&collection=compras&operation=insert")
                .log("enviando para o socket: ${body}")
                .to("vertx:compra-enriquecida");
    }

}

