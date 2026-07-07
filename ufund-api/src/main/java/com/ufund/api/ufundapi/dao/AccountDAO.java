package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Account;

public interface AccountDAO {
    Account getAccount(String username) throws IOException;
    Account createAccount(Account account) throws IOException;
    List<Account> getAllAccounts() throws IOException;
}
