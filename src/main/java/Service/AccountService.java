package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        Account newAddedAccount = accountDAO.createAccount(account);
        return newAddedAccount;
    }

    public Account getAccount(Account account) {
        Account existingAccount = accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        return existingAccount;
    }

    public Object getAccountByIdService(int id) {
        Object existingAccountId = accountDAO.getAccountById(id);
        return existingAccountId;
    }
} 



    
    

   
    

    