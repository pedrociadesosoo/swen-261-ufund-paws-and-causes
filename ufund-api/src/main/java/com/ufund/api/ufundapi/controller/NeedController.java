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
import com.ufund.api.ufundapi.service.NeedService;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService needService;
    //remove following when NeedService is ready
    private NeedDAO needDao;
    
    public NeedController (NeedDAO needDao){
        this.needDao = needDao;
    }
    //replace above with following when NeedService is ready
    /*
    public NeedController (NeedService needService){
        this.needService = needService;
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        
        try {
            Need need = needService.getNeedById(id);
            if (need != null) {
                return new ResponseEntity<>(need, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

        LOG.info("DELETE /needs/" + id);
        try { 
            Need deletedNeed = needDao.getNeedById(id);
            if (deletedNeed != null) {
                needDao.deleteNeed(id);
                return new ResponseEntity<Need>(deletedNeed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        /* //Current version uses needDao because NeedService is not ready for the
           //rest of the user stories. When it is, switch to the following version and delete the above.
        LOG.info("DELETE /needs/" + id);
        try { 
            Need deletedNeed = needService.deleteNeed(id);
            if(deletedNeed != null){
                return new ResponseEntity<Need>(deletedNeed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }     
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }

}
