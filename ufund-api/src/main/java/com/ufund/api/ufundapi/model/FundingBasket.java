package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
public class FundingBasket {

    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());
    @JsonProperty("id") private int id;
    @JsonProperty("needs") private Need[] needs;

    public FundingBasket(@JsonProperty("id") int id, 
                         @JsonProperty("needs") Need[] needs){
        this.id = id;
        this.needs = needs;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Need[] getNeeds(){
        return this.needs;
    }

}
