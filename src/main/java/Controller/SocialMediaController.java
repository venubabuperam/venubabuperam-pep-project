package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */


    // Create a new account
    private void postAccount(Context ctx) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAddedAccount = accountService.addAccount(account);

        if(newAddedAccount == null || newAddedAccount.username == "") {
            ctx.status(400);
        }else{
            ctx.status(200);
            ctx.json(newAddedAccount);
            System.out.println(newAddedAccount);
        }
    }


    // Login to existing account
    private void loginAccount(Context ctx) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account inputAccount = mapper.readValue(ctx.body(), Account.class);
        Account existingAccount = accountService.getAccount(inputAccount);

        if (existingAccount != null && existingAccount.getUsername().equals(inputAccount.getUsername()) && existingAccount.getPassword().equals(inputAccount.getPassword())) {
            ctx.status(200); // Successful login
            ctx.json(existingAccount);
        } else {
            ctx.status(401); // Invalid login credentials
        }
    }


    // Create a new message
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Message inputMessage = mapper.readValue(ctx.body(), Message.class);
        
        if (inputMessage.getMessage_text() != null && !inputMessage.getMessage_text().isEmpty() && inputMessage.getMessage_text().length() <= 254) {
            Message createdMessage = messageService.addMessage(inputMessage, accountService);
            if (createdMessage != null) {
                ctx.status(200); 
                ctx.json(createdMessage);
            } else {
                ctx.status(400); 
            }
        } else {
            ctx.status(400); 
        }
    }


    // Get all messages in database
    public void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessagesService();
        ctx.status(200);
        ctx.json(messages);
    }


    // Get a message by message_id
    public void getMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByIdService(message_id);

        if (message != null) {
            ctx.status(200);
            ctx.json(message);
        } else {
            ctx.status(200); // Message not found
            ctx.json("");
        }
    }

    // Delete a message by message_id
    public void deleteMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageByIdService(message_id);

        if (message != null) {
            ctx.status(200);
            ctx.json(message);
        } else {
            ctx.status(200); // Message not found
            ctx.json("");
        }
    }


    // Update a message by message_id
    public void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageService(message_id, message);

        if (updatedMessage != null && !message.getMessage_text().isBlank() && message.getMessage_text().length() < 255) {
            ctx.status(200);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }


    // get all messages from an user account
    private void getMessagesByUserIdHandler(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserIdService(userId);
        ctx.json(messages);
        ctx.status(200); 
    }
}