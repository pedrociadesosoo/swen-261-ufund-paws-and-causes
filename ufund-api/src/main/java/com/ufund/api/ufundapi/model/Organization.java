package com.ufund.api.ufundapi.model;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Organization {
    private static final Logger LOG = Logger.getLogger(Organization.class.getName());

    @JsonProperty("Name") private String name;
    @JsonProperty("Description") private String description;
    @JsonProperty("Needs") private Map<Integer, Need> needs;

    public Organization(@JsonProperty("Name") String name, 
                        @JsonProperty("Description") String description,
                        @JsonProperty("Needs") Map<Integer, Need> needs){
        this.name = name;
        this.description = description;
        this.needs = needs != null ? needs : new HashMap<>();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDesc(){
        return this.description;
    }

    public void setDesc(String description){
        this.description = description;
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
