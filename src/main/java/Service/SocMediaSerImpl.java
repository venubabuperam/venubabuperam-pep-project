package Service;

import java.util.List;
import DAO.SoaMediaDAO;
import DAO.SoaMediaDAOImpl;
import Model.Account;
import Model.Message;


public class SocMediaSerImpl implements SocMediaServ {

    private SoaMediaDAO soaMediaDAO;
    public String username;
    public String password;
    public int account_id;
    
    public SocMediaSerImpl(){
        this.soaMediaDAO = new SoaMediaDAOImpl();
    }

    @Override
    public Account addAccount(Account account) {
       // Account existingAccount = soaMediaDAO.getUsername()
       
        return this.soaMediaDAO.addAccount(account);
              
    }
    @Override
    public boolean isUsernameExist(String username){
        return soaMediaDAO.isUsernameExist(username);
    }

    public Account verifyLogin(Account account){

        return soaMediaDAO.verifyLogin(account.getUsername(), account.getPassword());
   
      
       }

    @Override
    public Message createMessage(Message message) {
        if(message.getMessage_text() == "" || message.getMessage_text().length() >254){
            return null;
       }else{
          return soaMediaDAO.createMessage(message);
    }  

    
}

    @Override
    public List<Message> getAllMessages() {
        return soaMediaDAO.getAllMessages();
    }

    @Override
    public Message getMessageById(int message_id) {
        return soaMediaDAO.getMessageById(message_id);
    }

    @Override
    public Message deleteMessageById(int message_id){
       
        if(soaMediaDAO.getMessageById(message_id) != null){
            Message message = soaMediaDAO.getMessageById(message_id);
            soaMediaDAO.deleteMessageById(message_id);
            return message;
        }
         return null;
    }

    @Override
    public Message updateMessageById(Message message, int message_id) {
       Message messages = soaMediaDAO.getMessageById(message_id);

       if ( messages != null && messages.message_text.length() <256) {
           return soaMediaDAO.getMessageById(message_id);
                    
        } else {
            return null;             
        }
       // return messages;
    }

    @Override
    public List<Message> getAllMessagesById(int account_id) {
        return soaMediaDAO.getAllMessagesById(account_id);
    }
       
    
}