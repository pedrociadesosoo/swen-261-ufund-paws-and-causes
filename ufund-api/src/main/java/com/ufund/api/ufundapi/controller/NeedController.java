package com.ufund.api.ufundapi.controller;

import java.util.List;
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

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService needService;

    /**
     * Creates a REST API controller for responding to requests
     *
     * @param needService The {@link NeedService business service} that the
     * controller delegates to for all Need operations.
     * <br>
     * This dependency is injected by Spring framework
     */
    public NeedController(NeedService needService) {
        this.needService = needService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id){
        //implement here
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs} in the cupboard.
     * <p>
     * Implements the <em>Get Entire Cupboard</em> story.
     *
     * @return ResponseEntity with the array of {@link Need needs} and HTTP
     * status {@code OK}. The array is empty (but never {@code null}) when no
     * needs exist in the cupboard.
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        LOG.info("GET /needs");
        List<Need> needs = needService.getAllNeeds();
        return new ResponseEntity<>(needs.toArray(new Need[0]), HttpStatus.OK);
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
