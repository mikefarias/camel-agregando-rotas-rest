package org.example;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestAggregateRoute extends RouteBuilder
{
    private static final int OK_CODE = 200;
    private static final int APP_RESPONSE_CODE = 204;

    @Override
    public void configure(){

        from("direct:usuarios")
                .routeId("call-rest-services")
                .to("direct:usuario")
                .choice()
                .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(OK_CODE))
                //.process(new GetAuthorIdProcessor())
                .enrich("direct:user", new JsonRestCallsAggregator())
                .otherwise()
                .setHeader(Exchange.HTTP_RESPONSE_CODE).constant(APP_RESPONSE_CODE);
    }
}
