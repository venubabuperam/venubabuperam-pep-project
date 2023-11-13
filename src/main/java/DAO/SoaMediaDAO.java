package DAO;

import java.util.List;
import Model.Account;
import Model.Message;

public interface SoaMediaDAO {
    
    // define CRUD operations

    // create 
    public abstract Account addAccount(Account account);
    public boolean isUsernameExist(String username);
    public Message createMessage( Message message);

    // read
    public Account verifyLogin(String username, String password);
    public List<Message> getAllMessages();
    public Message getMessageById(int message_id);
    public List<Message> getAllMessagesById(int account_id);   
    
    // update
    public boolean updateMessageById(Message message);
    // delete
    public void deleteMessageById( int message_id);
}
    