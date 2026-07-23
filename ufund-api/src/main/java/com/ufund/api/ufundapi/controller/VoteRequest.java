package com.ufund.api.ufundapi.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request body for casting a vote on a {@linkplain
 * com.ufund.api.ufundapi.model.Proposal proposal}. Carries the voting user's
 * name and their vote value (+1 or -1). Deserialized from the POST body by
 * Jackson via the annotated constructor.
 */
public class VoteRequest {

    @JsonProperty("username") private String username;
    @JsonProperty("vote") private int vote;

    public VoteRequest(@JsonProperty("username") String username,
                       @JsonProperty("vote") int vote) {
        this.username = username;
        this.vote = vote;
    }

    public String getUsername() {
        return username;
    }

    public int getVote() {
        return vote;
    }
}
