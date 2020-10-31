package model;

public class BankTeller {

    private String tellerName;
    private String password;

    public BankTeller(String name, String password){
        this.tellerName = name;
        this.password = password;
    }

    public BankTeller(){}

    public String getTellerName(){return tellerName;}

    public void setTellerName(String tellerName){this.tellerName = tellerName;}

    public String getPassword(){ return password;}

    public void setPassword(String password){this.password = password;}


}
