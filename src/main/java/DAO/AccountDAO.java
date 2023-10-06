package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO 
{
    /**
     * Insert a new account into the account table
     * 
     * @param account, the account object to be inserted
     * @return the inserted account object
     */
    public Account insertAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "insert into account (username, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next())
            {
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Login to an account given the username and password of said account
     * 
     * @param account, an account object containing the username and password used to attempt to login
     * @return the account that was logged into
     */
    public Account loginAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "select * from account where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Account newAccount = new Account(rs.getInt("account_id"),
                                rs.getString("username"),
                                rs.getString("password"));
                return newAccount;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve an account from the database based on its username
     * 
     * @param username, the username of the account to be retrieved
     * @return the retrieved account
     */
    public Account getAccountByUsername(String username)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "select * from account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"),
                                rs.getString("username"),
                                rs.getString("password"));
                return account;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
        
    }
}
