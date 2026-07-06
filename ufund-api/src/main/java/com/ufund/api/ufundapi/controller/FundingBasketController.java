package com.ufund.api.ufundapi.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.service.FundingBasketService;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.model.FundingBasket;

@RestController
@RequestMapping("fundingbasket")
public class FundingBasketController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private FundingBasketService fbService;
    private NeedService needService;

    public FundingBasketController(FundingBasketService fbService, NeedService needService){
        this.fbService = fbService;
        this.needService = needService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingBasket> getFundingBasket(@PathVariable int id){
        LOG.info("GET /fundingbasket/" + id);
        try{
            FundingBasket fb = fbService.getFundingBasket(id);
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

    @GetMapping("")
    public ResponseEntity<FundingBasket[]> getFundingBasketArray(){
        LOG.info("GET /fundingbasket");
        try{
            FundingBasket[] baskets = fbService.getFundingBasketArray();
            return new ResponseEntity<>(baskets, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{idFB}/{idNeed}")
    public ResponseEntity<Boolean> addNeed(@PathVariable int idFB, @PathVariable int idNeed){
        LOG.info("POST /fundingbasket/" + idFB + "/" + idNeed);
        try {
            FundingBasket fb = fbService.getFundingBasket(idFB);
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(idNeed)){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need need = needService.getNeedById(idNeed);
            return new ResponseEntity<>(fbService.addNeed(fb, need), HttpStatus.OK);
        } catch (IOException e ){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{idFB}/{idNeed}")
    public ResponseEntity<Boolean> removeNeed(@PathVariable int idFB, @PathVariable int idNeed){
        LOG.info("DELETE /fundingbasket/" + idFB + "/" + idNeed);
        try{
            FundingBasket fb = fbService.getFundingBasket(idFB);
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(idNeed)){
                Need need = needService.getNeedById(idNeed);
                return new ResponseEntity<>(fbService.removeNeed(fb, need), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
