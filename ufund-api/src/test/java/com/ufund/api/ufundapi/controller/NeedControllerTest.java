package com.ufund.api.ufundapi.controller;

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

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDao;


    @BeforeEach
    public void setupNeedController(){
        mockNeedDao = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDao);
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need need = new Need(999, "corn", 10.37, 3, "hunger");
        when(mockNeedDao.createNeed(need)).thenReturn(need);

        ResponseEntity<Need> response = needController.createNeed(need);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());

    }
}
