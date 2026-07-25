package com.ufund.api.ufundapi.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.OrganizationFileDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Organization;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    
    private OrganizationFileDAO ofDAO;

    public OrganizationServiceImpl(OrganizationFileDAO ofDAO){
        this.ofDAO = ofDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization getOrganization(String name) throws IOException {
        return this.ofDAO.getOrganization(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization[] getOrganizationArray() throws IOException {
        return this.ofDAO.getOrganizationArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization createOrganization(Organization o) throws IOException {
        return this.ofDAO.createOrganization(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization updateOrganization(Organization o, String name) throws IOException {
        return this.ofDAO.updateOrganization(o, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteOrganization(String name) throws IOException {
        return this.ofDAO.deleteOrganization(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNeed(Organization o, Need need) throws IOException {
        return this.ofDAO.addNeed(o, need);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(Organization o, Need need) throws IOException {
        return this.ofDAO.deleteNeed(o, need);
    }

}
