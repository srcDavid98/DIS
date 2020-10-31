package view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import controller.AccountController;
import controller.CustomerController;
import controller.TellerController;
import cyclops.control.Trampoline;
import model.Account;
import model.Customer;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * The main view of the application showing main menu etc.
 */
public class MainView extends View {

    private CustomerController customerController;
    private AccountController accountController;
    private TellerController tellerController;

    public MainView(CustomerController controller, AccountController controller1, TellerController controller2) {
        this.customerController = controller;
        this.accountController = controller1;
        this.tellerController = controller2;

    }



/**
 * Login meny som tar username & password som parameter i validering
 * */
      public Trampoline<Void> LoginMenu() {

          boolean verify = false;
          while (verify == false) {
              System.out.println("Please log in with your username & password\n");
              try {
                  System.out.println("Enter your username");
                  String username = br.readLine();
                  System.out.println("Enter your password");
                  String password = br.readLine();

                  String d = tellerController.validate(username, password);
                  if (d.equals("true")) {
                      verify = true;
                      System.out.println("Welcome " + username);
                      return Trampoline.more(this::MainMenu);
                  } else {
                      System.out.println("Username and Password are incorrect! Please try again");
                      return Trampoline.more(this::LoginMenu);
                  }
              } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("Error");
              }
          }
          return Trampoline.more(this::MainMenu);
      }







    //Trampoline tillatter oss å utføre metodekall uten å få StackOverFlow
    public Trampoline<Void> returnToMainMenu() {
        try {
            System.out.println("Press any key to return to main menu.");
            getNextStringValue();
            return Trampoline.more(this::MainMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Trampoline.more(this::MainMenu);
    }

    /**
     * Shows a customer.
     * @param customer The customer to show.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> ShowCustomer(Customer customer)
    {
        System.out.println(customer);
        return Trampoline.more(this::returnToMainMenu);
    }

    //Metode som viser alle kontoer for én bruker
    public Trampoline<Void> ShowAccountsById(List<Account> AccountsForUser){
        for (Account a: AccountsForUser){
            //String output = new Gson().toJson(a);
            System.out.println(a);
        }
        return Trampoline.more(this::AccountMenu);
    }
    /**
     * Shows all customers.
     * @param customers The customers to be shown.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> ShowAllCustomers(List<Customer> customers)
    {
        for(Customer c : customers) {
            System.out.println(c);
        }
        return Trampoline.more(this::returnToMainMenu);
    }

        //Metod showing all accounts
        //Prettyprinting is a fun twist to the system.out.print!
        public Trampoline<Void> ShowAllAccounts(List<Account> accounts){

            for (Account a: accounts){
                String output = new Gson().toJson(a);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(output);

                String prettyJsonString = gson.toJson(je);
                System.out.println(prettyJsonString);


            }
            return  Trampoline.more(this::AccountMenu);
    }

    //Viser én konto for én bruk
    public Trampoline<Void> ShowAccountById(Account account){

            String output = new Gson().toJson(account);
            System.out.println(output);
           return Trampoline.more(this::AccountMenu);
    }

    /**
     * Shows a single message, before returning to main menu.
     * @param message The message to show.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     **/
    public Trampoline<Void> ShowMessage(String message)
    {
        System.out.println(message);
        return Trampoline.more(this::returnToMainMenu);
    }

    /**
     * Shows a single message, before returning to Account menu.
     * @param message The message to show.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> ShowMessageAcc(String message)
    {
        System.out.println(message);
        return Trampoline.more(this::AccountMenu);
    }
    /**
     * The main menu.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */

    public Trampoline<Void> MainMenu()
    {
        while (true)
        {
            clearConsole();
            System.out.println("Main menu:");
            System.out.println("1. Get all customers");
            System.out.println("2. Get customer");
            System.out.println("3. Create a new customer");
            System.out.println("4. Update a customer's information");
            System.out.println("5. Go to Account menu");


            System.out.print("Please select an option from 1-n\r\n");

            try {
                switch (getNextPosIntValue()) {
                    case 1:
                        return Trampoline.more(() -> customerController.getAllCustomers());
                    case 2:
                        try {
                            System.out.println("Enter user id");
                            int id = Integer.parseInt(br.readLine());
                            return Trampoline.more(() -> customerController.getCustomer(id));
                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                            return Trampoline.more(this::MainMenu);
                        }
                    case 3:
                        System.out.println("Enter user name");
                        String name = br.readLine();
                        return Trampoline.more(() -> customerController.createCustomer(name));
                    case 4:
                        try {
                            System.out.println("Enter your user id");
                            int id = Integer.parseInt(br.readLine());
                            System.out.println("Please enter new username");
                            String username = br.readLine();
                            return Trampoline.more(() -> customerController.updateCustomer(id, username));
                        } catch (Exception p){
                            System.out.println("Invalid input, returning to the Main menu");
                            return Trampoline.more(this::MainMenu);
                        }
                    case 5:
                        return Trampoline.more(this::AccountMenu);
                        default:
                        System.out.println("invalid selection. Returning to main menu\n");
                }
            } catch (IOException ioe) {
                System.out.println("IO error trying to read your input!");
            }

        }
    }

    /**
     * The Account Menu.
     *
     * */
    public Trampoline<Void> AccountMenu()
         {
             while (true)

         {  clearConsole();
             System.out.println("1. Get all accounts");
             System.out.println("2. Get all acounts for 1 user");
             System.out.println("3. Get 1 account for 1 User");
             System.out.println("4. Create a new account");
             System.out.println("5. Withdraw from account");
             System.out.println("6. Deposit to account");
             System.out.println("7. Make a transaction between accounts");
             System.out.println("8. Return to MainMenu");
             System.out.print("Please select an option from 1-n\r\n");


             try{
                 switch (getNextPosIntValue()){
                     case 1:
                         return Trampoline.more(() -> accountController.getAllAccounts());
                     case 2:
                         System.out.println("Enter customer_id");
                         try {
                             int customer_id = Integer.parseInt(br.readLine());
                             return Trampoline.more(() -> accountController.getAccountsById(customer_id));
                         } catch (Exception e){
                             System.out.println("invalid input\nReturning to Account Menu");
                             return Trampoline.more(this::AccountMenu);
                         }
                     case 3:
                         try
                         {
                             System.out.println("Enter customer_id");
                             int customer_id = Integer.parseInt(br.readLine());
                             System.out.println("Enter account id");
                             int account_id = Integer.parseInt(br.readLine());
                             return Trampoline.more(() -> accountController.getAccountById(customer_id,account_id));
                         }catch (Exception e) {
                             System.out.println("invalid input\nReturning to Account Menu");
                             return Trampoline.more(this::AccountMenu);
                         }
                     case 4:
                         try {
                             System.out.println("You are now creating a new account.\n Please enter the start deposit you wish to deposit");
                             int balance = Integer.parseInt(br.readLine());
                             System.out.println("Enter your customer_id");
                             int customer_id = Integer.parseInt(br.readLine());
                             return Trampoline.more(() -> accountController.createAccount(customer_id, balance));
                         }catch (Exception v){
                             System.out.println("invalid input\nReturning to Account Menu");
                             return Trampoline.more(this::AccountMenu);
                         }
                     case 5:
                         try {
                             System.out.println("Enter id of account you want to withdraw from");
                             int accId = Integer.parseInt(br.readLine());
                             System.out.println("Enter the amount you wish to withdraw");
                             int amount = Integer.parseInt(br.readLine());

                             return Trampoline.more(() -> accountController.withdrawFromAccount(accId, amount));
                         } catch (Exception o){
                             System.out.println("invalid input\nReturning to Account Menu");
                             return Trampoline.more(this::AccountMenu);
                         }
                     case 6:
                         try {
                             System.out.println("Enter id of account you wish to deposit to");
                             int depoAccId = Integer.parseInt(br.readLine());
                             System.out.println("Enter amount you wish to deposit");
                             int depoAmount = Integer.parseInt(br.readLine());
                             return Trampoline.more(() -> accountController.depositToAccount(depoAccId, depoAmount));
                         }catch (Exception f){
                             System.out.println("invalid input\nReturning to Account Menu");
                             return Trampoline.more(this::AccountMenu);
                         }
                     case 7:
                         System.out.println("Transfering money\n");
                         System.out.println("From which account do you wish to transfer from?: ");
                         int fromAccount = Integer.parseInt(br.readLine());
                         System.out.println("To which account do you wish to tranfer to?: ");
                         int toAccount = Integer.parseInt(br.readLine());
                         if (fromAccount == toAccount){
                             System.out.println("Need to choose different accounts");
                             return Trampoline.more(this::AccountMenu);
                         }
                         else if (fromAccount != toAccount){
                             System.out.println("Enter the amount you want to transfer: ");
                             int transferAmount = Integer.parseInt(br.readLine());
                             return Trampoline.more(() -> accountController.transfer(fromAccount,toAccount,transferAmount));
                         }
                     case 8:
                         try {
                             System.out.println("Press any key to return to main menu");
                             getNextStringValue();
                             return Trampoline.more(this::MainMenu);
                         } catch (Exception e )  {
                             e.printStackTrace();
                         }
                         return Trampoline.more(this::MainMenu);
                         default:
                         System.out.println("invalid selection. Returning to main menu\n");


                 }
             }catch (IOException | NumberFormatException g){
                 System.out.println("IO error trying to read your input!");
             }
         }

    }

}
