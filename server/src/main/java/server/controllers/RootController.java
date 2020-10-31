package server.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RootController {

    @GET
    public Response defaultGetMethod(){
        return Response.status(200).type("text/html").entity("Welcome to DIS exam 2019. Good luck!").build();
    }

}