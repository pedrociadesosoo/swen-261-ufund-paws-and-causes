package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.AccountDAO;
import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.HelperAccount;
import com.ufund.api.ufundapi.model.ManagerAccount;

@Service
public class AccountServiceImpl implements AccountService {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{1,20}$";
    private AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public boolean isValidUsername(String username) {
        return username != null && username.matches(USERNAME_PATTERN);
    }

    @Override
    public Account createAccount(String username, String password) throws IOException {
        if (accountDAO.getAccount(username) != null)
            return null;
        Account account = username.equals("admin")
            ? new ManagerAccount(username, password)
            : new HelperAccount(username, password, new ArrayList<>());
        return accountDAO.createAccount(account);
    }

    @Override
    public Account getAccount(String username) throws IOException {
        return accountDAO.getAccount(username);
    }

    @Override
    public List<Account> getAllAccounts() throws IOException {
        return accountDAO.getAllAccounts();
    }
}
