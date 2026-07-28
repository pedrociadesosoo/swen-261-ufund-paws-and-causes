package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.ProposalService;

@Component
public class ProposalFileDAO implements ProposalDAO {
	private Map<Integer, Proposal> proposals = new TreeMap<>();
	private ObjectMapper objectMapper;
	private int nextId;
	private String filename;

	public ProposalFileDAO(@Value("${proposals.file}") String filename, ObjectMapper objectMapper) throws IOException {
		this.filename = filename;
		this.objectMapper = objectMapper;
		load();
	}

	private synchronized int nextId(){
		int id = nextId;
		++nextId;
		return id;
	}

	@Override
	public List<Proposal> getAllProposals(){
		synchronized (proposals) {
			return new ArrayList<>(proposals.values());
		}
	}

	@Override
	public Proposal getProposalById(int id){
		synchronized (proposals) {
			if (proposals.containsKey(id)) {
				return proposals.get(id);
			}
			return null;
		}
	}

	@Override
	public List<Proposal> findProposals(String containsText) {
		synchronized (proposals) {
			List<Proposal> matches = new ArrayList<>();
			for (Proposal proposal : proposals.values()) {
				if (proposal.getName().toLowerCase().contains(containsText.toLowerCase()))
					matches.add(proposal);
			}
			return matches;
		}
	}

	private boolean save() throws IOException {
		objectMapper.writeValue(new File(filename), getAllProposals());
		return true;
	}

	private boolean load() throws IOException {
		proposals = new TreeMap<>();
		nextId = 0;

		Proposal[] proposalArray = objectMapper.readValue(new File(filename), Proposal[].class);

		for (Proposal proposal : proposalArray) {
			proposals.put(proposal.getId(), proposal);
			if (proposal.getId() >= nextId) nextId = proposal.getId();
		}
		++nextId;
		return true;
	}

	@Override
	public Proposal createProposal(Proposal proposal) throws IOException {
		synchronized (proposals) {
			for (Proposal existing : proposals.values()) {
				if (existing.getUsername().equals(proposal.getUsername()) &&
					"pending".equals(existing.getStatus()))
					return null;
			}

			proposal.setId(nextId());
			proposals.put(proposal.getId(), proposal);
			save();
			return proposal;
		}
	}


	@Override
	public Proposal updateProposal(Proposal proposal) throws IOException {
		synchronized (proposals) {
			Proposal existing = proposals.get(proposal.getId());
			if (existing == null) {
				return null; 
			}
			
			existing.setName(proposal.getName());
			existing.setCost(proposal.getCost());
			existing.setQuantity(proposal.getQuantity());
			existing.setType(proposal.getType());
			existing.setStatus(proposal.getStatus());
			existing.setOrganization(proposal.getOrganization());


			save();
			return existing;
		}
	}


	@Override
	public boolean deleteProposal(int id) throws IOException {
		synchronized (proposals) {
			if (proposals.containsKey(id)) {
				proposals.remove(id);
				return save();
			}
			else
				return false;
		}
	}
}
