package com.ufund.api.ufundapi.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Proposal{

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;
    @JsonProperty("username") private String username;
    @JsonProperty("votes") private Map<String, Integer> votes = new HashMap<>();
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
        @JsonProperty("votes") Map<String, Integer> votes,
        @JsonProperty("status") String status
		) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
	this.username = username;
	this.organization = organization;
    this.votes = votes;
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

    public String getCreationDate(){
    	return creationDate;
    }

    public void setCreationDate(String creationDate){
    	this.creationDate = creationDate;
    }

    public String getLastEdited(){
    	return lastEdited;
    }

    public void setLastEdited(String lastEdited){
    	this.lastEdited = lastEdited;
    }

    @JsonIgnore
    public void setAllVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    @JsonIgnore
    public Map<String, Integer> getAllVotes(){
    	return votes;
    }

    public Integer getUserVoteStatus(String user){
    	return votes.get(user);
    }

    public void setStatus(String Status) {
        this.status = Status;
    }

    public String getStatus() {
        return this.status;
    }

}
