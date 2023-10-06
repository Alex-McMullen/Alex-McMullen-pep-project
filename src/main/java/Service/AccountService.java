package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService 
{
    AccountDAO accountDAO;

    /**
     * No-args constructor for a accountService instantiates a plain accountDAO
     */
    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for a accountService when an accountDAO is provided
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    /**
     * Use accountDAO to insert an account to the database
     * 
     * @param account, the account to be created
     * @return the created account
     */
    public Account createAccount(Account account)
    {
        if(accountDAO.getAccountByUsername(account.getUsername()) == null)
        {
            if(account.getUsername().length() > 0 && account.getPassword().length() >= 4)
            {
                return accountDAO.insertAccount(account);
            }
        }
        return null;
    }

    /**
     * Use accountDAO to login to an account
     * 
     * @param account, an account object containing the username and password used to attempt the login
     * @return the account that was logged into
     */
    public Account loginAccount(Account account)
    {
        if(accountDAO.getAccountByUsername(account.getUsername()) != null)
        {
            return accountDAO.loginAccount(account);
        }
        return null;
    }
}
