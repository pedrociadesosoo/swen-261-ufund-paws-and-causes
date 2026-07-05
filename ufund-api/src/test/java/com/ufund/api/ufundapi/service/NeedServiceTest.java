package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedDAO;

@Tag("Service-tier")
class NeedServiceTest {

    private NeedServiceImpl service;
    private NeedDAO mockDao;

    @BeforeEach
    void setup() {
        mockDao = mock(NeedDAO.class);
        service = new NeedServiceImpl(mockDao);
    }

    @Test
    void testGetAllNeedsReturnsAll() throws IOException {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, "food"),
            new Need(2, "Rice", 1.99, 100, "food"));
        when(mockDao.getAllNeeds()).thenReturn(needs);

        List<Need> result = service.getAllNeeds();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Canned Soup", result.get(0).getName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals("food", result.get(0).getType());
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
    void testFindNeeds() {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, "food"));
        when(mockDao.findNeeds("Canned")).thenReturn(needs);

        List<Need> result = service.findNeeds("Canned");

        assertEquals(1, result.size());
        assertEquals("Canned Soup", result.get(0).getName());
        verify(mockDao).findNeeds("Canned");
    }

    /**
     * Verifies that findNeeds returns empty list when no needs match the search term
     */
    @Test
    void testFindNeedsNoMatch() {
        when(mockDao.findNeeds("xyz")).thenReturn(Collections.emptyList());

        List<Need> result = service.findNeeds("xyz");

        assertTrue(result.isEmpty());
        verify(mockDao).findNeeds("xyz");
    }

    /**
     * Verifies that findNeeds returns multiple needs when multiple names match.
     */
    @Test
    void testFindNeedsMultipleMatches() {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, "food"),
            new Need(2, "Canned Beans", 1.99, 100, "food"));
        when(mockDao.findNeeds("Canned")).thenReturn(needs);

        List<Need> result = service.findNeeds("Canned");

        assertEquals(2, result.size());
        verify(mockDao).findNeeds("Canned");
    }
}