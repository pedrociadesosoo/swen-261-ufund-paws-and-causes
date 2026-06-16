package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Unit tests for the business layer of the <em>Get Entire Cupboard</em> story.
 * The {@link NeedDAO} is mocked so the service is tested in isolation.
 */
class NeedServiceTest {

    private NeedService service;
    private NeedDAO mockDao;

    @BeforeEach
    void setup() {
        mockDao = mock(NeedDAO.class);
        service = new NeedService(mockDao);
    }

    @Test
    void testGetAllNeedsReturnsAll() {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 50, "cans"),
            new Need(2, "Rice", 100, "lbs"));
        when(mockDao.getAllNeeds()).thenReturn(needs);

        List<Need> result = service.getAllNeeds();

        assertEquals(2, result.size());
        // Value-level checks so the test catches field corruption, not just references.
        assertEquals(1, result.get(0).getId());
        assertEquals("Canned Soup", result.get(0).getName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals("cans", result.get(0).getUnit());
        verify(mockDao).getAllNeeds();
    }

    @Test
    void testGetAllNeedsWhenEmpty() {
        when(mockDao.getAllNeeds()).thenReturn(Collections.emptyList());

        List<Need> result = service.getAllNeeds();

        assertTrue(result.isEmpty());
        verify(mockDao).getAllNeeds();
    }
}
