package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedDAO needDao;

    /**
     * Creates a REST API controller for responding to requests
     * 
     * @param needdao The {@link NeedDAO Need link Data Access Object} for CRUD operations
     * <br>
     * This dependency is injected by Spring framework
     */

    public NeedController (NeedDAO needDao){
        this.needDao = needDao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){

    }

    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){

    }

    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name){

    }

    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need){

    }

    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need){

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
        
    }

}
