package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagerAccount extends Account {
    public ManagerAccount(@JsonProperty("username") String username) {
        super(username);
    }
}
