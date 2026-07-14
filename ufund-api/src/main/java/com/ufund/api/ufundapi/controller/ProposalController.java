package com.ufund.api.ufundapi.controller;

import java.util.List;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.service.ProposalService;
import com.ufund.api.ufundapi.model.Proposal;

@RestController
@RequestMapping("proposals")
public class ProposalController {
    private ProposalService proposalService;

    /**
     * Creates a REST API controller for {@linkplain Proposal proposals}.
     *
     * @param proposalService the {@link ProposalService} for CRUD operations.
     * This dependency is injected by the Spring framework.
     */
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    /**
     * Responds to a GET request for a {@linkplain Proposal proposal} with the given id.
     *
     * @param id the id used to locate the {@link Proposal proposal}
     * @return ResponseEntity with the {@link Proposal proposal} and HTTP status OK
     * if found, NOT_FOUND if no proposal has that id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proposal> getProposal(@PathVariable int id) {
        Proposal proposal = proposalService.getProposalById(id);
        if (proposal != null)
            return new ResponseEntity<>(proposal, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Responds to a GET request for all {@linkplain Proposal proposals}, or
     * searches by partial name if the name parameter is provided.
     *
     * @param name optional search term to filter proposals by partial name
     * @return ResponseEntity with an array of {@link Proposal proposals} and HTTP
     * status OK
     */
    @GetMapping("")
    public ResponseEntity<Proposal[]> getProposals(@RequestParam(required = false) String name) {
        List<Proposal> proposals;
        if (name != null && !name.isBlank())
            proposals = proposalService.findProposals(name);
        else
            proposals = proposalService.getAllProposals();
        return new ResponseEntity<>(proposals.toArray(new Proposal[0]), HttpStatus.OK);
    }

    /**
     * Creates a {@linkplain Proposal proposal} from the provided proposal object.
     * The owning username is taken from the request body, and the id is assigned
     * by the persistence tier (any id in the body is ignored).
     *
     * @param proposal the {@link Proposal proposal} to create, from the JSON body
     * @return ResponseEntity with the created {@link Proposal proposal} and HTTP
     * status CREATED, CONFLICT if the user already has a pending proposal,
     * INTERNAL_SERVER_ERROR on a storage failure
     */
    @PostMapping("")
    public ResponseEntity<Proposal> createProposal(@RequestBody Proposal proposal) {
        try {
            Proposal created = proposalService.createProposal(proposal);
            if (created == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
