package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class HelperAccountTest {
    @Test
    public void testConstructor(){
        String username = "user";
        String pass = "pass";
        List<Integer> ids = new ArrayList<>();

        HelperAccount constructed = new HelperAccount(username, pass, ids);

        assertTrue(constructed instanceof HelperAccount);
        assertEquals(username, constructed.getUsername());
        assertEquals(ids, constructed.getBasketIds());
        assertTrue(constructed.checkPassword(pass));
    }

    @Test
    public void testConstructorNullBasket(){
        String username = "user";
        String pass = "pass";
        List<Integer> ids = null;

        HelperAccount constructed = new HelperAccount(username, pass, ids);

        assertTrue(constructed instanceof HelperAccount);
        assertEquals(username, constructed.getUsername());
        assertEquals(new ArrayList<>(), constructed.getBasketIds());
        assertTrue(constructed.checkPassword(pass));
    }
}
