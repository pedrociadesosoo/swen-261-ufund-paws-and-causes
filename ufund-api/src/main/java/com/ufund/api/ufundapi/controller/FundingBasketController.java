package com.ufund.api.ufundapi.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
     * POST /fundingbasket - creates a basket owned by the caller. The id in
     * the request body is ignored; the persistence tier assigns the real one.
     * The owner is always taken from the X-Username header rather than the
     * request body, so a client can't create a basket on someone else's behalf.
     *
     * @param username the caller's username, from the X-Username header
     * @param fb the basket to create, from the JSON request body
     * @return 201 with the created basket (including assigned id), 403 if no
     * username was supplied, 500 on failure
     */
    @PostMapping("")
    public ResponseEntity<FundingBasket> createFundingBasket(@RequestHeader(value = "X-Username", required = false) String username,
                                                               @RequestBody FundingBasket fb) {
        LOG.info("POST /fundingbasket");
        try {
            if (username == null || username.isBlank())
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            fb.setOwnerUsername(username);
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
     * GET /fundingbasket - retrieves only the baskets owned by the caller
     * (identified by the X-Username header), not every basket in the system.
     * This keeps one helper's basket private from every other helper.
     *
     * @param username the caller's username, from the X-Username header
     * @return 200 with an array of the caller's baskets (empty array if none),
     * 403 if no username was supplied, 500 on storage error
     */
    @GetMapping("")
    public ResponseEntity<FundingBasket[]> getFundingBasketArray(@RequestHeader(value = "X-Username", required = false) String username){
        LOG.info("GET /fundingbasket");
        try{
            if (username == null || username.isBlank())
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            FundingBasket[] baskets = fbService.getFundingBasketsByOwner(username);
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

    /**
     * POST /fundingbasket/{idFB}/checkout - completes a helper's contribution.
     * Every need currently in the basket is considered fully funded: it's
     * removed from the cupboard and cleared out of the basket. Matches the
     * sprint acceptance criteria: an empty basket is refused, checkout only
     * proceeds on a non-empty one, and only the basket's owner can check it out.
     *
     * @param username the caller's username, from the X-Username header
     * @param idFB the basket id to check out
     * @return 200 true on success, 403 if the caller doesn't own the basket,
     * 404 if the basket doesn't exist, 409 if the basket is empty,
     * 500 on storage error
     */
    @PostMapping("/{idFB}/checkout")
    public ResponseEntity<Boolean> checkout(@RequestHeader(value = "X-Username", required = false) String username,
                                             @PathVariable int idFB) {
        LOG.info("POST /fundingbasket/" + idFB + "/checkout");
        try {
            FundingBasket fb = fbService.getFundingBasket(idFB);
            if (fb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (fb.getOwnerUsername() != null && !fb.getOwnerUsername().equals(username)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            List<Need> needsToFund = new ArrayList<>(fb.getNeeds().values());
            if (needsToFund.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            for (Need need : needsToFund) {
                needService.deleteNeed(need.getId());
                fbService.removeNeed(fb, need);
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
