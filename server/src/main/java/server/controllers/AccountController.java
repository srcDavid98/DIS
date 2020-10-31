package server.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.model.Account;
import server.repository.AccountRepository;
import server.util.Encryption;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Contains the endpoints for account operations.
 */

@Path("/accounts")
public class AccountController {

/**
 * Initialises our caches
 * */
    private static ArrayList<Account> cache = new ArrayList<>();
    private static ArrayList<Account> cache2 = new ArrayList<>();
    private static ArrayList<Account> cache3 = new ArrayList<>();


    private AccountRepository accountRepository;

    public AccountController() {

        this.accountRepository = new AccountRepository();
    }

    /**
     * Gets a customers accounts by id.
     *
     * @param customer_id The id for the customer.
     * @return The response to the client
     * Customers should have the possibility to have several accounts, therefore we use an ArrayList.
     * Double JSON conversion because of Encryption bug
     */

    @GET
    @Path("{customer_id}")
    public synchronized Response getAccountsById(@PathParam("customer_id") int customer_id) {

        if(!cache2.isEmpty()){

            for (Account a: cache2){

                if (a.getCustomer_id() == customer_id){

                    ArrayList<Account>  accounts = accountRepository.getAccountsById(customer_id);

                      String output = new Gson().toJson(accounts);

                        output = Encryption.encryptDecrypt(output);

                           output = new Gson().toJson(output);

                    System.out.println("Printed from cache\n" + output);

                    return Response
                            .status(200)
                            .type("application/json")
                            .entity(output)
                            .build();
                }

            }

        }


        //Checks if Arraylist is empty, returns error if true.
        ArrayList<Account> accounts = accountRepository.getAccountsById(customer_id);
        if (accounts.isEmpty()) {

            return Response
                    .status(404)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity("This customer either doesn't exist or has no accounts in our system")
                    .build();
        }
        cache2.addAll(accounts);

        String output = new Gson().toJson(accounts);

            output = Encryption.encryptDecrypt(output);

                output = new Gson().toJson(output);

                    System.out.println(output);

         return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(output)
                .build();
    }


    /**
     * Gets one account for one customer.
     *
     * @param customer_id The id of the customer.
     * @param account_id  The id of the account
     *
     * Prints from cache if all accounts have been requested before
     * Double JSON conversion because of Encryption bug
     * @return the response from cache if all accounts have been requested before
     */
    @GET
    @Path("{customer_id}/{account_id}")
    public synchronized Response getAccountById(@PathParam("customer_id") int customer_id, @PathParam("account_id") int account_id) {

        if (!cache3.isEmpty()) {

            //For-each loop that iterates cache ArrayList
            for (Account a : cache3) {

                //Checks if account requested matches an account in the cache
                if (a.getAccountId() == account_id && a.getCustomer_id() == customer_id) {

                    String output = new Gson().toJson(a);

                       output = Encryption.encryptDecrypt(output);

                         output = new Gson().toJson(output);

                          System.out.println("Printed from cache\n" + output);

                    return Response
                            .status(200)
                            .type("application/json")
                            .entity(output)
                            .build();
                }
            }
        }

        Account account = accountRepository.getAccountById(customer_id, account_id);

        if (account == null) {

            return Response
                    .status(404)
                    .type("application/json")
                    .entity("\"You have entered something invalid. Check if the account number you entered is valid\":\"failed\"}")
                    .build();
        }

        cache3.add(account);

          String output = new Gson().toJson(account);

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
     * Gets all accounts in the system.
     * returns from cache if accounts have been requested before
     * returns and adds to cache before returning response if not
     *
     * Double JSON conversion because of Encryption bug
     *
     * @return The response to the client
     */
    @GET
    public synchronized Response getAccounts() throws SQLException {

        if (!cache.isEmpty()) {

            String output = new Gson().toJson(cache);

             output = Encryption.encryptDecrypt(output);

                output = new Gson().toJson(output);

            System.out.println("Printing from cache\n" + output);

            return Response
                    .status(200)
                    .type("application/json")
                    .entity(output)
                    .build();
        }


        ArrayList<Account> accounts = accountRepository.getAccounts();

           cache.addAll(accounts);

               String output = new Gson().toJson(accounts);

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
     * Creates a customer account
     * clears cache
     *
     * @param account creates an account object
     * @return response whether or not creation was succesfull
     */
    @POST
    public synchronized Response createAccount(String account) throws SQLException {

        account = Encryption.encryptDecrypt(account);

           Account account1 = new Gson().fromJson(account, Account.class);

               account1 = accountRepository.createAccount(account1);

                  if (account1 == null) {

            return Response
                    .status(400)
                    .type("application/json")
                    .entity("{\"AccountCreated\":\"false\"}")
                    .build();

    }
        cache.clear();
        cache2.clear();
        cache3.clear();

            return Response
                    .status(200)
                    .type("application/json")
                    .entity("{\"AccountCreated\":\"true\"}")
                    .build();
}


    /**
     * Let's a customer withdraw from an account
     * clears cache because an account is updated
     *
     * @param account
     * @return response whether or not creation withdraw was succesfull
     */
    @PUT
    @Path("withdraw")
    public synchronized Response withdrawFromAccount(String account) throws SQLException {

        account = Encryption.encryptDecrypt(account);

        JsonObject js = new Gson().fromJson(account, JsonObject.class);


        int amount = js.get("amount").getAsInt();
        int account_id = js.get("account_id").getAsInt();


        Account acc = accountRepository.withdrawFromAccount(amount, account_id);

        if (acc == null){

            return Response
                    .status(400)
                    .type("application/json")
                    .entity("{\"amountWithdrawed\":\"false\"}")
                    .build();
        }


        cache.clear();
        cache2.clear();
        cache3.clear();

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"amountwithdrawed\":\"true\"}")
                .build();
    }

    /**
     * Let's a customer deposit to an account
     * @param account
     * @return response whether or not creation was succesfull
     *
     * clears caches if deposit is succesufll
     */

    @PUT
    @Path("deposit")
    public synchronized Response DepositToAccount(String account) {

        account = Encryption.encryptDecrypt(account);

        JsonObject js = new Gson().fromJson(account, JsonObject.class);

        int amount = js.get("amount").getAsInt();
        int account_id = js.get("account_id").getAsInt();


        Account acc = accountRepository.DepositToAccount(amount, account_id);

        if (acc == null){

            return Response
                .status(400)
                    .type("application/json")
                    .entity("{\"amountDeposited\":\"true\"}")
                    .build();

        }


        cache.clear();
        cache2.clear();
        cache3.clear();

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"amountDeposited\":\"true\"}")
                .build();


    }

    /**
     * Makes it possible to transfer between accounts
     * clears cache because an account is updated
     *
     * @param transfer
     * @return response whether or not transfer was succesfull
     */
    @PUT
    @Path("transfer")
    public synchronized Response transfer(String transfer) throws SQLException {

        transfer = Encryption.encryptDecrypt(transfer);

        int fromAccount_id = new Gson().fromJson(transfer, JsonObject.class).get("fromAccount").getAsInt();
        int toAccount_id = new Gson().fromJson(transfer, JsonObject.class).get("toAccount").getAsInt();
        int amount = new Gson().fromJson(transfer, JsonObject.class).get("amount").getAsInt();

       Account accounts = accountRepository.transfer(fromAccount_id, toAccount_id, amount);
       if(accounts == null){

           return Response
                   .status(400)
                   .type(MediaType.APPLICATION_JSON)
                   .entity("{\"Transfer\":\"true\"}")
                   .build();
       }

        //Renser cache fordi det har skjedd en oppdatering av enkelte kontoer og cachen er derfor ikke "up to date"
        cache.clear();
        cache2.clear();
        cache3.clear();

        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"Transfer\":\"true\"}")
                .build();
    }

    @GET
    @Path("interest")
    public synchronized Response addInterest() throws SQLException {

        accountRepository.addInterest();
        cache.clear();
        cache2.clear();
        cache3.clear();


        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"Interest\":\"true\"}")
                .build();

    }

}

