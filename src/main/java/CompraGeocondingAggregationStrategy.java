
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CompraGeocondingAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Compra compra = oldExchange.getIn().getBody(Compra.class);
        String geocodingResponse = newExchange.getIn().getBody(String.class);
        compra.setEndereco(geocodingResponse);
        return oldExchange;
    }

}
