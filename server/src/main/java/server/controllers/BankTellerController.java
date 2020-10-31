package server.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.model.BankTeller;
import server.model.Customer;
import server.repository.BankTellerRepository;
import server.util.Encryption;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/banktellers")
public class BankTellerController {

    private BankTellerRepository bankTellerRepository;

    //Bruker this. for å refferere til BankTellerRepository
    public BankTellerController(){ this.bankTellerRepository = new BankTellerRepository(); }



/**
 * Let's bankteller login to the system from a Login String representing true or false
 * Object takes username and password as elements of the login request
 * */
    @POST
    @Path("login")
    public Response validate(String login) throws SQLException {

        login = Encryption.encryptDecrypt(login);

        JsonObject js = new Gson().fromJson(login, JsonObject.class);
        String username = new Gson().fromJson(js, JsonObject.class).get("username").getAsString();
        String password = new Gson().fromJson(js, JsonObject.class).get("password").getAsString();

        boolean banktellers = bankTellerRepository.authorizeUser(username, password);

        String out = new Gson().toJson(banktellers);
        System.out.println(out);

        return Response
                .status(200)
                .type("application/json")
                .entity(out)
                .build();
    }


    @POST
    public Response createBankTeller(String bankteller) throws SQLException {


        BankTeller bankTeller1 = new Gson().fromJson(bankteller, BankTeller.class);

        bankTellerRepository.createTeller(bankTeller1);


        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userCreated\":\"true\"}")
                .build();
    }
}
