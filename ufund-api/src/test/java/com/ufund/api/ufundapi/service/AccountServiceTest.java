package com.ufund.api.ufundapi.service;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ufund.api.ufundapi.dao.AccountDAO;

@Tag("Service-tier")
public class AccountServiceTest {
    private AccountService testService;
    private AccountDAO mockDAO;
    
    @BeforeEach
    public void setupAccountServiceTest(){
        mockDAO = mock(AccountDAO.class);
        testService = new AccountServiceImpl(mockDAO);
    }
    

    @Test
    public void testValidUsername(){
        String testUsername = "test";
        boolean result = testService.isValidUsername(testUsername);
        assertTrue(result);
    }

    @Test
    public void testinValidUsername(){
        String testUsername = null;
        boolean result = testService.isValidUsername(testUsername);
        assertFalse(result);
    }

    @Test
    public void testNullUsername(){
        String testUsername = "-&(^&*%&*^";
        boolean result = testService.isValidUsername(testUsername);
        assertFalse(result);
    }   

    @Test 
    public void testGetAccount() throws IOException{
        String testUsername = "test";
        testService.getAccount(testUsername);
        verify(mockDAO).getAccount(testUsername);
    }

    @Test 
    public void testGetAllAccounts() throws IOException{
        testService.getAllAccounts();
        verify(mockDAO).getAllAccounts();
    }

    /*@Test
    public void testCreateAccount() throws IOException{
        String testUsername = "valid";
        String testPassword = "password";
        
        when(mockDAO.getAccount(testUsername)).thenReturn(null);
        Account result = testService.createAccount(testUsername, testPassword);
        
        assertInstanceOf(HelperAccount.class, result);
    }

    @Test
    public void testCreateAccountManager() throws IOException{
        Account testAdmin = new ManagerAccount(null, null)
        String testUsername = "admin";
        String testPassword = "password";
        
        when(mockDAO.getAccount(testUsername)).thenReturn(null);
        Account result = testService.createAccount(testUsername, testPassword);
        
        assertInstanceOf(ManagerAccount.class, result);
    }*/


}