package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    public void testGetNeedFailed() throws Exception {
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

    /************* deleteNeed() unit tests, implemented by Harikleia Sparakis*************/

    /**
     * Tests if needController.deleteNeed() returns HttpStatus.INTERNAL_SERVER_ERROR 
     *  when IOException is thown.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedIOException() throws IOException {
        //setting up test data
        int needTestIndex = 1;
        
        //force exception to be thrown
        doThrow(new IOException()).when(needService).deleteNeed(needTestIndex);

        //check if method returns HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Need> response = needController.deleteNeed(needTestIndex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests if needController.deleteNeed() returns HttpStatus.NOT_FOUND when
     * indicated need does not exist.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedNull() throws IOException {
        //setting up test data
        int needTestId = 1;

        //force needService.deleteNeed() to return null
        when(needService.deleteNeed(1)).thenReturn(null);

        //check if method returns HttpStatus.NOTFOUND
        ResponseEntity<Need> response = needController.deleteNeed(needTestId);
        assertEquals((HttpStatus.NOT_FOUND), response.getStatusCode());   
        
    }

    /**
     * Tests if needController.deleteNeed() returns HttpStatus.OK when indicated
     *  need is found and deleted
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedExists() throws IOException {
        //setting up test data
        Need testNeed = new Need(1, "Corn", 10.97, 100, "food");
        
        //force needService.deleteNeed() to return the test need
        when(needService.deleteNeed(testNeed.getId())).thenReturn(testNeed);
        
        //check if method returns HttpStatus.OK 
        ResponseEntity<Need> response = needController.deleteNeed(testNeed.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
