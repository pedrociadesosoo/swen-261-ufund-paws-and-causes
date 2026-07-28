package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.dao.ProposalDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Proposal;

@Service
public class NeedServiceImpl implements NeedService {
    private NeedDAO needDao;
    private ProposalDAO proposalDao;
    private OrganizationService orgService;
    
    /**
     * Creates a NeedServiceImpl with the provided {@link NeedDAO}
     *
     * @param needDao The {@link NeedDAO} to use for data access
     */
    public NeedServiceImpl(NeedDAO needDao, ProposalDAO proposalDao, OrganizationService orgService) {
        this.needDao = needDao;
        this.proposalDao = proposalDao;
        this.orgService = orgService;
    }

    /**
     * {@inheritDoc}
     */
    public List<Need> getAllNeeds() throws IOException {
        return needDao.getAllNeeds();
    }

    /**
     * {@inheritDoc}
     */
    public Need getNeedById(int id) throws IOException {
        return needDao.getNeedById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Need createNeed(Need need) throws IOException {
        com.ufund.api.ufundapi.model.Organization org = orgService.getOrganization(need.getOrganization());
        if (org == null) {
            throw new IllegalArgumentException(
                "Organization \"" + need.getOrganization() + "\" does not exist; create it first or fix the spelling before approving.");
        }

        Need created = needDao.createNeed(need);
        orgService.addNeed(org, created);
        return created;
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getNeedArray(String containsText) {
        return needDao.findNeeds(containsText).toArray(new Need[0]);
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getNeedArray(String containsText, NeedType type) {
        return needDao.findNeeds(containsText, type).toArray(new Need[0]);
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getNeedArray(NeedType type) {
        return needDao.findNeedsWithType(type).toArray(new Need[0]);
    }

    /**
     * {@inheritDoc}
     */
    public Need updateNeed(int id, Need need) throws IOException {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    /**
     * {@inheritDoc}
     */
    public Need deleteNeed(int id) throws IOException {
        Need deletedNeed = needDao.getNeedById(id);
        if (deletedNeed != null && needDao.deleteNeed(id)) {
            if (proposalDao != null) {
                for (Proposal proposal : proposalDao.getAllProposals()) {
                        if (proposal.getName().equalsIgnoreCase(deletedNeed.getName()) &&
                            proposal.getCost() == deletedNeed.getCost() &&
                            proposal.getQuantity() == deletedNeed.getQuantity() &&
                            proposal.getType().equalsIgnoreCase(deletedNeed.getType().name())) {
                            proposal.setStatus("rejected");
                            proposalDao.updateProposal(proposal);
                        }
                    }
            }
            return deletedNeed;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Need> findNeeds(String containsText, NeedType type, String org) throws IOException{
        if (org != null) {
            if (containsText != null && type != null) {
                return needDao.findNeeds(containsText, type, org);
            } else {
                if(containsText != null){
                    return needDao.findNeeds(containsText, org);
                } else if (type != null){
                    return needDao.findNeeds(type, org);
                } else {
                    return needDao.findNeedsWithOrg(org);
                }
            }
        } else {
            if (containsText != null && type != null) {
                return needDao.findNeeds(containsText, type);
            } else {
                if(containsText != null){
                    return needDao.findNeeds(containsText);
                } else if (type != null){
                    return needDao.findNeedsWithType(type);
                } else {
                    return getAllNeeds();
                }
            }
        }

        
    }
}