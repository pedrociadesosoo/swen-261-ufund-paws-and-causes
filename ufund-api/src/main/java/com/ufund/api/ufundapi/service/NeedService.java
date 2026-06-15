


package com.ufund.api.ufundapi.service;


import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;



public interface NeedService {
    List<Need> getAllNeeds() throws IOException;

    Need getNeedById(int id) throws IOException;
    Need addNeed(Need need) throws IOException;

    Need updateNeed(int id,Need need) throws IOException;

    Need deleteNeed(int id) throws IOException;
    
}