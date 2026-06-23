package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;


public interface NeedService {

     /**
     * Retrieves all {@linkplain Need needs}
     *
     * @return List of all {@link Need needs}
     * @throws IOException if an issue with storage access occurs
     */
    List<Need> getAllNeeds() throws IOException;

    /**
     * Finds all {@linkplain Need needs} whose name contains the given text
     *
     * @param containsText The text to search for in need names
     * @return List of {@link Need needs} whose name contains the given text
     */
    List<Need> findNeeds(String containsText);

    /**
     * Finds and retrieves {@linkplain Need need} with the provided id
     * 
     * @param id if of the {@link Need need} to find
     * @return the {@link Need need} of the need if found, otherwise null
     * @throws IOException if an issue with storage access occurs
     */
    Need getNeedById(int id) throws IOException;
    
    /**
     * Creates a new {@linkplain Need need}
     *
     * @param need The {@link Need need} to create
     * @return the created {@link Need need} if successful, null otherwise
     * @throws IOException if an issue with storage access occurs
     */
    Need createNeed(Need need) throws IOException;

    /**
     * Retrieves an array of {@linkplain Need needs} whose name contains the given text
     *
     * @param containsText The text to search for in need names
     * @return array of {@link Need needs} whose name contains the given text
     */
    Need[] getNeedArray(String containsText);

    /**
     * Updates an existing {@linkplain Need need}
     *
     * @param id id of the {@link Need need} to update
     * @param need The {@link Need need} with updated fields
     * @return the updated {@link Need need} if successful, null otherwise
     * @throws IOException if an issue with storage access occurs
     */
    Need updateNeed(int id, Need need) throws IOException;

    /**
     * Deletes {@linkplain Need need} with the provided id
     * 
     * @param id if of the {@link Need need} to find and delete
     * @return the {@link Need need} that has been deleted if sucessful, null otherwise
     * @throws IOException if an issue with storage occurs
     */
    Need deleteNeed(int id) throws IOException;
}