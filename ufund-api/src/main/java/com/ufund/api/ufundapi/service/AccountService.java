package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Account;

public interface AccountService {
    boolean isValidUsername(String username);
    Account createAccount(String username) throws IOException;
    Account getAccount(String username) throws IOException;
    List<Account> getAllAccounts() throws IOException;
}
