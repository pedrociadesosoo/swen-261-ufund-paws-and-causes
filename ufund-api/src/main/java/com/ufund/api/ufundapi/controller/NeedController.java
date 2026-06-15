package com.ufund.api.ufundapi.controller;

import java.util.logging.Logger;

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
import com.ufund.api.ufundapi.service.NeedService;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService needService;

    public NeedController(NeedService needService) {
        this.needService = needService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        Need need = needService.getNeedById(id);
        if (need != null) {
            return new ResponseEntity<>(need, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

}
