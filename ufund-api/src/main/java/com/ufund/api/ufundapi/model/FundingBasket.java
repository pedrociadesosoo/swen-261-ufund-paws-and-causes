package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Helper's funding basket which is a collection of {@linkplain Need needs}
 * the Helper intends to fund, keyed by need id so duplicates are impossible.
 */
public class FundingBasket {

    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());
    @JsonProperty("id") private int id;
    @JsonProperty("needs") Map<Integer, Need> needs;
    @JsonProperty("ownerUsername") private String ownerUsername;

    /**
     * Creates a funding basket. Used both by controllers
     *
     * @param id the basket id 
     * @param needs the needs in the basket keyed by need id
     */
    public FundingBasket(@JsonProperty("id") int id,
                         @JsonProperty("needs") Map<Integer, Need> needs){
        this.id = id;
        this.needs = needs != null ? needs : new HashMap<>();
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    /**
     * @return the username of the helper who owns this basket, or null if unset
     */
    public String getOwnerUsername(){
        return this.ownerUsername;
    }

    /**
     * @param ownerUsername the username of the owning helper
     */
    public void setOwnerUsername(String ownerUsername){
        this.ownerUsername = ownerUsername;
    }

    /**
     * @return the needs in this basket
     */
    public Map<Integer, Need> getNeeds(){
        return this.needs;
    }

    /**
     * Puts a need into the basket
     * @param need the need to add
     */
    public void addNeed(Need need){
        this.needs.put(need.getId(), need);
    }

    /**
     * Removes a need from the basket if present
     * @param need the need to remove
     */
    public void removeNeed(Need need){
        this.needs.remove(need.getId());
    }

}
