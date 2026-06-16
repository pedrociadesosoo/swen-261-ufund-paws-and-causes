package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;

/**
 * Unit tests for the API layer of the <em>Get Entire Cupboard</em> story.
 * The {@link NeedService} is mocked so the controller is tested in isolation.
 */
class NeedControllerTest {

    private NeedController controller;
    private NeedService mockService;

    @BeforeEach
    void setup() {
        mockService = mock(NeedService.class);
        controller = new NeedController(mockService);
    }

    /**
     * Acceptance criterion: Given needs exist in the cupboard, when I get the
     * needs, then the API returns the full list and status 200.
     */
    @Test
    void testGetNeedsReturnsFullListAndOk() {
        Need[] needs = {
            new Need(1, "Canned Soup", 50, "cans"),
            new Need(2, "Rice", 100, "lbs")
        };
        when(mockService.getAllNeeds()).thenReturn(Arrays.asList(needs));

        ResponseEntity<Need[]> response = controller.getNeeds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(needs, response.getBody());
        // Value-level checks so the test catches field corruption, not just references.
        Need[] body = response.getBody();
        assertEquals(2, body.length);
        assertEquals(1, body[0].getId());
        assertEquals("Canned Soup", body[0].getName());
        assertEquals(50, body[0].getQuantity());
        assertEquals("cans", body[0].getUnit());
    }

    /**
     * Acceptance criterion: Given no needs exist in the cupboard, when I get the
     * needs, then the API returns an empty array and status 200.
     */
    @Test
    void testGetNeedsReturnsEmptyArrayAndOk() {
        when(mockService.getAllNeeds()).thenReturn(Collections.emptyList());

        ResponseEntity<Need[]> response = controller.getNeeds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }
}
