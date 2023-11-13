package Controller;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.SocMediaSerImpl;
import Service.SocMediaServ;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private SocMediaServ socMediaServ;

    public SocialMediaController(){
        this.socMediaServ = new SocMediaSerImpl();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/registerAccount", this::addAccountHandler);
        app.post("/login", this::verifyLoginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByIdHandler);
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param username 
     * @param password 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void addAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        String username =ctx.pathParam("username");
        String password = ctx.pathParam("password");
        Account newAccount = socMediaServ.addAccount(account);
        boolean usernameExist = socMediaServ.isUsernameExist(account.username);
        if(account.getUsername().isBlank() || account.getUsername().isEmpty() || account.password.length()< 4){
            ctx.status(400);
            
           
        }else{
           // Account newAccount = socMediaServ.addAccount(account);
            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);
        }
    }
    private void verifyLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = socMediaServ.verifyLogin(account);
        if(newAccount==null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(),Message.class);
        Message newMessage = socMediaServ.createMessage(message);
        if(newMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        }
    }
    
    private void getMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = socMediaServ.getAllMessages();
        ctx.json(messages);
    }
    private void getMessagesByIdHandler(Context ctx){
        int idInput = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = socMediaServ.getMessageById(idInput);
        if(message!=null){
            ctx.json(message);
            
        }else{
            ctx.status(400);
            ctx.status(200);
            
        }
            
    }
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException , JsonMappingException{
        ObjectMapper om = new ObjectMapper();
        int id_input = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = socMediaServ.deleteMessageById(id_input);

    
         if (message != null){
          ctx.json(om.writeValueAsString(message));
          ctx.status(200);
        }else{
            ctx.body();
        }
       
    }
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int id_input = Integer.parseInt(ctx.pathParam("message_id"));
        String text = ctx.pathParam("message_text");
        Message uMessage = socMediaServ.getMessageById(id_input);

    
         if (uMessage != null){
            ctx.status(200);
            ctx.json(uMessage);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesByIdHandler(Context ctx) throws JsonProcessingException{
        int id_input = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = socMediaServ.getAllMessagesById(id_input);
        if(messages != null){
            ctx.json(messages);
        }else{
            ctx.status(400);
        }
    }

}