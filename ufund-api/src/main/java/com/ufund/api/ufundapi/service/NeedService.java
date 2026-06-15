package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedService{
    private NeedDAO needDao;

    public NeedService(NeedDAO needDao) {
        this.needDao = needDao;
    }

    public List<Need> getAllNeeds() throws IOException {
        return needDao.getAllNeeds();
    }


    public Need getNeedById(int id) throws IOException{
        return needDao.getNeedById(id);
    }


    public Need addNeed(Need need) throws IOException {
        return needDao.addNeed(need);
    }


    public Need updateNeed(int id, Need need) throws IOException {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    public Need deleteNeed(int id) throws IOException {

            Need deletedNeed = needDao.getNeedById(id);
            if (deletedNeed != null && needDao.deleteNeed(id)) {
                return deletedNeed;
            } else {
                return null;
            }
    }
}
