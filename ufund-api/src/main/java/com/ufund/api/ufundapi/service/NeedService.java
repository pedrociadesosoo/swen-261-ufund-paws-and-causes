


package com.ufund.api.ufundapi.service;


import java.util.List;
import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;



public interface NeedService {
    List<Need> getAllNeeds();

    Need getNeedById(int id);
    Need createNeed(Need need) throws IOException;

    Need updateNeed(int id,Need need) throws IOException;

    boolean deleteNeed(int id) throws IOException;
    
}
