package com.ufund.api.ufundapi.dao;

import java.util.List;

import com.ufund.api.ufundapi.model.Need;



public interface NeedDAO {
    List<Need> getAllNeeds();

    Need getNeedById(int id);
    Need addNeed(Need need);

    Need updateNeed(Need need);

    Need deleteNeed(int id);
    
}
