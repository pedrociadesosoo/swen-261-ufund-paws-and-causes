package com.ufund.api.ufundapi.dao;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Organization;

public interface OrganizationDAO {

    /**
     * Retrieves the {@linkplain Organization organization} with the same name.
     * 
     * @param name the name of the organization
     * @return the organization with the matching name, or null if nonexistent
     * @throws IOException if an issue with storage occurs
     */
    Organization getOrganization(String name) throws IOException;

    /**
     * Retrieves all {@linkplain Organization organizations}.
     * 
     * @return an array of every stored organization, empty if there is none
     * @throws IOException if an issue with storage occurs.
     */
    Organization[] getOrganizationArray() throws IOException;

    /**
     * Creates a new {@linkplain Organization organization}.
     * 
     * @param o the organization to be created
     * @return the created organization, null on failure/taken name
     * @throws IOException if an issue with storage occurs
     */
    Organization createOrganization(Organization o) throws IOException;

    /**
     * Deletes the {@linkplain Organization organization}.
     * 
     * @param o the organization to be deleted
     * @return true on success, false on failure
     * @throws IOException if an issue with storage occurs.
     */
    boolean deleteOrganizaiton(Organization o) throws IOException;


    /**
     * Adds a {@linkplain Need need} to a organization and persists the change.
     *
     * @param o the organization to add to.
     * @param need the need to add
     * @return true if added, false if the need was already in the organization
     * @throws IOException if an issue with storage occurs
     */
    boolean addNeed(Organization o, Need need) throws IOException;

     /**
     * Removes a {@linkplain Need need} from a organization and persists the change.
     *
     * @param o the organization to remove from
     * @param need the need to remove
     * @return true if removed, false if the need was not in the organization
     * @throws IOException if an issue with storage occurs
     */
    boolean deleteNeed(Organization o, Need need) throws IOException;

}
