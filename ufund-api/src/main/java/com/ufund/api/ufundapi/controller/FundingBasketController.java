package com.ufund.api.ufundapi.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.ufund.api.ufundapi.dao.FundingBasketDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.model.FundingBasket;

@RestController
@RequestMapping("fundingbasket")
public class FundingBasketController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private FundingBasketDAO fbDao;
    private NeedService needService;

    public FundingBasketController(FundingBasketDAO fbDao, NeedService needService){
        this.fbDao = fbDao;
        this.needService = needService;
    }

    @PostMapping("")
    public ResponseEntity<FundingBasket> createFundingBasket(@PathVariable int id){
        LOG.info("GET /fundingbasket/" + id);
        try{
            FundingBasket[] fbArray = fbDao.getFundingBasketArray();
            for (FundingBasket afb : fbArray){
                if (afb.getId() == id){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity<>(fbDao.createFundingBasket(id), HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteFundingBasket(int id){
        LOG.info("DELETE /fundngbasket/" + id);
        try{
            FundingBasket[] fbArray = fbDao.getFundingBasketArray();
            for (FundingBasket afb : fbArray){
                if (afb.getId() == id){
                    return new ResponseEntity<>(fbDao.deleteFundingBasket(id), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  
        }    
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingBasket> getFundingBasket(@PathVariable int id){
        LOG.info("GET /fundingbasket/" + id);
        try{
            FundingBasket fb = fbDao.getFundingBasket(id);
            if (fb != null){
                return new ResponseEntity<>(fb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<Integer, Need>> getNeeds(@PathVariable int id){
        LOG.info("GET /fundingbasket/" + id + "/needs");
        try{
            FundingBasket fb = fbDao.getFundingBasket(id);
            if (fb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Map<Integer, Need> needs = fb.getNeeds();
            if(needs.isEmpty() == false){
                return new ResponseEntity<>(needs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Need> addNeed(@PathVariable int idFB, @PathVariable Need need){
        LOG.info("POST /fundingbasket/" + idFB + "/" + need.getId());
        try {
            FundingBasket fb = fbDao.getFundingBasket(idFB);
            if (fb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(fbDao.addNeed(fb, need), HttpStatus.OK);
        } catch (IOException e ){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Need> removeNeed(@PathVariable int idFB, @PathVariable Need need){
        LOG.info("DELETE /fundingbasket/" + idFB + "/" + need.getId());
        try{
            FundingBasket fb = fbDao.getFundingBasket(idFB);
            if (fb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                return new ResponseEntity<>(fbDao.removeNeed(fb, need), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
