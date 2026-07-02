package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Account;

@Component
public class AccountFileDAO implements AccountDAO {
    private Map<String, Account> accounts = new TreeMap<>();
    private ObjectMapper objectMapper;
    private String filename;

    public AccountFileDAO(@Value("${accounts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private boolean load() throws IOException {
        accounts = new TreeMap<>();
        Account[] accountArray = objectMapper.readValue(new File(filename), Account[].class);
        for (Account account : accountArray)
            accounts.put(account.getUsername(), account);
        return true;
    }

    private boolean save() throws IOException {
        Account[] accountArray = accounts.values().toArray(new Account[0]);
        objectMapper.writeValue(new File(filename), accountArray);
        return true;
    }

    @Override
    public Account getAccount(String username) throws IOException {
        synchronized (accounts) {
            return accounts.get(username);
        }
    }

    @Override
    public Account createAccount(Account account) throws IOException {
        synchronized (accounts) {
            accounts.put(account.getUsername(), account);
            save();
            return account;
        }
    }

    @Override
    public List<Account> getAllAccounts() throws IOException {
        synchronized (accounts) {
            return new ArrayList<>(accounts.values());
        }
    }
}
