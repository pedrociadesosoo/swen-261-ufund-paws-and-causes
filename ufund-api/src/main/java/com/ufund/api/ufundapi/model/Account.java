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
    @JsonProperty("password") private String password;
    public Account(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
	this.password = password;
    }

    public String getUsername() { return this.username; }
    public boolean  checkPassword(String password) { return password != null && password.equals(this.password); }
}
