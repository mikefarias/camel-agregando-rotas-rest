package org.example;

import java.util.List;
import java.util.Objects;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.util.json.JsonObject;
import org.example.beans.User;
import org.example.beans.Usuario;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

public class JsonRestCallsAggregator implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Usuario user = newExchange.getIn().getBody(Usuario.class);
        User usuario = oldExchange.getIn().getBody(User.class);

        List<Usuario> usuarios = null;

        //usuarios.add(usuario);
        JsonParser parser = JsonParserFactory.getJsonParser();
        JsonObject json = new JsonObject();
        json.put("usuario", parser.parseMap(usuario.toString()));
        json.put("user", parser.parseMap(user.toString()));

        newExchange.getIn().setBody(json.toJson());
        return newExchange;
    }
}