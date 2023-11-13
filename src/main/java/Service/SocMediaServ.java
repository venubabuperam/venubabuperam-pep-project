package Service;

import java.util.List;
import Model.Account;
import Model.Message;

public interface SocMediaServ {


    // create 
    public abstract Account addAccount(Account account);
    public boolean isUsernameExist(String username);
    public Message createMessage(Message message);
       //read
    public Account verifyLogin(Account account);
    public List<Message> getAllMessages();
    public Message getMessageById(int message_id);
    public List<Message> getAllMessagesById(int account_id);
    
    //update
    public Message updateMessageById(Message message, int message_id);
    //delete
    public Message deleteMessageById(int message_id);
   
   
}