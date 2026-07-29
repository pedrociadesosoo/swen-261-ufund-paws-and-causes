package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests the small request body object used by the vote endpoint.
 */
@Tag("Controller-tier")
class VoteRequestTest {

    @Test
    void testUpvoteRequest() {
        VoteRequest request = new VoteRequest("alice", 1);

        assertEquals("alice", request.getUsername());
        assertEquals(1, request.getVote());
    }

    @Test
    void testDownvoteRequest() {
        VoteRequest request = new VoteRequest("bob", -1);

        assertEquals("bob", request.getUsername());
        assertEquals(-1, request.getVote());
    }
}
