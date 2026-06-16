package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;



public interface NeedDAO {
    List<Need> getAllNeeds() throws IOException;

    Need getNeedById(int id) throws IOException;
    Need addNeed(Need need) throws IOException;
    
    Need updateNeed(Need need) throws IOException;

    /**
     * Deletes {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find and delete
     * @return true if successful, false if otherwise
     * @throws IOException if an issue with storage access occurs
     */
    boolean deleteNeed(int id) throws IOException;
    
}