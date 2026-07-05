package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

@Service
public class NeedServiceImpl implements NeedService {
    private NeedDAO needDao;
    
    /**
     * Creates a NeedServiceImpl with the provided {@link NeedDAO}
     *
     * @param needDao The {@link NeedDAO} to use for data access
     */
    public NeedServiceImpl(NeedDAO needDao) {
        this.needDao = needDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<Need> getAllNeeds() throws IOException {
        return needDao.getAllNeeds();
    }

    /**
     * {@inheritDoc}
     */
    public Need getNeedById(int id) throws IOException {
        return needDao.getNeedById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Need createNeed(Need need) throws IOException {
        return needDao.createNeed(need);
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getNeedArray(String containsText) {
        return needDao.findNeeds(containsText).toArray(new Need[0]);
    }

    /**
     * {@inheritDoc}
     */
    public Need updateNeed(int id, Need need) throws IOException {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    /**
     * {@inheritDoc}
     */
    public Need deleteNeed(int id) throws IOException {
        Need deletedNeed = needDao.getNeedById(id);
        if (deletedNeed != null && needDao.deleteNeed(id)) {
            return deletedNeed;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Need> findNeeds(String containsText) {
        return needDao.findNeeds(containsText);
    }
}