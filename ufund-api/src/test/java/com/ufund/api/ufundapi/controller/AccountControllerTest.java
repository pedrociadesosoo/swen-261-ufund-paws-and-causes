package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.service.AccountService;

@Tag("Controller-tier")
public class AccountControllerTest {
    private AccountController accountController;
    private AccountService accountService;
    private Map<String, String> body;


    @BeforeEach
    public void setupAccountController(){
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
        body = new HashMap<>();
        body.put("username", "charlieg");
        body.put("password", "12345");

    }

    @Test
    public void testRegister() throws IOException{
        Account created = new Account("charlieg", "12345") {};
        when(accountService.isValidUsername("charlieg")).thenReturn(true);
        when(accountService.createAccount("charlieg", "12345")).thenReturn(created);

        ResponseEntity<Account> response = accountController.register(body);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created.getUsername(), response.getBody().getUsername());
        assertEquals(true, response.getBody().checkPassword("12345"));
    }

    @Test
    public void testRegisterConflict() throws IOException {
        when(accountService.isValidUsername("charlieg")).thenReturn(true);
        when(accountService.createAccount("charlieg", "12345")).thenReturn(null);

        ResponseEntity<Account> response = accountController.register(body);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testRegisterInvalidUsername() throws IOException {
        Account created = new Account("charlieg", "12345") {};
        when(accountService.isValidUsername("charlieg")).thenReturn(false);

        ResponseEntity<Account> response = accountController.register(body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterFail() throws IOException{
        Account created = new Account("charlieg", "12345") {};
        when(accountService.isValidUsername("charlieg")).thenReturn(true);
        when(accountService.createAccount("charlieg", "12345")).thenReturn(created);

    }

    @Test
    public void testLogin() throws IOException{
        String password = "12345";
        Account created = new Account("charlieg", "12345") {};
        when(accountService.getAccount("charlieg")).thenReturn(created);
        when(created.checkPassword(password)).thenReturn(true);

        ResponseEntity<Account> response = accountController.login(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(created.getUsername(), response.getBody().getUsername());

    }

    @Test
    public void testLoginBadRequest() throws IOException{
        String password = "12345";
        Account created = new Account("charlieg", "12345") {};
        when(accountService.getAccount("charlieg")).thenReturn(created);
        body.replace("username", null);

        ResponseEntity<Account> response = accountController.login(body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody());
        body.replace("username", "charlieg");
    }

    @Test
    public void testLoginNotFound() throws IOException{
        String password = "12345";
        Account created = new Account("charlieg", "12345") {};
        when(accountService.getAccount("charlieg")).thenReturn(null);

        ResponseEntity<Account> response = accountController.login(body);
        assertEquals(HttpStatus.NOT_FOUND, response.getBody());
    }

    @Test
    public void testLoginUnauthorized() throws IOException{
        String password = "12345";
        Account created = new Account("charlieg", "12345") {};
        when(accountService.getAccount("charlieg")).thenReturn(created);
        body.replace("password", "1234");

        ResponseEntity<Account> response = accountController.login(body);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getBody());
        body.replace("password", "12345");

    }

}
