package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedServiceImpl implements NeedService {
    private NeedDAO needDao;

    public NeedServiceImpl(NeedDAO needDao) {
        this.needDao = needDao;
    }

    public List<Need> getAllNeeds() throws IOException {
        return needDao.getAllNeeds();
    }

    /**
     * Finds and retrieves {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find
     * @return the {@link Need need} of the need if found, otherwise null
     * @throws IOException if an issue with storage access occurs
     */
    public Need getNeedById(int id) throws IOException {
        return needDao.getNeedById(id);
    }

    
    public Need createNeed(Need need) throws IOException {
        return needDao.createNeed(need);
    }

    public Need[] getNeedArray(String containsText) {
        return needDao.findNeeds(containsText).toArray(new Need[0]);
    }

    public Need updateNeed(int id,Need need) throws IOException {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    /**
     * Retrieves and deletes {@linkplain Need need} with the provided id
     * @param id if of the {@link Need need} to find and delete
     * @return the {@link Need need} that has been deleted if sucessful, null otherwise
     * @throws IOException if an issue with storage access occurs
     */
    public Need deleteNeed(int id) throws IOException {

        Need deletedNeed = needDao.getNeedById(id);
        if (deletedNeed != null && needDao.deleteNeed(id)) {
            return deletedNeed;
        } else {
            return null;
        }
    }

    public List<Need> findNeeds(String containsText) {
        return needDao.findNeeds(containsText);
    }

}