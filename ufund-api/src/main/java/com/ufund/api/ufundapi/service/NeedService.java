package com.ufund.api.ufundapi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedService implements NeedDAO{
    private NeedDAO needDao;

    public NeedService(NeedDAO needDao) {
        this.needDao = needDao;
    }

    @Override
    public List<Need> getAllNeeds() {
        return needDao.getAllNeeds();
    }

    @Override
    public Need getNeedById(int id) {
        return needDao.getNeedById(id);
    }

    @Override
    public Need addNeed(Need need) {
        return needDao.addNeed(need);
    }

    @Override
    public Need updateNeed(int id,Need need) {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    @Override
    public Need updateNeed(Need need) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNeed'");
    }

    @Override
    public Need deleteNeed(int id) {
        return needDao.deleteNeed(id);
    }

    
}
