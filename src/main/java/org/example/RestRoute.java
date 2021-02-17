package org.example;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {

    private static final int OK_CODE = 200;
    private static final int APP_RESPONSE_CODE = 204;

    @Override
    public void configure() throws Exception{

        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

        rest("/integracao")
                .get("/usuario")
                .route().routeId("id-route-usuario")
                .to("direct:usuarios")
                .endRest();

        from("direct:usuarios")
                .routeId("call-rest-services")
                .to("direct:usuario")
                .choice()
                .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(OK_CODE))
                .enrich("direct:user", new JsonRestCallsAggregator())
                .otherwise()
                .setHeader(Exchange.HTTP_RESPONSE_CODE).constant(APP_RESPONSE_CODE);
        from("direct:usuario")
                .routeId("all-usuarios")
                .to("http://localhost:3000/usuario?bridgeEndpoint=true");

        from("direct:user").to("http://localhost:3000/user?bridgeEndpoint=true");

    }
}
