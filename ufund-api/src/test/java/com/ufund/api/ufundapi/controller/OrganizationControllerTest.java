package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Organization;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.OrganizationService;

@Tag("Controller-tier")
public class OrganizationControllerTest {
    private OrganizationController orgCont;
    private OrganizationService orgService;
    private NeedService needService;

    private Need[] sampleNeeds() {
        return new Need[] {
                new Need(1, "Canned Soup", 2.50, 50, "food"),
                new Need(2, "Rice", 1.99, 100, "food"),
                new Need(3, "Blankets", 15.00, 25, "clothing")
        };
    }

    private Organization sampleOrganization(){
        Need[] needs = sampleNeeds();
        Map<Integer, Need> needMap = new HashMap<>();

        for (Need need : needs){
            needMap.put(need.getId(), need);
        }

        return new Organization("General Humanities", 
                                "We're a nonprofit general charity working for a variety of causes",
                                needMap);
    }

    @BeforeEach
    public void setupOrgController(){
        orgService = mock(OrganizationService.class);
        needService = mock(NeedService.class);
        orgCont = new OrganizationController(orgService, needService);
    }

    @Test
    public void testGetOrganization() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(o);

        ResponseEntity<Organization> newOrg = orgCont.getOrganization("General Humanities");
        assertEquals(HttpStatus.OK, newOrg.getStatusCode());
        assertEquals(o.getName(), newOrg.getBody().getName());
        assertEquals(o.getNeeds(), newOrg.getBody().getNeeds());
    }

    @Test
    public void testGetOrganizationFail() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(null);

        ResponseEntity<Organization> newOrg = orgCont.getOrganization("General Humanities");
        assertEquals(HttpStatus.NOT_FOUND, newOrg.getStatusCode());

    }

    @Test
    public void testGetOrganizationHandleException() throws IOException{
        when(orgService.getOrganization("General Humanities")).thenThrow(new IOException("read failed"));

        ResponseEntity<Organization> newOrg = orgCont.getOrganization("General Humanities");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, newOrg.getStatusCode());

    }

    @Test
    public void testCreateOrganization() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.createOrganization(o)).thenReturn(o);

        ResponseEntity<Organization> newOrg = orgCont.createOrganization(o);
        assertEquals(HttpStatus.CREATED, newOrg.getStatusCode());
        assertEquals(o, newOrg.getBody());
    }

    @Test
    public void testCreateOrganizationConflict() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(o);

        ResponseEntity<Organization> newOrg = orgCont.createOrganization(o);
        assertEquals(HttpStatus.CONFLICT, newOrg.getStatusCode());

    }

    @Test
    public void testCreateOrganizationNull(){
        Organization o = sampleOrganization();
        o.setName(null);

        ResponseEntity<Organization> newOrg = orgCont.createOrganization(o);
        assertEquals(HttpStatus.FORBIDDEN, newOrg.getStatusCode());
    }

    @Test
    public void testCreateOrganizationHandleException() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.createOrganization(o)).thenThrow(new IOException("read failed"));

        ResponseEntity<Organization> newOrg = orgCont.createOrganization(o);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, newOrg.getStatusCode());
    }

    @Test
    public void testDeleteOrganization() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.deleteOrganization(o.getName())).thenReturn(true);

        ResponseEntity<Boolean> response = orgCont.deleteOrganization(o.getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteOrganizationNotFound() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.deleteOrganization(o.getName())).thenReturn(false);

        ResponseEntity<Boolean> response = orgCont.deleteOrganization(o.getName());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteOrganizationHandleException() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.deleteOrganization(o.getName())).thenThrow(new IOException("read failed"));

        ResponseEntity<Boolean> response = orgCont.deleteOrganization(o.getName());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddNeed() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(o);

        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");
        when(needService.getNeedById(newNeed.getId())).thenReturn(newNeed);
        
        when(orgService.addNeed(o,newNeed)).thenReturn(true);

        ResponseEntity<Boolean> response = orgCont.addNeed(o.getName(), newNeed.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testAddNeedConflict() throws IOException{

        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");
        when(needService.getNeedById(newNeed.getId())).thenReturn(newNeed);

        Organization o = sampleOrganization();
        o.addNeed(newNeed);
        when(orgService.getOrganization(o.getName())).thenReturn(o);
        when(orgService.addNeed(o,newNeed)).thenReturn(false);

        ResponseEntity<Boolean> response = orgCont.addNeed(o.getName(), newNeed.getId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(false, response.getBody());
        o.removeNeed(newNeed);
    }

    @Test
    public void testAddNeedHandleException() throws IOException{
        
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenThrow(new IOException("read failed"));

        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");
        when(needService.getNeedById(newNeed.getId())).thenReturn(newNeed);

        ResponseEntity<Boolean> response = orgCont.addNeed(o.getName(), newNeed.getId());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(o);

        Need need = o.getNeeds().get(1);
        when(needService.getNeedById(need.getId())).thenReturn(need);
        when(orgService.deleteNeed(o, need)).thenReturn(true);

        ResponseEntity<Boolean> response = orgCont.deleteNeed(o.getName(), need.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenReturn(o);

        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");
        when(needService.getNeedById(newNeed.getId())).thenReturn(newNeed);

        ResponseEntity<Boolean> response = orgCont.deleteNeed(o.getName(), newNeed.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException{
        Organization o = sampleOrganization();
        when(orgService.getOrganization(o.getName())).thenThrow(new IOException("read failed"));

        Need need = o.getNeeds().get(1);
        when(needService.getNeedById(need.getId())).thenReturn(need);

        ResponseEntity<Boolean> response = orgCont.deleteNeed(o.getName(), need.getId());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
