package com.ufund.api.ufundapi.dao;

import java.util.List;
import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;



public interface NeedDAO {
    List<Need> getAllNeeds();
    List<Need> findNeeds(String containsText);
    

    Need getNeedById(int id);
    Need addNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;

    boolean deleteNeed(int id) throws IOException;
    
}
