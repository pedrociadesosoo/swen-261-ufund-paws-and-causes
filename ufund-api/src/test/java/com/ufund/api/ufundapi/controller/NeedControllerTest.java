package com.ufund.api.ufundapi.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedService needService;

        /**
     * Before each test, create a new NeedController object and inject
     * a mock Need Service
     */
    @BeforeEach
    public void setupNeedController() {
        needService = mock(NeedService.class);
        needController = new NeedController(needService);
    }

    @Test 
    public void testUpdateNeed() throws Exception{  // getNeed may throw IOException
        Need need = new Need(1, "Corn", 10.97, 100, "food");
        when(needService.updateNeed(need.getId(), need)).thenReturn(need);

        ResponseEntity<Need> response = needController.updateNeed(need.getId(), need);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test 
    public void testGetNeedFailed() throws Exception {  // getNeed may throw IOException
        // Setup
        Need need = new Need(3, "Corn", 10.97, 100, "food");


        // when updateNeed is called, return null simulating failed
        // update and save
        when(needService.updateNeed(need.getId(), need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need.getId(), need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test void testUpdateNeedHandleException() throws Exception {
        // Setup
        Need need = new Need(3, "Corn", 10.97, 100, "food");

        // When updateNeed is called on the Mock Need Service, throw an IOException
        doThrow(new IOException()).when(needService).updateNeed(need.getId(), need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need.getId(), need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws Exception {
        // Setup
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, "food"));
        needs.add(new Need(2, "Blanket", 5.00, 50, "clothing"));
        when(needService.getAllNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds(null);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testSearchNeeds() throws Exception {
        // Setup
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, "food"));
        when(needService.findNeeds("Cor")).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds("Cor");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void testSearchNeedsHandleException() throws Exception {
        // Setup
        doThrow(new RuntimeException()).when(needService).findNeeds("Cor");

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds("Cor");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}