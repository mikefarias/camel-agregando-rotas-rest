package org.example.beans;


import org.example.beans.types.PostRequestType;
import org.example.beans.types.ResponseType;
import org.springframework.stereotype.Component;

@Component
public class PostBean {

    public ResponseType response(PostRequestType input) {
        // We create a new object of the ResponseType
        // Camel will be able to serialise this automatically to JSON
        return new ResponseType("Thanks for your post, " + input.getName() + "!");
    }
}
