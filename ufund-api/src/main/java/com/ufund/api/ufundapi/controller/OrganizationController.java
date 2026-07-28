package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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


/**
 * Handles REST requests for {@linkplain Organization organizations}
 * under the /organization resource.
 */
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

    
    /**
     * GET /organization/{name} - retrieves single organization
     * @param name the organization name
     * @return 200 with organization, 404 if doesn't exist, 500 if storage error
     */
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

    @GetMapping("")
    public ResponseEntity<Organization[]> getOrganizationArray(){
        LOG.info("GET /organization");
        try{
            Organization[] orgs = oService.getOrganizationArray();
            return new ResponseEntity<>(orgs, HttpStatus.OK);
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }

    /**
     * POST /organization - Creates the new organization
     * with all of the relevant information carried on from
     * the body parameter
     * @param o the organization to be created
     * @return 201 on creation, 403 on null name, 409 on existing name, 500 on failure
     */
    @PostMapping("")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization o){
        LOG.info("POST /organization");
        try {
            if (o.getName() == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            if (oService.getOrganization(o.getName()) != null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
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

    /**
     * POST /organization/{name} - updates a prexisitng organization,
     * changing the relevant information based on organization name
     * @param o - the organization to be updated
     * @return 403 if null name, 200 on success, 404 on not found, 500 on failure.
     */
    @PostMapping("/{name}")
    public ResponseEntity<Organization> updateOrganization(@RequestBody Organization o, @PathVariable String name){
        LOG.info("POST /organization/" + name);
        try {
            if (o.getName() == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Organization updated = oService.updateOrganization(o, name);
            if (updated != null){
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);         
        }
    }

    /**
     * DELETE /organization/{name} - Deletes an organization
     * @param name - the name of the organization to be deleted
     * @return 403 on null name, 404 on nonexistent, 200 on success, 500 on failure.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Boolean> deleteOrganization(@PathVariable String name){
        LOG.info("DELETE /organization/" + name);
        try{
            if (name == null){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Organization org = oService.getOrganization(name);
            if (org != null) {
                for (int needId : org.getNeeds().keySet()) {
                    needService.deleteNeed(needId);
                }
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

    /**
     * POST /organization/{name}/{id} - Adds a need to an organization
     * @param name - the name of the organization
     * @param id -  the id of the need
     * @return - 404 on null org, 409 on conflict, 200 on success, 500 on failure
     */
    @PostMapping("/{name}/{id}")
    public ResponseEntity<Boolean> addNeed(@PathVariable String name, @PathVariable int id){
        LOG.info("POST /organization/" + name + "/" + id);
        try {
            Organization o = oService.getOrganization(name);
            if (o == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (o.getNeeds().containsKey(id)){
                return new ResponseEntity<>(false,HttpStatus.CONFLICT);
            }

            Need n = needService.getNeedById(id);
            return new ResponseEntity<>(oService.addNeed(o,n), HttpStatus.OK);

        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /organization/{name}/{id} - Deletes a need from an organization
     * @param name - the name of the organization
     * @param id - the id of the need
     * @return - 404 on null org or no present need, 200 on success, 500 on failure.
     */
    @DeleteMapping("/{name}/{id}")
    public ResponseEntity<Boolean> deleteNeed(@PathVariable String name, @PathVariable int id){
        LOG.info("DELETE /organization/" + name + "/" + id);
        try{
            Organization o = oService.getOrganization(name);
            if (o == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (!o.getNeeds().containsKey(id)){
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            Need n = needService.getNeedById(id);
            return new ResponseEntity<>(oService.deleteNeed(o,n), HttpStatus.OK);

        } catch (IOException e ){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
