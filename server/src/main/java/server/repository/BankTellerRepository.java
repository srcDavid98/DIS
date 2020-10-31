package server.repository;

import server.model.BankTeller;
import server.model.Customer;
import server.util.Hashing;

import java.sql.*;
import java.util.ArrayList;


public class BankTellerRepository extends BaseRepository {


    // Boolean fordi telleren enten eksisterer eller ikke
   /* public BankTeller validateBankTeller(String username, String password){

        //Vi setter teleren til null for å opprette en connection etterpå

        BankTeller bankTeller = null;

        try{

            //Vår sql statement som går inn i databsen og utfører de nødvendige funksjonene
            String sql = "SELECT * FROM tellers WHERE username = ? and password = ?";

            //preparedstatement som utfører sql queryen vi har definert ovenfor
            PreparedStatement ps = super.prepareQuery(sql);

            //Definerer parameter 1 & 2 som skal insettes i vår sql query
            ps.setString(1, username);
            ps.setString(2, password);

            //Oppretter resultset som er en midlertidig liste for alle BankTellers (er i grunn kun én så egentlig likegyldig)
            ResultSet rs = super.executePreparedStatementQuery(ps);

       //if statement som sjekker om hvorvidt listen er tom
            //Om listen er tom oppretter vi et nytt Bankteller objekt og retunrer true (aka loggin er validert)
          if(rs != null && rs.next())
            bankTeller= new BankTeller(rs.getString("username"), rs.getString("password"));

        }  catch (Exception e) {
             e.printStackTrace();
        }
       return bankTeller;

    }               */

          // Boolean fordi telleren enten eksisterer eller ikke
          public boolean validateBankTeller(BankTeller bankTeller){

              String sql = "SELECT * FROM tellers WHERE username = ? and password = ?";

              try{

                  //preparedstatement som utfører sql queryen vi har definert ovenfor
                  PreparedStatement ps = super.prepareQuery(sql);

                  //Definerer parameter 1 & 2 som skal insettes i vår sql query
                  ps.setString(1, bankTeller.getName());
                  ps.setString(2, bankTeller.getPassword());

                  //Oppretter resultset som er en midlertidig liste for alle BankTellers (er i grunn kun én så egentlig likegyldig)
                  ResultSet rs = super.executePreparedStatementQuery(ps);

             //if statement som sjekker om hvorvidt listen er tom
                  //Om listen er tom oppretter vi et nytt Bankteller objekt og retunrer true (aka loggin er validert)
                if(rs != null && rs.next()) {
                    new BankTeller(rs.getString("username"), (rs.getString("password")));
                    return true;
                }


              }  catch (Exception e) {
                   e.printStackTrace();
              }

              return false;
    }

    public boolean authorizeUser(String username, String password) throws  SQLException  {


        //Der oprettes en teller som sættes til nul
        BankTeller bankTeller = null;
        PreparedStatement ps = null;
        //Try-catch med en SQL-statement som sørger for at finde brugeren med det indtastet "username" & "password".

        try {
            String sql =("SELECT * FROM tellers WHERE username = ? AND password = ?");
            ps = super.prepareQuery(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = super.executePreparedStatementQuery(ps);
            if (rs !=null && rs.next()) {
                new BankTeller
                        (rs.getString("username"),
                                (rs.getString("password")));
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            cleanUp(ps);
        }
        return false;
    }

    public BankTeller createTeller(BankTeller bankteller) throws SQLException {

        String sql = "INSERT INTO tellers (username, password) VALUES (?,?)";

        if (bankteller.getName() == null || bankteller.getName().isEmpty())
            throw new SQLException("Customer password cannot be null or empty.");

        PreparedStatement ps= null;

        try
        {
            ps = prepareInsert(sql);
            ps.setString(1, bankteller.getName());
            ps.setString(2, Hashing.hasWithSalt(bankteller.getPassword()));

            executeInsertPreparedStatement(ps);

        } catch (SQLException ex) {

            throw new SQLException("An error occurred while trying to insert the customer into the database. Please try again later or report this error to error@Disbank.dk");
        }
        finally {
            cleanUp(ps);
        }
        // Return the new customer
        return bankteller;
    }
}



