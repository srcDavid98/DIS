package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cyclops.control.Trampoline;
import model.Account;
import util.Encryption;
import util.NetworkUtil;
import view.MainView;

import java.io.IOException;
import java.util.Arrays;


public class AccountController {
    private NetworkUtil networkUtil;
    private MainView view;

    //Setter base til å være for accounts
    private static final String BASE = "http://localhost:8080/exam/accounts/";

    public AccountController(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }


/**
 * Method for all accounts
 *
 * */

    public Trampoline<Void> getAccountsById(int customer_id) {

        try
        {
            String url = BASE+(customer_id);
            String response = networkUtil.httpRequest(url, "GET");

            String output = new Gson().fromJson(response, String.class);

            output = Encryption.encryptDecrypt(output);
            Account[] accounts = new Gson().fromJson(output, Account[].class);


            return Trampoline.more(() -> view.ShowAccountsById(Arrays.asList(accounts)));
        }
        catch (IOException e)
        {
            return Trampoline.more(() -> view.ShowMessageAcc("Error getting Accounts from server.\nThis user might not have any accounts.\n" + e.getMessage()));
        }
    }

    /**
     * Gets one account for one customer.
     * @param customerId
     * @param accountid
     *
     *
     * */
    public Trampoline<Void> getAccountById(int customerId,int accountid){

        try
        {
            String url = BASE + (customerId) + "/" +(accountid);
            String response = networkUtil.httpRequest(url, "GET");

            String output = new Gson().fromJson(response, String.class);

            output = Encryption.encryptDecrypt(output);

            Account account = new Gson().fromJson(output, Account.class);

            return Trampoline.more(() -> view.ShowAccountById(account));
        }
         catch (IOException p) {
            return Trampoline.more(() -> view.ShowMessage("Error getting account from server. " + p.getMessage()));
         }
    }

    /**
     * Gets all accounts
     * no @Param
     *
     * */

    public Trampoline<Void> getAllAccounts(){
        String url = BASE;
        try {

            String response = networkUtil.httpRequest(url, "GET");
            String output = new Gson().fromJson(response, String.class);

            output = Encryption.encryptDecrypt(output);

            Account[] accounts = new Gson().fromJson(output, Account[].class);


            //Trampoline bruk i returnering i vår metode lar oss unngå StackOverFlow
            return Trampoline.more(() -> view.ShowAllAccounts(Arrays.asList(accounts)));
        }
        catch (IOException e) {
            return Trampoline.more(() -> view.ShowMessage("Error getting accounts from server. Message: " + e.getMessage()));
        }


        /**
         * CreateAccount method
         * @Param customer_id
         * @Param balance (start deposit)
         **/

    } public Trampoline<Void> createAccount(int customer_id,int balance){
        String url = BASE;

        try{



            Account newAccount = new Account();
            newAccount.setCustomer_id(customer_id);
            newAccount.setBalance(balance);

            String JsonInputString = new Gson().toJson(newAccount);

            JsonInputString = Encryption.encryptDecrypt(JsonInputString);

            networkUtil.httpRequest(url, "POST",JsonInputString);

        } catch (IOException pp){
            return Trampoline.more(() -> view.ShowMessage("Error creating account: " + pp.getMessage()));

        } return Trampoline.more(() -> view.ShowMessageAcc("Account was creat successfully.\nReturning to Account Menu"));
    }


    /**
     *
     *
     * */
    public Trampoline<Void> withdrawFromAccount(int id, int amount){

        //Base url for å tilgå informasjon
        String url = BASE+"withdraw";

        try{
            JsonObject object = new JsonObject();
            object.addProperty("account_id", id);     //Sender kontonummer
            object.addProperty("amount", amount);     //Sender mengden som skal trekkes

            String JsonInputString = new Gson().toJson(object);

            JsonInputString = Encryption.encryptDecrypt(JsonInputString);

            networkUtil.httpRequest(url,"PUT", JsonInputString);


        } catch (IOException e) {
            return Trampoline.more(() -> view.ShowMessageAcc("Error withdrawing from account " + e.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("Withdraw succesfull"));

    }

    public void setView(MainView view) {
        this.view = view;
    }


    /**
     *
     *
     * */
    public Trampoline<Void> depositToAccount(int id, int amount){
        //Base url for å tilgå informasjon
        String url = BASE+"deposit";

        try{

            JsonObject object = new JsonObject();
            object.addProperty("account_id", id);     //Sender kontonummer
            object.addProperty("amount", amount);     //Sender mengden som skal trekkes

            String JsonInputString = new Gson().toJson(object);

            JsonInputString = Encryption.encryptDecrypt(JsonInputString);

            networkUtil.httpRequest(url,"PUT", JsonInputString);


        } catch (IOException f) {
            return Trampoline.more(() -> view.ShowMessage("Error depositing from account" + f.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("Deposit succesfull"));

    }


    /**
     *
     *
     * */

    public Trampoline<Void> transfer (int fromAccount, int toAccount, int amount){
        String url = BASE +"transfer";
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("fromAccount", fromAccount);
            jsonObject.addProperty("toAccount", toAccount);
            jsonObject.addProperty("amount", amount);

            String input = new Gson().toJson(jsonObject);
            input = Encryption.encryptDecrypt(input);

            networkUtil.httpRequest(url, "PUT", input);

        }catch (IOException e){
            e.getMessage();
            return Trampoline.more(() -> view.ShowMessageAcc("error..." + e.getMessage()));
        }
        return Trampoline.more(() -> view.ShowMessage("trasfering the money"));
    }






}


