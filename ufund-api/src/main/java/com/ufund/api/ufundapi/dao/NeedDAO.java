package com.ufund.api.ufundapi.dao;

import java.util.List;
import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;



public interface NeedDAO {
    List<Need> getAllNeeds();

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

    boolean deleteNeed(int id) throws IOException;
    
}
