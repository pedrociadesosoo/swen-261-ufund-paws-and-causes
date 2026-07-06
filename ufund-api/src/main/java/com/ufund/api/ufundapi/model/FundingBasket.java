package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
public class FundingBasket {

    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());
    @JsonProperty("id") private int id;
    @JsonProperty("needs") Map<Integer, Need> needs;

    public FundingBasket(@JsonProperty("id") int id){
        this.id = id;
        this.needs = new HashMap<>();
    }

    public void setMap(@JsonProperty("needs") Map<Integer, Need> needs){
        this.needs = needs;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Map<Integer, Need> getNeeds(){
        return this.needs;
    }

    public void addNeed(Need need){
        this.needs.put(need.getId(), need);
    }

    public void removeNeed(Need need){
        this.needs.remove(need.getId());
    }

}
