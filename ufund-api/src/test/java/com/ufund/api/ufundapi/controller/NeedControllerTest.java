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

import com.ufund.api.ufundapi.model.HelperAccount;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.service.AccountService;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.OrganizationService;

@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedService needService;
    private OrganizationService orgService;
    private AccountService accountService;

    @BeforeEach
    public void setupNeedController() throws IOException {
        needService = mock(NeedService.class);
        accountService = mock(AccountService.class);
        orgService = mock(OrganizationService.class);
        needController = new NeedController(needService, accountService, orgService);

        when(accountService.getAccount("manager")).thenReturn(new ManagerAccount("manager", "pw"));
        when(accountService.getAccount("helper")).thenReturn(new HelperAccount("helper", "pw", new ArrayList<>()));
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, NeedType.ITEM_DONATION, "test");
        when(needService.createNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.createNeed("manager", need);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    void testCreateNeedConflict() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, NeedType.ITEM_DONATION, "test");
        Need other = new Need(666, "corn", 5, 2, NeedType.ITEM_DONATION, "test");
        when(needService.getNeedArray("corn")).thenReturn(new Need[] {need});
        ResponseEntity<Need> response = needController.createNeed("manager", other);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(needService, never()).createNeed(any());
    }

    @Test
    void testCreateNeedIOE() throws IOException{
         Need need = new Need(999, "corn", 10.37, 3, NeedType.ITEM_DONATION, "test");
         when(needService.getNeedArray("corn")).thenReturn(new Need[0]);
         when(needService.createNeed(need)).thenThrow(new IOException("IO Failure"));

        ResponseEntity<Need> response = needController.createNeed("manager", need);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * A helper account (or a request with no username at all) must not be able to create needs.
     */
    @Test
    void testCreateNeedForbiddenForNonManager() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, NeedType.ITEM_DONATION, "test");
        ResponseEntity<Need> response = needController.createNeed("helper", need);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(needService, never()).createNeed(any());
    }

    @Test
    public void testUpdateNeedGetNeedFailed() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");
        when(needService.updateNeed(need.getId(), need)).thenReturn(null);
        ResponseEntity<Need> response = needController.updateNeed("manager", need.getId(), need);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");
        doThrow(new IOException()).when(needService).updateNeed(need.getId(), need);
        ResponseEntity<Need> response = needController.updateNeed("manager", need.getId(), need);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * A helper account must not be able to update needs.
     */
    @Test
    public void testUpdateNeedForbiddenForNonManager() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");
        ResponseEntity<Need> response = needController.updateNeed("helper", need.getId(), need);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(needService, never()).updateNeed(any(int.class), any());
    }

    /**
     * A manager can delete a need.
     */
    @Test
    public void testDeleteNeed() throws Exception {
        Need need = new Need(3, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");
        when(needService.getNeedById(3)).thenReturn(need);
        when(needService.deleteNeed(3)).thenReturn(need);
        ResponseEntity<Need> response = needController.deleteNeed("manager", 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    /**
     * A helper account must not be able to delete needs.
     */
    @Test
    public void testDeleteNeedForbiddenForNonManager() throws Exception {
        ResponseEntity<Need> response = needController.deleteNeed("helper", 3);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(needService, never()).deleteNeed(any(int.class));
    }

    @Test
    public void testSearchNeeds() throws Exception {
        List<Need> needs = new ArrayList<>();
        needs.add(new Need(1, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test"));
        when(needService.findNeeds("Cor", null, null)).thenReturn(needs);
        ResponseEntity<Need[]> response = needController.getNeeds("Cor", null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void testSearchNeedsHandleException() throws Exception {
        doThrow(new RuntimeException()).when(needService).findNeeds("Cor", null, null);
        ResponseEntity<Need[]> response = needController.getNeeds("Cor", null, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests that getNeed returns HTTP 200 OK and the correct Need body when the service finds it.
     */
    @Test
    public void testGetNeed() throws Exception {
	Need need = new Need(1, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");
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
