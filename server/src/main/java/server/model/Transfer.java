package server.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Transfer implements Serializable {

    //decimal formatting til 2 decimaler
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int amount;
    private int toAccount;
    private int fromAccount;

    public Transfer(int amount, int toAccount, int fromAccount){
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getToAccount() {
        return toAccount;
    }
    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }
    public int getFromAccount() {
        return fromAccount;
    }
    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }
}