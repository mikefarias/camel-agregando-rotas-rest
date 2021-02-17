package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.example.beans.User;
import org.example.beans.Usuario;
import org.example.beans.types.PostRequestType;
import org.example.beans.types.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    CamelContext context = new DefaultCamelContext();

    @Override
    public void configure() {

        context.setStreamCaching(true);

/*        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/api")
             .consumes("application/json")
             .produces("application/json")
            .get("/ibge/localidades")
                .description("IBGE Localidades")
                .route().routeId("router-id")
                .marshal().json(JsonLibrary.Jackson)
                .aggregate(constant(true)).aggregationStrategy(AggregationStrategies.groupedBody())
                .completionSize(10)
                //.marshal().json(JsonLibrary.Jackson)
                //.to("https://servicodados.ibge.gov.br/api/v1/localidades/distritos/280030805?bridgeEndpoint=true").log("${body}")
                //.to("https://servicodados.ibge.gov.br/api/v1/localidades/distritos/280060505?bridgeEndpoint=true").log("${body}")
                .endRest();*/

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

        rest("/")
                //Endpoint que usa o Enrich EIP
                .get("autor")
                .route().routeId("autor")
                .to("direct:call-autor")
                .endRest()
                .get("usuario")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .route().routeId("id")
                //.setBody(constant("teste mike"))
                //.aggregate(constant(true))
                  //      .aggregationStrategy(AggregationStrategies.groupedBody())
                    //    .completionTimeout(300)
                //.enrich("http://localhost:3000/teste?bridgeEndpoint=true")
                //.to("direct:user").
                //unmarshal().json(JsonLibrary.Jackson, Usuario.class).log("${body}")
                .to("direct:usuario")
                .unmarshal().json(JsonLibrary.Jackson, User.class).log("${body}")
                .endRest()
                .get("user").to("direct:user");

                from("direct: usuario")
                        .routeId("all-usuarios")
                        .to("http://localhost:3000/usuario?bridgeEndpoint=true");

                from("direct: user").to("http://localhost:3000/user?bridgeEndpoint=true");
        ;
                //.endRest();

/*        rest().get("/teste3")
                .aggre(new GroupedExchangeAggregationStrategy()).parallelProcessing()
                .enrich("http://localhost:3000/teste?bridgeEndpoint=true")
                .enrich("http://localhost:3000/teste2?bridgeEndpoint=true")
                .end();*/
    }
}
