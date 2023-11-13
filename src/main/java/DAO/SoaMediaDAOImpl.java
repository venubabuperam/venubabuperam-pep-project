package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SoaMediaDAOImpl implements SoaMediaDAO {

    @Override
    public Account addAccount(Account account) {
        // create a connection and close connection
        try(Connection connection = ConnectionUtil.getConnection()) {
            // sql
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // create a statement
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // execute statement
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            
            statement.executeUpdate();

            // process results
            ResultSet keys = statement.getGeneratedKeys();

            // if there is a key to return
            if (keys.next()) {
                // getInt(1) gets the first int
                int generatedAccId = (int) keys.getLong(1);
                return new Account(generatedAccId, account.getUsername(),account.getPassword());
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }


        return null;
    }
    public Account verifyLogin(String username, String password){
        Connection conn = ConnectionUtil.getConnection();
        String sql ="select * from account where username = ? and password=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
          
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
             Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
        
    }
    @Override
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        String sql ="insert into message (posted_by, message_text, time_posted_epoch) values(?,?,?)";
        //"insert into message ( message_id, posted_by,  message_text,  time_posted_epoch) values (?,?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           // ps.setInt(1, message.getMessage_id());
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()){
                int generated_message_id = (int) keys.getLong(1);
                //String generated_message_text = (String) rs.getString("hello message");
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                 message.getTime_posted_epoch());
            }                
            
        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message();
        try {
            //Write SQL logic here
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                   message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }
    public void deleteMessageById( int message_id){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql ="DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id );            
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
       
    }
    public boolean updateMessageById(Message message){
        Connection connection = ConnectionUtil.getConnection();
        String sql="update message set message_text = ? where message_id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);            
            ps.setString(1, message.message_text);
            ps.setInt(2,message. message_id);
            int i =ps.executeUpdate(); 
            if(i ==1)
          return true;
          
        
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public List<Message> getAllMessagesById(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();    
        try{
            PreparedStatement ps = connection.prepareStatement("select * from message where posted_by =  ?");
            ps.setInt(1, account_id);
           
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch"));
                messages.add(message);                
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
           return messages;
       
    }
    @Override
    public boolean isUsernameExist(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "Select * from account where username = ?";
            
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return true;
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    }

    
    

    