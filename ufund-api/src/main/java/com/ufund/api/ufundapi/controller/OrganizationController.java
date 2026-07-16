package com.ufund.api.ufundapi.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Organization;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.OrganizationService;

@RestController
@RequestMapping("organization")
public class OrganizationController {
    private static final Logger LOG = Logger.getLogger(OrganizationController.class.getName());
    private OrganizationService oService;
    private NeedService needService;

    public OrganizationController(OrganizationService oService, NeedService needService){
        this.oService = oService;
        this.needService = needService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Organization> getOrganization(@PathVariable String name){
        LOG.info("GET /organization/" + name);
        try {
            Organization o = oService.getOrganization(name);
            if (o != null){
                return new ResponseEntity<>(o, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization o){
        LOG.info("POST /organization");
        try {
            if (o.getName() == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Organization newO = oService.createOrganization(o);
            if (newO != null){
                return new ResponseEntity<>(newO, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }

    @PostMapping("/{name}")
    public ResponseEntity<Boolean> updateOrganization(@RequestBody Organization o){
        LOG.info("POST /organization/" + o.getName());
        try {
            if (o.getName() == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            boolean updated = oService.updateOrganization(o);
            if (updated == true){
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Boolean> deleteOrganization(@PathVariable String name){
        LOG.info("DELETE /organization/" + name);
        try{
            if (name == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            boolean updated = oService.deleteOrganization(name);
            if (updated == true){
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{name}/{id}")
    public ResponseEntity<Boolean> addNeed(@PathVariable String name, @PathVariable int id){
        LOG.info("POST /organization/" + name + "/" + id);
        try {
            Organization o = oService.getOrganization(name);
            if (o == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (o.getNeeds().containsKey(id)){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Need n = needService.getNeedById(id);
            return new ResponseEntity<>(oService.addNeed(o,n), HttpStatus.OK);

        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{name}/{id}")
    public ResponseEntity<Boolean> deleteNeed(@PathVariable String name, @PathVariable int id){
        LOG.info("DELETE /organization/" + name + "/" + id);
        try{
            Organization o = oService.getOrganization(name);
            if (o == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (!o.getNeeds().containsKey(id)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Need n = needService.getNeedById(id);
            return new ResponseEntity<>(oService.deleteNeed(o,n), HttpStatus.OK);

        } catch (IOException e ){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
