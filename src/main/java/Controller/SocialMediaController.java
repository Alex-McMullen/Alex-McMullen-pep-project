package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postCreateAccountHandler);
        app.post("/login", this::postLoginAccountHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);

        app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * Handler to post a new account
     * The ObjectMapper will automatically convert the JSON of the POST request into an Account object
     * If AccountService returns a null account, the API will return a 400 message
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method. 
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postCreateAccountHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);
        Account addedAccount = accountService.createAccount(account);
        if(addedAccount != null)
        {
            context.json(om.writeValueAsString(addedAccount));
        }
        else
        {
            context.status(400);
        }
    }

    /**
     * Handler to login to an account
     * The ObjectMapper will automatically convert the JSON of the POST request into an Account object
     * If AccountService returns a null account, the API will return a 401 message
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method. 
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postLoginAccountHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);
        Account loginToAccount = accountService.loginAccount(account);
        if(loginToAccount != null)
        {
            context.json(om.writeValueAsString(loginToAccount));
        }
        else
        {
            context.status(401);
        }
    }

    /**
     * Handler to post a new message
     * The ObjectMapper will automatically convert the JSON of the POST request into a Message object
     * If MessageService returns a null message, the API will return a 400 message
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method. 
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postCreateMessageHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(context.body(), Message.class);
        Message addedMessage = messageService.insertMessage(message);
        if(addedMessage != null)
        {
            context.json(om.writeValueAsString(addedMessage));
        }
        else
        {
            context.status(400);
        }
    }

    /**
     * Handler to retrieve all messages
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessagesHandler(Context context)
    {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * Handler to get a message by its id
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByIdHandler(Context context)
    {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message != null)
        {
            context.json(message);
        }
    }

    /**
     * Handler to delete a message by its id\
     * If MessageService returns a null message, the API will return a 200 message
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.delete method
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException
    {   
        ObjectMapper om = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message != null)
        {
            context.json(om.writeValueAsString(message));
        }
        else
        {
            context.status(200);
        }
    }

    /**
     * Handler to update a message
     * If MessageService returns a null message, the API will return a 400 message
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.patch method
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void patchMessageByIdHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        if(updatedMessage != null)
        {
            context.json(om.writeValueAsString(updatedMessage));
        }
        else
        {
            context.status(400);
        }
    }

    /**
     * Handler to get all messages from an account
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method
     */
    private void getAllMessagesByAccountHandler(Context context)
    {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessagesFromUser(account_id));
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}