package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Organization;

@Component
public class OrganizationFileDAO implements OrganizationDAO{

    private static final Logger LOG = Logger.getLogger(OrganizationFileDAO.class.getName());

    private Map<String, Organization> organizations = new TreeMap<>();
    private ObjectMapper objectMapper;
    private String filename;

    public OrganizationFileDAO(@Value("${organizations.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Serializes the in-memory baskets to the JSON file.
     *
     * @return true if written successfully
     * @throws IOException if the file cannot be written
     */
    private boolean save() throws IOException{
        Organization[] organizations = getOrganizationArray();
        objectMapper.writeValue(new File(filename), organizations);
        return true;
    }

    /**
     * Loads all baskets from the JSON file into memory. Runs once at
     * startup; the file must exist and hold a JSON array (may be empty).
     *
     * @return true if loaded successfully
     * @throws IOException if the file cannot be read or parsed
     */
    private boolean load() throws IOException{
        organizations = new TreeMap<>();

        Organization[] orgArray = objectMapper.readValue(new File(filename), Organization[].class);

        for (Organization o : orgArray) {
            organizations.put(o.getName(), o);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization getOrganization(String name) throws IOException {
        synchronized(organizations){
            if (organizations.containsKey(name)){
                return organizations.get(name);
            } else {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization[] getOrganizationArray() throws IOException {
        synchronized(organizations){
            ArrayList<Organization> orgList = new ArrayList<>();
            for (Organization o: organizations.values()){
                orgList.add(o);
            }
            Organization[] orgArray = new Organization[organizations.size()];
            orgList.toArray(orgArray);
            return orgArray;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization createOrganization(Organization o) throws IOException {
        synchronized(organizations){
            try{
                for(Organization x : organizations.values()){
                    if (x.getName() == o.getName()){
                        return null;
                    }
                }
                organizations.put(o.getName(), o);
                save();
                return o;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization updateOrganization(Organization o, String name) throws IOException {
        synchronized(organizations){
            try{
                if (organizations.containsKey(name)){
                    organizations.remove(name);
                    organizations.put(o.getName(), o);
                    save();
                    return o;
                }
                return null;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteOrganization(String name) throws IOException {
        synchronized(organizations){
            try{
                if (organizations.containsKey(name)){
                    organizations.remove(name);
                    save();
                    return true;
                }
                return false;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNeed(Organization o, Need need) throws IOException {
        synchronized(organizations){
            try{
                Map<Integer,Need> needs = o.getNeeds();
                if (needs.containsKey(need.getId())){
                    return false;
                }
                o.addNeed(need);
                save();
                return true;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(Organization o, Need need) throws IOException {
        synchronized(organizations){
            try{
                Map<Integer,Need> needs = o.getNeeds();
                if (!needs.containsKey(need.getId())){
                    return false;
                }
                o.removeNeed(need);
                save();
                return true;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return false;
            }
        }
    }

    

}
