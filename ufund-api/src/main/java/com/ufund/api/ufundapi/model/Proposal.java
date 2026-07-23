package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.HashMap;

import java.time.Instant;

public class Proposal{

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    @JsonProperty("username") private String username;
    @JsonProperty("allVotes") private Map<String, Integer> allVotes = new HashMap<>();
    @JsonProperty("organization") private String organization;
    @JsonProperty("creationDate") private String creationDate = Instant.now().toString();
    @JsonProperty("lastEdited") private String lastEdited;
    @JsonProperty("status") private String status;


    public Proposal(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("cost") double cost,
                @JsonProperty("quantity") int quantity,
                @JsonProperty("type") String type,
		@JsonProperty("username") String username,
		@JsonProperty("organization") String organization,
        @JsonProperty("allVotes") Map<String, Integer> allVotes,
        @JsonProperty("status") String status
		) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
	this.username = username;
	this.organization = organization;
    this.allVotes = allVotes;
    this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getUsername() {
    	return username;
    }

    public String getOrganization() {
    	return organization;
    }

    public void setOrganization(String organization) {
    	this.organization = organization;
    }

    public String getLastEdited(){
    	return lastEdited;
    }

    public void setLastEdited(String lastEdited){
    	this.lastEdited = lastEdited;
    }

    public void setAllVotes(Map<String, Integer> allVotes) {
        this.allVotes = allVotes;
    }

    public Map<String, Integer> getAllVotes(){
    	return allVotes;
    }

    public Integer getUserVoteStatus(String user){
    	return allVotes.get(user);
    }

    public void setStatus(String Status) {
        this.status = Status;
    }

    public String getStatus() {
        return this.status;
    }

}
