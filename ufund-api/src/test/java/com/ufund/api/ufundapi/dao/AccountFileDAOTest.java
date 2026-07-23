package com.ufund.api.ufundapi.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.HelperAccount;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;

@Tag("Persistence-tier")
public class AccountFileDAOTest {
    private ObjectMapper mockMapper;
    private AccountFileDAO afDao;

    private ArrayList<Account> sampleAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new HelperAccount("charlieg", "12345", new ArrayList<>()));
        accounts.add(new HelperAccount("sarahg", "6969", new ArrayList<>()));
        accounts.add(new ManagerAccount("admin", "4567"));
        return accounts;
    }

    @BeforeEach
    public void setupAccountFileDAO() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.readValue(any(File.class), eq(Account[].class))).thenReturn(sampleAccounts().toArray(new Account[0]));
        afDao = new AccountFileDAO("data/accounts.json", mockMapper);

    }

    @Test
    public void testGetAccountReturnsAccount() throws IOException{
        Account account = afDao.getAccount("charlieg");

        assertNotNull(account);
        assertEquals("charlieg", account.getUsername());
        assertEquals(true, account.checkPassword("12345"));
        
    }

    @Test
    public void testGetAccountMissing() throws IOException{
        Account account = afDao.getAccount("glorbo");
        assertNull(account);
    }

    @Test
    public void testGetAllAccountsReturnsAll() throws IOException{
        List<Account> accounts = afDao.getAllAccounts();

        assertNotNull(accounts);
        Account charlie = accounts.stream()
            .filter(a -> a.getUsername().equals("charlieg"))
            .findFirst().orElse(null);
        assertNotNull(charlie);
        assertTrue(charlie.checkPassword("12345"));
    }

    @Test 
    void testCreateAccount() throws IOException{
        Account account = afDao.createAccount(new HelperAccount("louiehue", "12039", new ArrayList<>()));

        Account retrieve = afDao.getAccount("louiehue");
        assertEquals(account.getUsername(), retrieve.getUsername());
        assertTrue(retrieve.checkPassword("12039"));
    }

}
