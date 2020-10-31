package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cyclops.control.Trampoline;
import model.Customer;
import util.Encryption;
import util.NetworkUtil;
import view.MainView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The controller for customer related operations.
 */
public class CustomerController {
    private NetworkUtil networkUtil;
    private MainView view;

    private static final String BASE = "http://localhost:8080/exam/";

    public CustomerController(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    /**
     * Gets all customers from server.
     * Decrypts the String that is returned from server to make it readable
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> getAllCustomers() {

        try
        {
            String url = BASE+"customers";

            String response = networkUtil.httpRequest(url, "GET");
            String output = new Gson().fromJson(response, String.class);

            output = Encryption.encryptDecrypt(output);

            Customer[] customers = new Gson().fromJson(output, Customer[].class);


            return Trampoline.more(() -> view.ShowAllCustomers(Arrays.asList(customers)));
        }
        catch (IOException e)
        {
            return Trampoline.more(()-> view.ShowMessage("Error getting customers from server. Message: " + e.getMessage()));
        }

    }

    /**
     * Gets a customer.
     * @param userId The userid of the customer.
     * Decrypts the response from server to make it readable
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  getCustomer(int userId) {
        try
        {
            String url = BASE+"customers/" + (userId);
            String response = networkUtil.httpRequest(url, "GET");
            String output = new Gson().fromJson(response, String.class);

            output = Encryption.encryptDecrypt(output);

            Customer customer = new Gson().fromJson(output, Customer.class);

            return Trampoline.more(() -> view.ShowCustomer(customer));
        }
        catch (IOException e)
        {
            return Trampoline.more(() -> view.ShowMessage("Error getting customer from server. Message: " + e.getMessage()));
        }

    }

    /**
     * Creates a customer on the server.
     * @param name The name of the customer.
     * Decrypts the response from Server to make it readable
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  createCustomer (String name) {
        String url = BASE+"customers";
        try {
            Customer c = new Customer();
            c.setName(name);

            String jsonInputString = new Gson().toJson(c);

            jsonInputString = Encryption.encryptDecrypt(jsonInputString);

            networkUtil.httpRequest(url, "POST",jsonInputString);

        } catch (Exception e) {
            return Trampoline.more(() -> view.ShowMessage("Error creating customer. Message: " + e.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("Customer created. "));
    }

    /**
     * Updates a customer on the server.
     * @param id The customer id .
     * @param name The customer name.
     * Decrypts the response from Server to make it readable
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  updateCustomer (int id, String name) {

        String url = BASE+"customers";

        try {

            Customer c = new Customer();
            c.setName(name);
            c.setCustomerId(id);
            String jsonInputString = new Gson().toJson(c);

            jsonInputString = Encryption.encryptDecrypt(jsonInputString);

            networkUtil.httpRequest(url, "PUT",jsonInputString);

        } catch (IOException e) {
            return Trampoline.more(() -> view.ShowMessage("Error updating customer on server. Message: " + e.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("Customer was updated successfully. Returning to the Main menu"));
    }

    public void setView(MainView view) {
        this.view = view;
    }
}
