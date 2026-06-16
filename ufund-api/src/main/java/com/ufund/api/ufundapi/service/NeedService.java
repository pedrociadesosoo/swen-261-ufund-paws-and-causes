


package com.ufund.api.ufundapi.service;


import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;



public interface NeedService {
    List<Need> getAllNeeds() throws IOException;

    /**
     * Finds and retrieves {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find
     * @return the {@link Need need} of the need if found, otherwise null
     * @throws IOException if an issue with storage access occurs
     */
    Need getNeedById(int id) throws IOException;
    
    Need addNeed(Need need) throws IOException;

    Need updateNeed(int id,Need need) throws IOException;

    /**
     * Deletes {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find and delete
     * @return the {@link Need need} that has been deleted if sucessful, null otherwise
     * @throws IOException if an issue with storage occurs
     */
    Need deleteNeed(int id) throws IOException;
    
}