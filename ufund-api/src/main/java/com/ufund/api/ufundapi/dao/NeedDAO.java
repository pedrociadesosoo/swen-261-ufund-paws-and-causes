package com.ufund.api.ufundapi.dao;

import java.util.List;

import com.ufund.api.ufundapi.model.Need;

/**
 * Defines the persistence contract for {@link Need} objects (the "cupboard").
 * Implementations are responsible for storing and retrieving needs; the rest of
 * the application depends only on this interface, not on any concrete storage.
 *
 * @author U-Fund Team
 */
public interface NeedDAO {

    /**
     * Retrieves all {@linkplain Need needs} in the cupboard.
     *
     * @return a list of all needs; an empty list (never {@code null}) when the
     * cupboard is empty
     */
    List<Need> getAllNeeds();

    /**
     * Retrieves the {@linkplain Need need} with the given id.
     *
     * @param id the id of the need to find
     * @return the matching need, or {@code null} if no need has that id
     */
    Need getNeedById(int id);

    /**
     * Creates and stores a new {@linkplain Need need}; the id is assigned by the DAO.
     *
     * @param need the need to create
     * @return the created need, including its newly assigned id
     */
    Need addNeed(Need need);

    /**
     * Updates an existing {@linkplain Need need}, identified by its id.
     *
     * @param need the need to update
     * @return the updated need, or {@code null} if no need with that id exists
     */
    Need updateNeed(Need need);

    /**
     * Deletes the {@linkplain Need need} with the given id.
     *
     * @param id the id of the need to delete
     * @return the deleted need, or {@code null} if no need with that id exists
     */
    Need deleteNeed(int id);

}
