package org.example;

import org.apache.camel.builder.RouteBuilder;

import org.example.beans.types.PostRequestType;
import org.example.beans.types.ResponseType;
import org.springframework.stereotype.Component;

import org.apache.camel.model.rest.RestBindingMode;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        rest("/api")
             .consumes("application/json")
             .produces("application/json")
            .get("/ei")
                .description("tessdfte")
                .route().routeId("router-id")
                .to("http://localhost:44325/api/pet??bridgeEndpoint=true")
                //.to("direct:ong")
                .endRest();

        from("direct:ong").routeId("ong")
                .to("sql:select * from ong").log("${body}");

/*        rest()
                .path("/api") // This makes the API available at http://host:port/$CONTEXT_ROOT/api

                .consumes("application/json")
                .produces("application/json")

                // HTTP: GET /api
                .get("/")
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:getBean") // This will invoke the Spring bean 'getBean'

                // HTTP: POST /api
                .post()
                .type(PostRequestType.class) // Setting the request type enables Camel to unmarshal the request to a Java object
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:postBean");*/

/*        rest()
                .consumes("application/json")
                .produces("application/json")
                .path("/go-sports").get("/").route().transform(simple("Validando rota"+"dsfsd"+"fgsfds")).endRest();

        from("rest:get:hello:/{me}")
                .transform().simple("Hi ${header.me}");*/
    }
}
