package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HelperAccount extends Account {
    @JsonProperty("basketIds") private List<Integer> basketIds;

    public HelperAccount(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("basketIds") List<Integer> basketIds) {
        super(username, password);
        this.basketIds = basketIds != null ? basketIds : new ArrayList<>();
    }

    public List<Integer> getBasketIds() { return basketIds; }
}
