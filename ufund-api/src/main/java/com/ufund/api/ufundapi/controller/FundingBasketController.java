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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.service.FundingBasketService;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.model.FundingBasket;

/**
 * Handles REST requests for {@linkplain FundingBasket funding baskets}
 * under the /fundingbasket resource.
 */
@RestController
@RequestMapping("fundingbasket")
public class FundingBasketController {
    private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
    private FundingBasketService fbService;
    private NeedService needService;

    public FundingBasketController(FundingBasketService fbService, NeedService needService){
        this.fbService = fbService;
        this.needService = needService;
    }

    /**
     * GET /fundingbasket/{id} - retrieves a single basket.
     *
     * @param id the basket id
     * @return 200 with the basket, 404 if it doesn't exist, 500 on storage error
     */
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

    /**
     * POST /fundingbasket - creates a basket. The id in the request body is
     * ignored; the persistence tier assigns the real one.
     *
     * @param fb the basket to create, from the JSON request body
     * @return 201 with the created basket (including assigned id), 500 on failure
     */
    @PostMapping("")
    public ResponseEntity<FundingBasket> createFundingBasket(@RequestBody FundingBasket fb) {
        LOG.info("POST /fundingbasket");
        try {
            FundingBasket newBasket = fbService.createFundingBasket(fb);
            if (newBasket != null) {
                return new ResponseEntity<>(newBasket, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /fundingbasket/{id} - deletes a basket.
     *
     * @param id the basket id
     * @return 200 true if deleted, 404 if no such basket, 500 on storage error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteFundingBasket(@PathVariable int id) {
        LOG.info("DELETE /fundingbasket/" + id);
        try {
            boolean deleted = fbService.deleteFundingBasket(id);
            if (deleted) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /fundingbasket - retrieves every basket.
     *
     * @return 200 with an array of all baskets (empty array if none), 500 on storage error
     */
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

    /**
     * POST /fundingbasket/{idFB}/{idNeed} - adds an existing need to a basket.
     * The need is looked up in the cupboard by id, so clients only send ids.
     *
     * @param idFB the basket id
     * @param idNeed the id of the need to add
     * @return 200 true on success, 409 if the need is already in the basket,
     *         404 if the basket or the need doesn't exist, 500 on storage error
     */
    @PostMapping("/{idFB}/{idNeed}")
    public ResponseEntity<Boolean> addNeed(@PathVariable int idFB, @PathVariable int idNeed){
        LOG.info("POST /fundingbasket/" + idFB + "/" + idNeed);
        try {
            FundingBasket fb = fbService.getFundingBasket(idFB);
            if (fb == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(idNeed)){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need need = needService.getNeedById(idNeed);
            if (need == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(fbService.addNeed(fb, need), HttpStatus.OK);
        } catch (IOException e ){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /fundingbasket/{idFB}/{idNeed} - removes a need from a basket.
     *
     * @param idFB the basket id
     * @param idNeed the id of the need to remove
     * @return 200 true on success, 404 if the basket doesn't exist or the
     *         need isn't in it, 500 on storage error
     */
    @DeleteMapping("/{idFB}/{idNeed}")
    public ResponseEntity<Boolean> removeNeed(@PathVariable int idFB, @PathVariable int idNeed){
        LOG.info("DELETE /fundingbasket/" + idFB + "/" + idNeed);
        try{
            FundingBasket fb = fbService.getFundingBasket(idFB);
            if (fb == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
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
