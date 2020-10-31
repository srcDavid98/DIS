package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cyclops.control.Trampoline;
import model.BankTeller;
import model.Customer;
import util.Encryption;
import util.Hashing;
import util.NetworkUtil;
import view.MainView;

import java.io.IOException;


public class TellerController {
    private NetworkUtil networkUtil;
    private MainView view;

    private static final String BASE = "http://localhost:8080/exam/banktellers/";

    public TellerController(NetworkUtil networkUtil){this.networkUtil = networkUtil;}


    public void setView(MainView view) { this.view = view;
    }

    public String validate(String username, String password) {
        try
        {
            String url = BASE + "login/";
            try {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username", username);
                jsonObject.addProperty("password", Hashing.hasWithSalt(password));

                String jsonInputString = new Gson().toJson(jsonObject);
                jsonInputString = Encryption.encryptDecrypt(jsonInputString);

                String response = networkUtil.httpRequest(url, "POST", jsonInputString);

                return response;

            } catch (IOException e) {
                //Det skal være "" fordi den retuner en string
                return "false";
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return "false";
    }

    public Trampoline<Void> createTeller (String name, String password) {
        String url = BASE;
        try {
            BankTeller bankTeller = new BankTeller();
            bankTeller.setTellerName(name);
            bankTeller.setPassword(password);
            String jsonInputString = new Gson().toJson(bankTeller);

            jsonInputString = Encryption.encryptDecrypt(jsonInputString);

            networkUtil.httpRequest(url, "POST",jsonInputString);

        } catch (Exception e) {
            return Trampoline.more(() -> view.ShowMessage("Error creating customer. Message: " + e.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("Customer created. "));
    }
}

