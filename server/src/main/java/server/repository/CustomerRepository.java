package server.repository;

import server.model.Customer;

import java.sql.*;
import java.util.ArrayList;

/**
 * A repository class for database operations for customer objects.
 */
public class CustomerRepository extends BaseRepository {

    /**
     * Creates a customer in the database.
     *
     * @return The customer with the primary key added.
     */
    public synchronized Customer createCustomer(Customer customer) {


        PreparedStatement ps = null;
        String sql = "INSERT INTO customers (name) VALUES (?)";

        try {
             ps = prepareInsert(sql);
            ps.setString(1, customer.getName());
            int primary_key = executeInsertPreparedStatement(ps);
            customer.setCustomerId(primary_key);

           if(primary_key == 0)
           {
               return null;
           }

        } catch (SQLException ex) {
            //TODO: Add some sensible error handling!
            //For example you may use
            // throw ex;
        } finally {
            cleanUp(ps);
        }
        // Return the new customer
        return customer;
    }

    /**
     * Updates a customer in the database based on the primary key.
     * @param customer The customer to update.
     */
    public synchronized Customer updateCustomer(Customer customer) {

        String sql = "UPDATE customers SET name = ? WHERE customer_id = ?";
        PreparedStatement ps = prepareQuery(sql);

       // Customer customer1 = getCustomer(customer.getCustomerId());

        try {
            ps.setString(1, customer.getName());
            ps.setInt(2 , customer.getCustomerId());
            executePreparedStatementQuery(ps);

            int check = ps.executeUpdate();

            if(check == 0)
            {
                return null;
            }


        } catch (SQLException  ex) {
            ex.printStackTrace();

            //TODO: Add some sensible error handling! What happens if the primary key is not set?
        } finally {
            cleanUp(ps);
        }
        return customer;
    }



    /**
     * Gets all customers from the database.
     * @return A list of customers.
     */
    public synchronized ArrayList<Customer> getCustomers() {


        String sql = "SELECT * FROM customers";
        PreparedStatement ps = prepareQuery(sql);
        ResultSet rs = executePreparedStatementQuery(ps);

        ArrayList<Customer> customers = new ArrayList<>();

        if (rs == null)
            return customers;

        try {
            while (rs.next()) {
                Customer customer = new Customer(
                                rs.getString("name"),
                                rs.getInt("customer_id"));
                customers.add(customer);
            }
        } catch (SQLException ex) {
                 ex.printStackTrace();
        }  finally {
            cleanUp(rs, ps);
        }

        return customers;
    }

    /**
     * Gets a customer from the database by id.
     * @param customer_id The id of the customer.
     * @return The customer, if found. Else null.
     */


    //Feil med dbCon, fucker opp
    public Customer getCustomer(int customer_id) {

        //Connection dbCon = null;

        Customer customer = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT * FROM customers WHERE customer_id = ?";
            ps = super.prepareQuery(sql);

            ps.setInt(1, customer_id);
            rs = super.executePreparedStatementQuery(ps);

            if (rs != null && rs.next())
                customer = new Customer(rs.getString("name"), rs.getInt("customer_id"));



            }catch (Exception ee){
                ee.printStackTrace();

            }finally {
            cleanUp(rs, ps);

        }

        return customer;
    }


}
