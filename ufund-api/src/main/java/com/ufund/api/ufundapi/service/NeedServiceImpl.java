package com.ufund.api.ufundapi.service;

import java.util.List;
import org.springframework.stereotype.Service;
import java.io.IOException;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedServiceImpl implements NeedService {
    private NeedDAO needDao;

    public NeedServiceImpl(NeedDAO needDao) {
        this.needDao = needDao;
    }

    public List<Need> getAllNeeds() {
        return needDao.getAllNeeds();
    }

    public Need getNeedById(int id) {
        return needDao.getNeedById(id);
    }

    public Need addNeed(Need need) throws IOException {
        return needDao.addNeed(need);
    }

    public Need updateNeed(int id,Need need) throws IOException {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    public boolean deleteNeed(int id) throws IOException {
        return needDao.deleteNeed(id);
    }

}