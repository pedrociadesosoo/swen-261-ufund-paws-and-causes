package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Helper's funding basket: a collection of {@linkplain Need needs}
 * the Helper intends to fund, keyed by need id so duplicates are impossible.
 */
public class FundingBasket {

    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());
    @JsonProperty("id") private int id;
    @JsonProperty("needs") Map<Integer, Need> needs;

    /**
     * Creates a funding basket. Used both by controllers and by Jackson when
     * deserializing from JSON.
     *
     * @param id the basket id (reassigned by the DAO on creation)
     * @param needs the needs in the basket keyed by need id; may be null in
     *              incoming JSON, in which case an empty basket is created
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
     * @return the needs in this basket, keyed by need id
     */
    public Map<Integer, Need> getNeeds(){
        return this.needs;
    }

    /**
     * Puts a need into the basket; replaces any existing entry with the same id.
     * @param need the need to add
     */
    public void addNeed(Need need){
        this.needs.put(need.getId(), need);
    }

    /**
     * Removes a need from the basket if present; no effect otherwise.
     * @param need the need to remove
     */
    public void removeNeed(Need need){
        this.needs.remove(need.getId());
    }

}
