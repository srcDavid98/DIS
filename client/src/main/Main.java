package main;

import controller.AccountController;
import controller.CustomerController;
import controller.TellerController;
import util.NetworkUtil;
import view.MainView;

public class Main {

    public static void main(String[] args) {
        NetworkUtil util = new NetworkUtil();



        //instantierer de forskjellige views
        CustomerController controller = new CustomerController(util);
        AccountController controller1 = new AccountController(util);
        TellerController controller2 = new TellerController(util);

        //Setter mainview
        MainView view = new MainView(controller, controller1, controller2);



        controller.setView(view);
        controller1.setView(view);
        controller2.setView(view);
        view.LoginMenu().get();


    }
}
