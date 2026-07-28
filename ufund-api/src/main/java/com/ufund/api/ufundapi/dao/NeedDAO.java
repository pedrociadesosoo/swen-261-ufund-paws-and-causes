package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;



public interface NeedDAO {
    List<Need> getAllNeeds();
    List<Need> findNeeds(String containsText);
    List<Need> findNeeds(String containsText, NeedType type);
    List<Need> findNeedsWithType(NeedType type);
    List<Need> findNeeds(String containsText, NeedType type, String org);
    List<Need> findNeeds(NeedType type, String org);
    List<Need> findNeeds(String containsText, String org);
    List<Need> findNeedsWithOrg(String org);

    Need getNeedById(int id);
    Need[] getNeedArray(String name) throws IOException;
    Need[] getNeedArray();

    /**
     * Creates a new {@linkplain Need need}
     * @param need {@link Need need} the need to be created
     * @return new {@link Need need} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    Need createNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;


    /**
     * Deletes {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find and delete
     * @return true if successful, false if otherwise
     * @throws IOException if an issue with storage access occurs
     */
    boolean deleteNeed(int id) throws IOException;
    
    
    
}
