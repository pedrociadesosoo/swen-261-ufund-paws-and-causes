package com.ufund.api.ufundapi.controller;

import java.util.List;
import java.util.logging.Level;
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

import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.model.Need;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService  needService;

    /**
     * Creates a REST API controller for responding to requests
     * 
     * @param needservice The {@link NeedService Need link Service} for CRUD operations
     * <br>
     * This dependency is injected by Spring framework
     */

    public NeedController (NeedService needService2){
        this.needService = needService2;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
        LOG.info("GET /needs/" + id);
        try {
            Need need = needService.getNeedById(id); 
            if (need != null)
                return new ResponseEntity<>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(@RequestParam(required = false) String name) {
        LOG.info("GET /needs" + (name != null ? "?name=" + name : ""));
        try {
            List<Need> needs;
            if (name != null)
                needs = needService.findNeeds(name);
            else
                needs = needService.getAllNeeds();
            return new ResponseEntity<>(needs.toArray(new Need[0]), HttpStatus.OK);
        }
        catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need){
        return null;

    }

    @PutMapping("/{id}")
    public ResponseEntity<Need> updateNeed(@PathVariable int id, @RequestBody Need need){
        LOG.info("PUT /needs " + need);

        try {
            Need updated = needService.updateNeed(id, need);
            if (updated != null)
                return new ResponseEntity<Need>(updated, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id){
        return null;
    }

}
