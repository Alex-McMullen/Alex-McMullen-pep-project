package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService
{
    MessageDAO messageDAO;

    /**
     * No-args constructor for a messageService instantiates a plain messageDAO
     */
    public MessageService()
    {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a messageService when a messageDAO is provided
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO;
    }

    /**
     * Use messageDAO to add a new message to the database
     * 
     * @param message, new message to be inserted to the database
     * @return the inserted message
     */
    public Message insertMessage(Message message)
    {
        if(messageDAO.getMessagesByUser(message.getPosted_by()) != null)
        {
            if(message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255)
            {
                return messageDAO.insertMessage(message);
            }
        }
        return null;
    }

    /**
     * Use messageDAO to retrieve all messages in the database
     * 
     * @return a list of all messages in the database
     */
    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    /**
     * Use messageDAO to retrieve a message by its message_id
     * 
     * @param id, the message_id of the message being retrieved
     * @return the message being retrieved
     */
    public Message getMessageById(int id)
    {
        if((Integer) id != null)
        {
            return messageDAO.getMessageById(id);
        }
        return null;
    }

    /**
     * Use messageDAO to delete a message given its message_id
     * 
     * @param id, the id of the message to be deleted
     * @return the deleted message
     */
    public Message deleteMessage(int id)
    {
        if(messageDAO.getMessageById(id) != null)
        {
            return messageDAO.deleteMessageById(id);
        }
        return null;
    }

    /**
     * Use messageDAO to update a message
     * 
     * @param id, the id of the message to be updated
     * @param message, the message object containing the updated information
     * @return the updated message
     */
    public Message updateMessage(int id, Message message)
    {
        if(messageDAO.getMessageById(id) != null)
        {
            if(message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255)
            {
                messageDAO.updateMessage(id, message);
                return messageDAO.getMessageById(id);
            }
        }
        return null;
    }

    /**
     * Use messageDAO to retrieve all messages from a specific account
     * 
     * @param user_id, the account id of the user
     * @return a list of messages from the account
     */
    public List<Message> getAllMessagesFromUser(int user_id)
    {
        return messageDAO.getMessagesByUser(user_id);
    }
}




//You will need to design and create your own Service classes from scratch.
//You should refer to prior mini-project lab examples and course material for guidance.