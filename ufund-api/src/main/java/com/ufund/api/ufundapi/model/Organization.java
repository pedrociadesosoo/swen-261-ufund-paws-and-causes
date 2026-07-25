package com.ufund.api.ufundapi.model;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Organization {
    private static final Logger LOG = Logger.getLogger(Organization.class.getName());

    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description;
    @JsonProperty("needs") private Map<Integer, Need> needs;

    public Organization(@JsonProperty("name") String name, 
                        @JsonProperty("description") String description,
                        @JsonProperty("needs") Map<Integer, Need> needs){
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



    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
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

    /**
     * Ignore any unknown properties during deserialization (e.g., legacy 'desc' field)
     */
    @JsonAnySetter
    public void ignoreUnknownProperty(String name, Object value) {
        // Do nothing - silently ignore unknown fields
    }

}
