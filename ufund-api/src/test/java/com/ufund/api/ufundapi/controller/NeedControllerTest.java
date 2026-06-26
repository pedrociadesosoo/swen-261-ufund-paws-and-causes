package com.ufund.api.ufundapi.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;

@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedService needService;

    @BeforeEach
    public void setupNeedController() {
        needService = mock(NeedService.class);
        needController = new NeedController(needService);
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, "hunger");
        when(needService.createNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.createNeed(need);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test 
    void testCreateNeedConflict() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, "hunger");
        Need other = new Need(666, "corn", 5, 2, "hunger");
        when(needService.getNeedArray("corn")).thenReturn(new Need[] {need});
        ResponseEntity<Need> response = needController.createNeed(other);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(needService, never()).createNeed(any());
    }

    @Test
    void testCreateNeedIOE() throws IOException{        
         Need need = new Need(999, "corn", 10.37, 3, "hunger");
         when(needService.getNeedArray("corn")).thenReturn(new Need[0]);
         when(needService.createNeed(need)).thenThrow(new IOException("IO Failure"));

        ResponseEntity<Need> response = needController.createNeed(need);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(needService, never()).createNeed(any());
    }

    @Test 
    public void testUpdateNeedGetNeedFailed() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, "food");
        when(needService.updateNeed(need.getId(), need)).thenReturn(null);
        ResponseEntity<Need> response = needController.updateNeed(need.getId(), need);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test 
    public void testUpdateNeedHandleException() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, "food");
        doThrow(new IOException()).when(needService).updateNeed(need.getId(), need);
        ResponseEntity<Need> response = needController.updateNeed(need.getId(), need);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws Exception {
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, "food"));
        needs.add(new Need(2, "Blanket", 5.00, 50, "clothing"));
        when(needService.getAllNeeds()).thenReturn(needs);
        ResponseEntity<Need[]> response = needController.getNeeds(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testSearchNeeds() throws Exception {
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, "food"));
        when(needService.findNeeds("Cor")).thenReturn(needs);
        ResponseEntity<Need[]> response = needController.getNeeds("Cor");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void testSearchNeedsHandleException() throws Exception {
        doThrow(new RuntimeException()).when(needService).findNeeds("Cor");
        ResponseEntity<Need[]> response = needController.getNeeds("Cor");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeedsBlankNameReturnsAll() throws Exception {
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, "food"));
        needs.add(new Need(2, "Blanket", 5.00, 50, "clothing"));
        when(needService.getAllNeeds()).thenReturn(needs);
        ResponseEntity<Need[]> response = needController.getNeeds("");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }
    
    /**
     * Tests that getNeed returns HTTP 200 OK and the correct Need body when the service finds it.
     */
    @Test
    public void testGetNeed() throws Exception {
	Need need = new Need(1, "Corn", 10.97, 100, "food");
        when(needService.getNeedById(1)).thenReturn(need);
	
	ResponseEntity<Need> response = needController.getNeed(1);

	assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }
    
    /**
     * Tests that getNeed returns HTTP 404 NOT_FOUND when the service returns null.
     */
    @Test
    public void testGetNeedNotFound() throws Exception {
        when(needService.getNeedById(1)).thenReturn(null);
	
	ResponseEntity<Need> response = needController.getNeed(1);

	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests that getNeed returns HTTP 500 INTERNAL_SERVER_ERROR when the service throws an IOException.
     */
    @Test
    public void testGetNeedIOERR() throws Exception {
        doThrow(new IOException()).when(needService).getNeedById(1);
	
	ResponseEntity<Need> response = needController.getNeed(1);
	
	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
