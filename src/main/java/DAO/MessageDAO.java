package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO message (message_text, posted_by) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message.getPosted_by());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            
            if (generatedKeys.next()) {
                int messageId = generatedKeys.getInt(1);
                message.setMessage_id(messageId);
            }

            return message;

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }



    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getInt("time_posted_epoch"));

                messages.add(message);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }


    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * from message WHERE message_id = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getInt("time_posted_epoch"));
                return message;
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    

    public Message deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            // first, get message by id
            String sql = "SELECT * from message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            // check if message exists
            while(rs.next()){
                Message deletedMessage = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getInt("time_posted_epoch")
            );

            // execute delete SQL update and return deleted message
            String deleteSql = "DELETE FROM message WHERE message_id = ?;";
            
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);

            deleteStatement.setInt(1, id);

            deleteStatement.executeUpdate();

            return deletedMessage;
        } 

        } catch(SQLException e) {
                System.out.println(e.getMessage());
        }
            return null;
    }


    public Message updateMessageById(int id, Message message){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            return message;

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
            return null;
    }


    public List<Message> getMessagesByUserId(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, userId);
            
            ResultSet rs = preparedStatement.executeQuery();
    
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getInt("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    
}