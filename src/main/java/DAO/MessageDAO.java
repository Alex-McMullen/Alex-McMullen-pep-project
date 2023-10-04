package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO
{
    /**
     * Add a message into the database that matches the values contained in the message object
     * 
     * @param message the message that is being inserted. It does not contain a message_id
     * @return
     */
    public Message insertMessage(Message message)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) value (?, ?, ?) ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next())
            {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve all messages from the message table
     * 
     * @return messages, a list of all messages in the database
     */
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), 
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieve a message from the message table, based on its message_id
     * 
     * @param id, the id of the message that is being searched
     * @return message, the message that is being searched
     */
    public Message getMessageById(int id)
    {
        Connection connection  = ConnectionUtil.getConnection();
        try
        {
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Check to see if this actually works
     * 
     * Delete a message from the message table, based on its message_id
     * 
     * @param id, the id of the message to be deleted
     * @return message, the message that was deleted
     */
    public Message deleteMessageById(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try
        {
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            sql = "delete from message where message_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Make sure this works properly and returns updated message
     * 
     * update a message from the message table based on its message_id
     * 
     * @param id, the message_id of the message to be updated
     * @param message, the message object containing the updated information
     */
    public void updateMessage(int id, Message message)
    {
        Connection connection  = ConnectionUtil.getConnection();
        try
        {
            String sql = "update message set posted_by = ?, message_text = ?, time_posted_epoch = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieve all messages from the message table based on who posted them
     * 
     * @param user_id, the user_id of the user whose messages are being searched
     * @return messages, a list of messages posted by a given user
     */
    public List<Message> getMessagesByUser(int user_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try
        {
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, user_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), 
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}

//You will need to design and create your own DAO classes from scratch. 
//You should refer to prior mini-project lab examples and course material for guidance.

//Please refrain from using a 'try-with-resources' block when connecting to your database. 
//The ConnectionUtil provided uses a singleton, and using a try-with-resources will cause issues in the tests.
