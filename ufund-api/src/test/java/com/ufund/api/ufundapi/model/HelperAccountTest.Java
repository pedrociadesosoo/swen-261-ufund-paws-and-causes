package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@Tag("Model-Tier")
class HelperAccountTest { 
    @Test
    testConstructor(){
        public void testConstructor() {
            // Setup test data
            string username = "username";
            string password = "password";

            // Invoke
            HelperAccount helper = new HelperAccount(username, password);

            // Analyze
            assertEquals(username, helper.username);
            assertEquals(password, helper.password);
        }   
    }
}