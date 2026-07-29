package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.dao.ProposalDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Organization;

@Tag("Service-tier")
class NeedServiceTest {

    private NeedServiceImpl service;
    private NeedDAO mockDao;
    private ProposalDAO mockProposalDAO;
    private OrganizationService mockOService;

    @BeforeEach
    void setup() {
        mockDao = mock(NeedDAO.class);
        mockProposalDAO = mock(ProposalDAO.class);
        mockOService = mock(OrganizationService.class);
        service = new NeedServiceImpl(mockDao, mockProposalDAO, mockOService);
    }

    @Test
    void testGetAllNeedsReturnsAll() throws IOException {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"));
        when(mockDao.getAllNeeds()).thenReturn(needs);

        List<Need> result = service.getAllNeeds();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Canned Soup", result.get(0).getName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals(NeedType.ITEM_DONATION, result.get(0).getType());
        verify(mockDao).getAllNeeds();
    }

    @Test
    void testGetAllNeedsWhenEmpty() throws IOException {
        when(mockDao.getAllNeeds()).thenReturn(Collections.emptyList());

        List<Need> result = service.getAllNeeds();

        assertTrue(result.isEmpty());
        verify(mockDao).getAllNeeds();
    }

    /**
     * Verifies that findNeeds returns needs whose name contains the search term.
     */
    @Test
    void testFindNeedsString() throws IOException {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"));
        when(mockDao.findNeeds("Canned")).thenReturn(needs);

        List<Need> result = service.findNeeds("Canned", null, null);

        assertEquals(1, result.size());
        assertEquals("Canned Soup", result.get(0).getName());
        verify(mockDao).findNeeds("Canned");
    }

    /**
     * Verifies that findNeeds returns empty list when no needs match the search term
     */
    @Test
    void testFindNeedsStringNoMatch() throws IOException {
        when(mockDao.findNeeds("xyz")).thenReturn(Collections.emptyList());

        List<Need> result = service.findNeeds("xyz", null, null);

        assertTrue(result.isEmpty());
        verify(mockDao).findNeeds("xyz");
    }

    /**
     * Verifies that findNeeds returns multiple needs when multiple names match.
     */
    @Test
    void testFindNeedsStringMultipleMatches() throws IOException {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Canned Beans", 1.99, 100, NeedType.ITEM_DONATION, "test"));
        when(mockDao.findNeeds("Canned")).thenReturn(needs);

        List<Need> result = service.findNeeds("Canned", null, null);

        assertEquals(2, result.size());
        verify(mockDao).findNeeds("Canned");
    }

    @Test
    void testFindNeedsType() throws IOException {
        //set up test data
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        );
        List<Need> expected = Arrays.asList(
            needs.get(0),
            needs.get(1),
            needs.get(2),
            needs.get(5)
        );

        when(mockDao.findNeedsWithType(NeedType.ITEM_DONATION)).thenReturn(expected);

        List<Need> result = service.findNeeds(null, NeedType.ITEM_DONATION, null);
        verify(mockDao).findNeedsWithType(NeedType.ITEM_DONATION);
        assertEquals(4, result.size());
        for(Need need: result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
        }
    }

    @Test
    void testFindNeedsNeither() throws IOException {
        //set up test data
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        );

        when(mockDao.getAllNeeds()).thenReturn(needs);

        List<Need> result = service.findNeeds(null, null, null);
        verify(mockDao).getAllNeeds();
        assertEquals(6, result.size());       
    }

    @Test
    void testFindNeedsBoth() throws IOException {
        //set up test data
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        );
        
        List<Need> expected = Arrays.asList(
            needs.get(0),
            needs.get(2)
        );

        when(mockDao.findNeeds("a", NeedType.ITEM_DONATION)).thenReturn(expected);

        List<Need> result = service.findNeeds("a", NeedType.ITEM_DONATION, null);
        //verify(this).getAllNeeds();
        assertEquals(2, result.size());       
    }

    @Test
    public void testFindNeedsBothNoMatches() throws IOException {
        //set up test data        
        List<Need> expected = Arrays.asList();

        when(mockDao.findNeeds("a",NeedType.VOLUNTEERING)).thenReturn(expected);

        List<Need> result = service.findNeeds("a", NeedType.VOLUNTEERING, null);
        assertEquals(0, result.size());       
    }

    @Test
    public void testFindNeedsAll() throws IOException {
        service.findNeeds("a", NeedType.VOLUNTEERING, "org");
        verify(mockDao, times(1)).findNeeds("a", NeedType.VOLUNTEERING, "org");
    }   
    
    @Test
    public void testFindNeedsNoName() throws IOException {
        service.findNeeds(null, NeedType.VOLUNTEERING, "org");
        verify(mockDao, times(1)).findNeeds(NeedType.VOLUNTEERING, "org");
    }    

    @Test
    public void testFindNeedsNoType() throws IOException {
        service.findNeeds("a", null, "org");
        verify(mockDao, times(1)).findNeeds("a", "org");
    }  
    
    @Test
    public void testFindNeedsOrg() throws IOException {
        service.findNeeds(null, null, "org");
        verify(mockDao, times(1)).findNeedsWithOrg("org");
    }   

    

    @Test 
    public void testGetNeedById() throws IOException{
        int testId = 1;
        service.getNeedById(testId);
        verify(mockDao).getNeedById(testId);
    }

    @Test 
    public void testGetNeedArrayType() throws IOException{
        service.getNeedArray(NeedType.ITEM_DONATION);
        verify(mockDao).findNeedsWithType(NeedType.ITEM_DONATION);
    }

    @Test 
    public void testGetNeedArrayName() throws IOException{
        service.getNeedArray("test");
        verify(mockDao).findNeeds("test");
    }

    @Test 
    public void testGetNeedArrayNameAndType() throws IOException{
        service.getNeedArray("test", NeedType.ITEM_DONATION);
        verify(mockDao).findNeeds("test", NeedType.ITEM_DONATION);
    }

    /**
     * Tests if NeedService.deleteNeed() returns null when NeedDao.getNeedById() returns
     * null.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedNull() throws IOException {
        //set up test data
        Need testNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        //force mockDao.getNeedById() to return null 
        when(mockDao.getNeedById(testNeed.getId())).thenReturn(null);

        //check if method returns null
        Need deletedNeed = service.deleteNeed(testNeed.getId());
        assertEquals(null, deletedNeed);
    }

    /**
     * Tests if NeedService.deleteNeed() returns the test need when NeedDao.getNeedById()
     * returns null.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedExists() throws IOException {
        //set up test data
        Need testNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        
        //force mockDao.getNeedById() to return test need
        when(mockDao.getNeedById(testNeed.getId())).thenReturn(testNeed);
        when(mockDao.deleteNeed(testNeed.getId())).thenReturn(true);

        //check if method returns deleted need
        Need deletedNeed = service.deleteNeed(testNeed.getId());
        assertEquals(testNeed, deletedNeed);
    }

    @Test
    public void testCreateNeedNullOrg() throws IOException{
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, null);
        when(mockDao.createNeed(newNeed)).thenReturn(newNeed);

        Need created = service.createNeed(newNeed);
        assertEquals(newNeed, created);
    }

    @Test
    public void testCreateBlank() throws IOException{
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "");
        when(mockDao.createNeed(newNeed)).thenReturn(newNeed);
        
        Need created = service.createNeed(newNeed);
        assertEquals(newNeed, created);
    }

    @Test
    public void testCreateNeed() throws IOException{
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        
        when(mockOService.getOrganization(newNeed.getOrganization())).thenReturn(new Organization("test", "test", null));
        when(mockDao.createNeed(newNeed)).thenReturn(newNeed);

        Need created = service.createNeed(newNeed);
        assertEquals(newNeed, created);
    }

    @Test
    public void testUpdateNeed() throws IOException{
        Need oldNeed = new Need(1, "test", 2.50, 50, NeedType.ITEM_DONATION, "test");

        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        when(mockDao.getNeedById(1)).thenReturn(oldNeed);
        when(mockDao.updateNeed(newNeed)).thenReturn(newNeed);

        Need updated = service.updateNeed(newNeed.getId(), newNeed);
        assertEquals(newNeed, updated);
    }

    @Test
    public void testUpdateNeedNull() throws IOException{
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        
        when(mockDao.updateNeed(newNeed)).thenReturn(null);

        Need updated = service.updateNeed(newNeed.getId(), newNeed);
        assertEquals(null, updated);
    }

    @Test
    public void testUpdatedNull() throws IOException{
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        
        when(mockDao.updateNeed(newNeed)).thenReturn(null);

        Need updated = service.updateNeed(newNeed.getId(), newNeed);
        assertEquals(null, updated);
    }

}