package model;

public class Account {

    private int customer_id;
    private int account_id;
    private int balance;

    public Account(int Customer_id, int account_id, int Balance){
        this.customer_id = Customer_id;
        this.account_id = account_id;
        this.balance = Balance;

    }
    public Account(){}

    public int getCustomer_id() { return customer_id; }

    public void setCustomer_id(int customer_id) {this.customer_id = customer_id;}

    public int getAccount_id() {return account_id;}

    public void setAccount_id(int account_id){this.account_id = account_id; }

    public int getBalance() {return balance;}

    public void setBalance(int balance){this.balance = balance;}

    @Override
    public String toString() {

        return  account_id + " [" + customer_id + "]" +" [" + balance + "]";
}
}