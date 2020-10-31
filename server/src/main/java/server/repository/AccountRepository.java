package server.repository;

import server.model.Account;
import server.model.Customer;

import java.sql.*;
import java.util.ArrayList;

/**
 * A repository class for database operations for account objects.
 */
public class AccountRepository extends BaseRepository {
    private Connection connection;



    //Vi lager en arraylist for accountnumbers
    public synchronized ArrayList<Account> getAccounts() throws SQLException {


        // Bygger SQL queryen
        String sql = "SELECT * FROM accounts";

        // Instantierer en resultset som vil inneha dataen fra SQL queryen
           PreparedStatement ps = prepareQuery(sql);
           ResultSet rs = executePreparedStatementQuery(ps);

        //Instatierer Arrayliste som lar oss lagre dataen
        ArrayList<Account> accounts = new ArrayList<Account>();
        if (rs == null)
            return accounts;

        try {
            // Loop through DB Data
            while (rs.next()) {
                Account account =
                        new Account(
                                rs.getInt("customer_id"),
                                rs.getInt("account_id"),
                                rs.getInt("balance"));

                // Add elemeqnt to list
                accounts.add(account);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
        cleanUp(rs, ps);
    }
        // Return the list of users
        return accounts;



        }


    //Metode som lar oss hente alle kontoer for en bruker
    public synchronized ArrayList<Account> getAccountsById(int customer_id) {

        ArrayList<Account> accounts = new ArrayList<>();

        PreparedStatement ps = null;

        ResultSet rs = null;

            try {
                String sql = "SELECT * FROM accounts WHERE customer_id = ?";

                ps = super.prepareQuery(sql);

                ps.setInt(1, customer_id);

                rs = super.executePreparedStatementQuery(ps);


                // Loop through DB Data
                while (rs!= null && rs.next()) {
                    Account account =
                            new Account(
                                    rs.getInt("customer_id"),
                                    rs.getInt("account_id"),
                                    rs.getInt("balance"));

                    // Add element to list
                    accounts.add(account);
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                cleanUp(rs, ps);
            }

            //Returner alle accounts til én bruker
            return accounts;

        }



    //Metode som lar oss hente en konto for én bruker
    public synchronized Account getAccountById(int customer_id,int account_id){

      Account account = null;

      //Deklarer rs & ps = null for å bruke cleanup metode mht -> Hikari.con
         PreparedStatement ps = null;
          ResultSet rs = null;
      try {

       String sql = "SELECT * FROM accounts WHERE customer_id = ? AND account_id = ?";
        ps = super.prepareQuery(sql);

       ps.setInt(1, customer_id);
       ps.setInt(2, account_id);
       rs = super.executePreparedStatementQuery(ps);

       if(rs != null && rs.next())
       account = new Account(
               rs.getInt("customer_id"),
               rs.getInt("account_id"),
               rs.getInt("balance"));

   } catch (SQLException e){
       e.printStackTrace();
   } finally {
       cleanUp(rs, ps);
   }
return account;

    }

    /**
     * Bruker metoden i vår transfer metode
     * @param account_id
     * @return account object fra databasekall
     * */
    public synchronized Account getSpecificAccount(int account_id){

        Account account = null;

        //Deklarer rs & ps = null for å bruke cleanup metode mht -> Hikari.con
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT * FROM accounts WHERE account_id = ?";
            ps = super.prepareQuery(sql);


            ps.setInt(1, account_id);
            rs = super.executePreparedStatementQuery(ps);

            if(rs != null && rs.next())
                account = new Account(
                        rs.getInt("customer_id"),
                        rs.getInt("account_id"),
                        rs.getInt("balance"));

        } catch (SQLException | NumberFormatException e){
            e.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return account;

    }

    /**
     * Creates an account for a customer
     * @param account
     * @return account object
     * */
    public Account createAccount(Account account){

        PreparedStatement ps = null;

        String sql = "INSERT INTO accounts (customer_id, balance) VALUES (?,?)";

        try {
            ps = prepareInsert(sql);

               ps.setInt(1, account.getCustomer_id());
               ps.setInt(2, account.getBalance());
                   int primary_key = executeInsertPreparedStatement(ps);
                   account.setAccountId(primary_key);

            if(primary_key == 0)
            {
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(ps);
        }

        return account;
    }


    /**
     * Metode for å trekke penger fra en konto
     *
     * */
    public synchronized Account withdrawFromAccount(int amount, int account_id) {


        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ? " ;

        PreparedStatement ps =  prepareQuery(sql);

         Account account = new Account();
        try {

            ps.setInt(1, amount);
            ps.setInt(2, account_id);
            int check = ps.executeUpdate();

            if(check == 0){
                 return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
    } finally {
            cleanUp(ps);
        }
        return account;

    }
    public synchronized Account DepositToAccount(int amount, int account_id){

        String sql = "UPDATE accounts SET balance = Balance + ? WHERE account_id = ? ";


        PreparedStatement ps = prepareQuery(sql);
        Account account = new Account();

        try{
            //account = getSpecificAccount(account_id);
            ps.setInt(1,amount);
            ps.setInt(2,account_id);

           int check = ps.executeUpdate();
           if(check == 0){
               return null;
           }


        }catch (SQLException ef){
            ef.printStackTrace();
        }finally {
            cleanUp(ps);
        }
       return account;
    }


    /**
     *
     *
     *
     * */
    public synchronized Account transfer(int fromAccount, int toAccount, int amount) throws SQLException {

        connection = null;
        Account accounts = new Account();

        PreparedStatement withdraw = null;
        PreparedStatement deposit = null;

    try {

        connection = getConnection();
        connection.setAutoCommit(false);

        String sqlFrommAccount = "UPDATE accounts SET balance =  balance - ? WHERE account_id = ?";
        String sqlToAccount = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

        withdraw = prepareQuery2(connection,sqlFrommAccount);
        deposit = prepareQuery2(connection,sqlToAccount);

        try {

            withdraw.setInt(1, amount);
            withdraw.setInt(2, fromAccount);

            int check = withdraw.executeUpdate();

            deposit.setInt(1, amount);
            deposit.setInt(2, toAccount);

            int check1 = deposit.executeUpdate();


            if (check == 1 && check1 == 1) {
                connection.commit();
                connection.setAutoCommit(true);
                return accounts;

            } else {
                connection.rollback();
                return null;
            }

        } catch (SQLException e) {
            if (accounts == null) {
                connection.rollback();

            }
            e.printStackTrace();
            throw new SQLException("There was an error transfering between accounts");
        }
    } catch (SQLException p){
        p.printStackTrace();
        throw new SQLException("There was an error transfering between accounts");

    } finally {
        cleanUp(withdraw);
        cleanUp(deposit);
        getConnection().close();

    }
    }


public synchronized void addInterest() throws SQLException{

    PreparedStatement ps = null;

    try{
        String sql = "UPDATE accounts SET Balance = Balance * 1.005";

        ps = prepareQuery(sql);

        executePreparedStatementQuery(ps);

    }finally {
            cleanUp(ps);
    }

}
}


