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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.service.AccountService;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.service.OrganizationService;

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService needService;
    private AccountService accountService;
    private OrganizationService organizationService;

    /**
     * Creates a REST API controller for responding to requests
     *
     * @param needService The {@link NeedService Need Service} for CRUD operations
     * @param accountService The {@link AccountService Account Service}, used to verify
     * that the caller is a manager before allowing needs to be created, edited, or deleted
     * This dependency is injected by Spring framework
     */
    public NeedController(NeedService needService, AccountService accountService, OrganizationService organizationService) {
        this.needService = needService;
        this.accountService = accountService;
        this.organizationService = organizationService;

    }

    /**
     * Checks whether the given username belongs to a manager/admin account.
     * The frontend sends this as the X-Username header on every request so the
     * API can enforce the same helper/manager separation as the UI, even for
     * clients that bypass the UI entirely.
     *
     * @param username the username from the X-Username request header, may be null
     * @return true only if the username belongs to a {@link ManagerAccount}
     * @throws IOException if the account storage can't be read
     */
    private boolean isManager(String username) throws IOException {
        if (username == null || username.isBlank())
            return false;
        Account account = accountService.getAccount(username);
        return account instanceof ManagerAccount;
    }

    /**
     * Responds to GET request for a {@linkplain Need need} for the given id
     *
     * @param id The id used to locate the {@link Need need}
     * @return ResponseEntity with {@link Need need} 
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
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to GET request for all {@linkplain Need needs} or searches
     * by partial name if name parameter is provided and by need type if
     * type parameter is provided
     * 
     * @param name Optional search term to filter needs by partial name
     * @param type type of need to filter with
     * @return ResponseEntity with array of {@link Need need} objects 
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) NeedType type,
        @RequestParam(required = false) String org
    ) {
        if (org != null) {
            org.replaceAll("_", " ");
        }

        LOG.info("GET /needs" + (name != null ? "?name=" + name : "") +
            (type != null ? "&type=" + type : "") +
            (org != null ? "&org=" + org : "")
        );

        try {
            List<Need> needs = needService.findNeeds(name, type, org);         
            return new ResponseEntity<>(needs.toArray(new Need[0]), HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Need need} with the provided need object.
     * Manager-only: requires the X-Username header to belong to a manager account.
     *
     * @param username the caller's username, from the X-Username header
     * @param need The {@link Need need} to create
     * @return ResponseEntity with created {@link Need need} and HTTP status CREATED,
     * FORBIDDEN if the caller isn't a manager, CONFLICT if need with same name exists,
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestHeader(value = "X-Username", required = false) String username,
                                            @RequestBody Need need) {
        LOG.info("POST /needs " + need);
        try {
            if (!isManager(username))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            Need[] existing = needService.getNeedArray(need.getName());
            if (existing != null && existing.length > 0)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            Need created = needService.createNeed(need);
            organizationService.addNeed(organizationService.getOrganization(created.getOrganization()), created);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided need object.
     * Manager-only: requires the X-Username header to belong to a manager account.
     *
     * @param username the caller's username, from the X-Username header
     * @param id The id of the {@link Need need} to update
     * @param need The {@link Need need} to update
     * @return ResponseEntity with updated {@link Need need} and HTTP status OK if updated,
     * FORBIDDEN if the caller isn't a manager, NOT_FOUND if not found, INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<Need> updateNeed(@RequestHeader(value = "X-Username", required = false) String username,
                                            @PathVariable int id, @RequestBody Need need) {
        LOG.info("PUT /needs/" + id);
        try {
            if (!isManager(username))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            
            Need existing = needService.getNeedById(id);
            Need updated = needService.updateNeed(id, need);
            if (updated != null){
                if (!existing.getOrganization().equals(updated.getOrganization())) {
                    organizationService.addNeed(organizationService.getOrganization(updated.getOrganization()), updated);
                    organizationService.deleteNeed(organizationService.getOrganization(existing.getOrganization()), existing);
                }
                return new ResponseEntity<>(updated, HttpStatus.OK);
            }else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id.
     * Manager-only: requires the X-Username header to belong to a manager account.
     *
     * @param username the caller's username, from the X-Username header
     * @param id The id of the {@link Need need} to delete
     * @return ResponseEntity HTTP status OK if deleted, FORBIDDEN if the caller isn't a
     * manager, NOT_FOUND if not found, INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@RequestHeader(value = "X-Username", required = false) String username,
                                            @PathVariable int id) {
        LOG.info("DELETE /needs/" + id);
        try {
            if (!isManager(username))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            Need existing = needService.getNeedById(id);
            organizationService.deleteNeed(organizationService.getOrganization(existing.getOrganization()), existing);
            Need deletedNeed = needService.deleteNeed(id);
            if (deletedNeed != null)
                return new ResponseEntity<Need>(deletedNeed, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}