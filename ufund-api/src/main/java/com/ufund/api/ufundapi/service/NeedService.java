package com.ufund.api.ufundapi.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedService {
    private NeedDAO needDao;

    public NeedService(NeedDAO needDao) {
        this.needDao = needDao;
    }

    public List<Need> getAllNeeds() {
        return needDao.getAllNeeds();
    }

    public Need getNeedById(int id) {
        return needDao.getNeedById(id);
    }

    public Need addNeed(Need need) {
        return needDao.addNeed(need);
    }

    public Need updateNeed(int id,Need need) {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    public Need deleteNeed(int id) {
        return needDao.deleteNeed(id);
    }
}
