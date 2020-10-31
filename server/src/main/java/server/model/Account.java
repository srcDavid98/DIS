package server.model;

/**
 * A class representing an account in the system.
 *
 */
public class Account {
    //TODO: Implement the class. See init.sql for suggested fields - though remember that they may be
    //named differently here, if you so wish.

       //Innehaver av kontoene sin ID
        private int customer_id;
        //id for en gitt konto
        private int account_id;
        //Balansen på en gitt konto
        private int balance;


        public Account(int customer_id, int account_id, int balance) {
            this.customer_id = customer_id;
            this.balance = balance;
            this.account_id = account_id;
        }



        //Tom Customer constructor slik at vi kan oprette nye "states" for Accounts og sende frem og tilbake
        //mellom web-browser og intelliJ
        public Account(){}



        public int getBalance()  { return balance; }

        public void setBalance(int Balance) { this.balance = Balance; }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomerId(int customer_Id) {
            this.customer_id = customer_Id;
        }

        public int getAccountId() { return account_id; }

        public void setAccountId(int account_id) { this.account_id = account_id; }



}


