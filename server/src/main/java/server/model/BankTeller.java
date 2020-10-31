package server.model;

public class BankTeller {

    private String username;
    private String password;

    public BankTeller(String username, String password){
        this.username = username;
        this.password = password;
    }

    public BankTeller(){}

    public String getName() { return username;}

    public void setName(String userName){ this.username = userName; }

    public String getPassword() {return  password; }

    public void setPassword(String password) {this.password = password;}



}
