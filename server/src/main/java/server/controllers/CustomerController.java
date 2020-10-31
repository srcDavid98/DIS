package server.controllers;

import com.google.gson.Gson;
import server.model.Account;
import server.model.Customer;
import server.repository.CustomerRepository;
import server.util.Encryption;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Contains endpoints for customer related operations.
 * Endpoints er sårbare mot cyberattacks og skal sikres ettersom.
 */


@Path("/customers")
public class CustomerController {

    private static ArrayList<Customer> cache = new ArrayList<>();
    private static ArrayList<Customer> cache2 = new ArrayList<>();

    private CustomerRepository customerRepository;

    public CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    /**
     * Gets a customer by id.
     *
     * @param id The id of the customer.
     * @return The response to the client.
     */
    @GET
    @Path("{id}")
    public synchronized Response getCustomerById(@PathParam("id") int id) {

        if (!cache2.isEmpty()) {
            //for-each loop som itererer gjennom arrylisten for Customers
            for (Customer c : cache2) {


                if (c.getCustomerId() == id) {

                    String output = new Gson().toJson(c);

                    output = Encryption.encryptDecrypt(output);

                    output = new Gson().toJson(output);
                    System.out.println("Printing from chache\n" + output);

                    return Response
                            .status(200)
                            .type("application/json")
                            .entity(output)
                            .build();
                }
            }
        }
        Customer customer = customerRepository.getCustomer(id);

        if (customer == null) {

            return Response
                    .status(400)
                    .type("application/json")
                    .entity("\"You have entered something invalid. Check if what you entered is valid\":\"failed\"}")
                    .build();
        }


        cache2.add(customer);
        String output = new Gson().toJson(customer);

        output = Encryption.encryptDecrypt(output);
        output = new Gson().toJson(output);

        System.out.println(output);

        return Response
                .status(200)
                .type("application/json")
                .entity(output)
                .build();
    }


    /**
     * Gets all customers from the database
     *
     * @return Response to the client.
     */


    @GET
    public synchronized Response getCustomers() {

        if (!cache.isEmpty()) {


                String output = new Gson().toJson(cache);

               // output = Encryption.encryptDecrypt(output);
               // output = new Gson().toJson(output);

               // System.out.println(" Printing from chache\n" + output);


            return Response
                        .status(200)
                        .type("application/json")
                        .entity(output)
                        .build();
        }

        ArrayList<Customer> customers = customerRepository.getCustomers();
        cache.addAll(customers);

        String output = new Gson().toJson(customers);

        //output = Encryption.encryptDecrypt(output);
        //output = new Gson().toJson(output);

        //System.out.println(output);


        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(output)
                .build();
    }

    /**
     * Creates a customer from a JSON encoded representation of a customer object.
     *
     * @param customer The JSON encoded representation of a customer object.
     * @return A response indicating if the creation went well.
     */
    @POST
    public synchronized Response createCustomer(String customer) {

        //customer = Encryption.encryptDecrypt(customer);

        Customer customer1 = new Gson().fromJson(customer, Customer.class);


        customerRepository.createCustomer(customer1);
        cache.clear();

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userCreated\":\"true\"}")
                .build();
    }

    /**
     * Updates a customer from a JSON String representing a full JSON object.
     * Matches the customer based on the primary key.
     *
     * @param customer A JSON String representing a Customer object.
     * @return A JSON Object with e
     */
    @PUT
    public synchronized Response updateCustomer(String customer) {

        //customer = Encryption.encryptDecrypt(customer);

        Customer customer1 = new Gson().fromJson(customer, Customer.class);

        Customer cc = customerRepository.updateCustomer(customer1);

        if(cc == null){

            return Response
                    .status(400)
                    .type("application/json")
                    .entity("\"You have entered something invalid. Check if the account number you entered is valid\":\"failed\"}")
                    .build();
        }

        // customer = Encryption.encryptDecrypt(customer);

        //Clearer cache
        cache.clear();


        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userUpdated\":\"true\"}")
                .build();
    }
}





