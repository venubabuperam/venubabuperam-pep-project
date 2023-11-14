package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    AccountService accountService;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountService = new AccountService();
    }

    public MessageService(MessageDAO messageDAO, AccountService accountService){
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    public Message addMessage(Message message, AccountService accountService) {
        // Check if the message is valid
        if (message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255) {
            return null; // Invalid message length
        }
    
        // Check if the posted_by user exists
        Object postedBy = accountService.getAccountByIdService(message.getPosted_by());
        if (postedBy == null) {
            return null; // User does not exist
        }
    
        // Create the message
        Message newAddedMessage = messageDAO.createMessage(message);
    
        return newAddedMessage;
    }
    
    public List<Message> getAllMessagesService() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByIdService(int id) {
        Message message = messageDAO.getMessageById(id);

        return message;
    }

    public Message deleteMessageByIdService(int id) {
        Message message = messageDAO.deleteMessageById(id);

        return message;
    }


    public Message updateMessageService(int message_id, Message message){
        Message existingMessageId = messageDAO.getMessageById(message_id);
        if (existingMessageId != null) {
           messageDAO.updateMessageById(message_id, message);
           Message updatedMessage = messageDAO.getMessageById(message_id);
           return updatedMessage;
        } 
        return null;
    }

    public List<Message> getMessagesByUserIdService(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }   
}