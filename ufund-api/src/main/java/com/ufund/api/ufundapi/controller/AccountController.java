package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.service.AccountService;

@RestController
@RequestMapping("accounts")
public class AccountController {
    private static final Logger LOG = Logger.getLogger(AccountController.class.getName());
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
	String password = body.get("password");
        LOG.info("POST /accounts/register username=" + username);
        try {
            if (!accountService.isValidUsername(username))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Account created = accountService.createAccount(username, password);
            if (created == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
	String password = body.get("password");
        LOG.info("POST /accounts/login username=" + username);
        try {
            if (username == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Account account = accountService.getAccount(username);
            if (account == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    if (!account.checkPassword(password))
		    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
