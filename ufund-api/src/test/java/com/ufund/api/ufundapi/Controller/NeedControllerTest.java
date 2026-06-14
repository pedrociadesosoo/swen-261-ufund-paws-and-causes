package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.controller.NeedController;
import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Test the NeedController Class
 * 
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockneedDAO;

    /**
     * Before each test, create a new controller
     * additionally, create a mock DAO
     */

    @BeforeEach
    public void setupNeedController(){
        mockneedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockneedDAO);
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need need = new Need();

        when(mockneedDAO.createNeed(need)).thenReturn(need);

        ResponseEntity<Need> response = needController.createNeed(need);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

}
