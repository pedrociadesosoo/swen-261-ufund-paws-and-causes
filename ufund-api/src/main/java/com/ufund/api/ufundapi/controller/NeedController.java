package com.ufund.api.ufundapi.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

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
    private NeedService needService;

    /**
     * Creates a REST API controller for responding to requests
     * 
     * @param needService The {@link NeedService Need Service} for CRUD operations
     * This dependency is injected by Spring framework
     */
    public NeedController(NeedService needService) {
        this.needService = needService;
    }

    /**
     * Responds to GET request for a {@linkplain Need need} for the given id
     * 
     * @param id The id used to locate the {@link Need need}
     * @return ResponseEntity with {@link Need need} and HTTP status OK if found,
     * NOT_FOUND if not found, INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
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

    /**
     * Responds to GET request for all {@linkplain Need needs} or searches
     * by partial name if name parameter is provided
     * 
     * @param name Optional search term to filter needs by partial name
     * @return ResponseEntity with array of {@link Need need} objects and HTTP status OK,
     * INTERNAL_SERVER_ERROR otherwise
     */
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

    /**
     * Creates a {@linkplain Need need} with the provided need object
     * 
     * @param need The {@link Need need} to create
     * @return ResponseEntity with created {@link Need need} and HTTP status CREATED,
     * CONFLICT if need with same name exists, INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /needs " + need);
        try {
            Need[] existing = needService.getNeedArray(need.getName());
            if (existing != null && existing.length > 0)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(needService.createNeed(need), HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided need object
     * 
     * @param id The id of the {@link Need need} to update
     * @param need The {@link Need need} to update
     * @return ResponseEntity with updated {@link Need need} and HTTP status OK if updated,
     * NOT_FOUND if not found, INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<Need> updateNeed(@PathVariable int id, @RequestBody Need need) {
        LOG.info("PUT /needs/" + id);
        try {
            Need updated = needService.updateNeed(id, need);
            if (updated != null)
                return new ResponseEntity<>(updated, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need} to delete
     * @return ResponseEntity HTTP status OK if deleted,
     * NOT_FOUND if not found, INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /needs/" + id);
        try {
            boolean deleted = needService.deleteNeed(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}