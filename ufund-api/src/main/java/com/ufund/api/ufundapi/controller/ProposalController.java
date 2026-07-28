package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.ufund.api.ufundapi.model.Account;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.AccountService;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.ProposalService;

@RestController
@RequestMapping("proposals")
public class ProposalController {
    private ProposalService proposalService;
    private NeedService needService;
    private AccountService accountService;

    private static final Logger LOG = Logger.getLogger(ProposalController.class.getName());

    /**
     * Creates a REST API controller for {@linkplain Proposal proposals}.
     *
     * @param proposalService the {@link ProposalService} for CRUD operations.
     */
    public ProposalController(ProposalService proposalService, NeedService needService, AccountService accountService) {
        this.proposalService = proposalService;
        this.needService = needService;
        this.accountService = accountService;
    }

    private boolean isManager(String username) throws IOException {
        if (username == null || username.isBlank())
            return false;
        Account account = accountService.getAccount(username);
        return account instanceof ManagerAccount;
    }
    /**
     * Responds to a GET request for a {@linkplain Proposal proposal} with the given id.
     *
     * @param id the id used to locate the {@link Proposal proposal}
     * @return ResponseEntity with the {@link Proposal proposal}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proposal> getProposal(@PathVariable int id) throws IOException{
        LOG.info("GET /proposal/" + id);
        Proposal proposal = proposalService.getProposalById(id);
        if (proposal != null)
            return new ResponseEntity<>(proposal, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Responds to a GET request for all {@linkplain Proposal proposals}
     *
     * @param name optional search term to filter proposals by partial name
     * @return ResponseEntity with an array of {@link Proposal proposals} 
     */
    @GetMapping("")
    public ResponseEntity<Proposal[]> getProposals(@RequestParam(required = false) String name) {
        LOG.info("GET /proposals");
        List<Proposal> proposals;
        if (name != null && !name.isBlank())
            proposals = proposalService.findProposals(name);
        else
            proposals = proposalService.getAllProposals();
        return new ResponseEntity<>(proposals.toArray(new Proposal[0]), HttpStatus.OK);
    }

    /**
     * Creates a {@linkplain Proposal proposal} from the provided proposal object.
     *
     * @param proposal the {@link Proposal proposal} to create, from the JSON body
     * @return ResponseEntity with the created {@link Proposal proposal} 
     */
    @PostMapping("")
    public ResponseEntity<Proposal> createProposal(@RequestBody Proposal proposal) {
        LOG.info("POST /proposal/");
        try {
            Proposal created = proposalService.createProposal(proposal);
            if (created == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IOException e) {
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
    public ResponseEntity<?> deleteProposal(@RequestHeader(value = "X-Username", required = false) String username,
                                            @PathVariable int id) {
        LOG.info("DELETE /Proposal/" + id);
        try {
            if (isManager(username)) {
                Proposal deletedProposal = proposalService.deleteProposal(id);
                if (deletedProposal != null)
                    return new ResponseEntity<Proposal>(deletedProposal, HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> ApprovalProposal(@RequestHeader(value = "X-Username", required = false) String username, @PathVariable int id) {
        LOG.info("PUT /proposal/" + id + "/approve");
        try {
            if (isManager(username)) {

                Proposal existing = proposalService.getProposalById(id);

                if (existing == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                Proposal approved = proposalService.approveProposal(id);
                approved.setStatus("approved");

                return new ResponseEntity<>(approved, HttpStatus.OK);
            }

              return new ResponseEntity<>(HttpStatus.FORBIDDEN);


        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Unexpected error approving proposal " + id, e);
            return new ResponseEntity<>("Unexpected error approving proposal: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProposal(@RequestHeader(value = "X-Username", required = false) String username, @PathVariable int id,
                                                @RequestBody Proposal updatedProposal) {
        LOG.info("PUT /proposal/" + id + "/save");
        try {
            if (isManager(username)) {

                Proposal existing = proposalService.getProposalById(id);
                if (existing == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                updatedProposal.setId(id);
                Proposal saved = proposalService.updateProposal(updatedProposal);

                return new ResponseEntity<>(saved, HttpStatus.OK);
            }

             return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Unexpected error updating proposal " + id, e);
            return new ResponseEntity<>("Unexpected error updating proposal: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectProposal(@RequestHeader(value = "X-Username", required = false) String username, @PathVariable int id) {
        LOG.info("PUT /proposal/" + id + "/reject");
        try {
            if (isManager(username)) {

                Proposal rejected = proposalService.rejectProposal(id);

                if (rejected == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(rejected, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);


        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Unexpected error rejecting proposal " + id, e);
            return new ResponseEntity<>("Unexpected error rejecting proposal: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Records a vote on the {@linkplain Proposal proposal} with the given id.
     * The voting user and vote value (+1 or -1) come from the request body.
     * Voting again with the same value removes the vote; voting with the
     * opposite value flips it.
     *
     * @param id the id of the {@link Proposal proposal} being voted on
     * @param req the {@link VoteRequest} carrying the username and vote value
     * @return ResponseEntity with the updated {@link Proposal proposal} 
     */
    @PostMapping("/{id}/vote")
    public ResponseEntity<?> voteUpdate(@PathVariable int id, @RequestBody VoteRequest req) {
        LOG.info("PUT /proposal/" + id + "/vote");
        try {
            Proposal updated = proposalService.voteUpdate(id, req.getUsername(), req.getVote());
            if (updated == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
