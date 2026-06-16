package com.ufund.api.ufundapi.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

/**
 * Business-layer service for {@link Need} operations. It sits between the REST
 * controller and the {@link NeedDAO} persistence layer so that controllers
 * never depend on the DAO directly, keeping business logic in one place.
 *
 * @author U-Fund Team
 */
@Service
public class NeedService {
    private NeedDAO needDao;

    /**
     * Creates the service.
     *
     * @param needDao the {@link NeedDAO} persistence dependency, injected by Spring
     */
    public NeedService(NeedDAO needDao) {
        this.needDao = needDao;
    }

    /**
     * Retrieves all {@linkplain Need needs} in the cupboard.
     *
     * @return a list of all needs; an empty list (never {@code null}) when the
     * cupboard is empty
     */
    public List<Need> getAllNeeds() {
        return needDao.getAllNeeds();
    }

    /**
     * Retrieves the {@linkplain Need need} with the given id.
     *
     * @param id the id of the need
     * @return the matching need, or {@code null} if none exists
     */
    public Need getNeedById(int id) {
        return needDao.getNeedById(id);
    }

    /**
     * Creates and stores a new {@linkplain Need need}.
     *
     * @param need the need to create
     * @return the created need, including its assigned id
     */
    public Need addNeed(Need need) {
        return needDao.addNeed(need);
    }

    /**
     * Updates the {@linkplain Need need} with the given id.
     *
     * @param id the id of the need to update
     * @param need the new values for the need
     * @return the updated need, or {@code null} if no need with that id exists
     */
    public Need updateNeed(int id,Need need) {
        Need existing = needDao.getNeedById(id);
        if (existing == null) {
            return null;
        }
        need.setId(id);
        return needDao.updateNeed(need);
    }

    /**
     * Deletes the {@linkplain Need need} with the given id.
     *
     * @param id the id of the need to delete
     * @return the deleted need, or {@code null} if no need with that id exists
     */
    public Need deleteNeed(int id) {
        return needDao.deleteNeed(id);
    }
}
