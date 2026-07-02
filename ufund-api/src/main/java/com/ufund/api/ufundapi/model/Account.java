package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HelperAccount.class, name = "helper"),
    @JsonSubTypes.Type(value = ManagerAccount.class, name = "admin")
})
public abstract class Account {
    @JsonProperty("username") private String username;

    public Account(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}
