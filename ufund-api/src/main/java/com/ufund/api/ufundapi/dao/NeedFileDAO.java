package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

/**
 * File-based implementation of the {@linkplain NeedDAO} interface.
 * <p>
 * Needs are kept in an in-memory cache and persisted to a JSON file using
 * Jackson. The cache is the source of truth at runtime so read operations
 * (such as {@link #getAllNeeds()}) require no file I/O.
 *
 * @author U-Fund Team
 */
@Repository
public class NeedFileDAO implements NeedDAO {

    /** In-memory cache of needs, keyed by id and kept ordered for stable reads. */
    private final Map<Integer, Need> needs;

    /** Jackson mapper used to (de)serialize the JSON data file. */
    private final ObjectMapper objectMapper;

    /** Path to the JSON file that backs the cache. */
    private final String filename;

    /** Next id to assign to a newly created need. */
    private int nextId;

    /**
     * Creates a file-based Need Data Access Object.
     *
     * @param filename     path to the JSON data file (from {@code needs.file})
     * @param objectMapper Jackson mapper injected by Spring
     * @throws IOException if the data file cannot be read on startup
     */
    public NeedFileDAO(@Value("${needs.file:data/needs.json}") String filename,
                       ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.needs = new TreeMap<>();
        load();
    }

    /**
     * Loads the needs from the JSON file into the in-memory cache and
     * determines the next id to assign.
     *
     * @throws IOException if the file cannot be read or deserialized
     */
    private void load() throws IOException {
        needs.clear();
        nextId = 0;
        Need[] needArray = objectMapper.readValue(new File(filename), Need[].class);
        for (Need need : needArray) {
            needs.put(need.getId(), need);
            if (need.getId() > nextId) {
                nextId = need.getId();
            }
        }
        ++nextId;
    }

    /**
     * Saves the current in-memory cache back to the JSON file.
     *
     * @throws IOException if the file cannot be written
     */
    private void save() throws IOException {
        Need[] needArray = needs.values().toArray(new Need[0]);
        objectMapper.writeValue(new File(filename), needArray);
    }

    /**
     * Persists the cache, converting the checked {@link IOException} into an
     * unchecked one so the method signatures match the {@link NeedDAO} contract.
     */
    private void persist() {
        try {
            save();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to persist needs to " + filename, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns every need in the cupboard. When the cupboard is empty an empty
     * list is returned (never {@code null}).
     */
    @Override
    public List<Need> getAllNeeds() {
        synchronized (needs) {
            return new ArrayList<>(needs.values());
        }
    }

    @Override
    public Need getNeedById(int id) {
        synchronized (needs) {
            return needs.get(id);
        }
    }

    @Override
    public Need addNeed(Need need) {
        synchronized (needs) {
            Need created = new Need(nextId++, need.getName(), need.getQuantity(), need.getUnit());
            needs.put(created.getId(), created);
            persist();
            return created;
        }
    }

    @Override
    public Need updateNeed(Need need) {
        synchronized (needs) {
            if (!needs.containsKey(need.getId())) {
                return null;
            }
            needs.put(need.getId(), need);
            persist();
            return need;
        }
    }

    @Override
    public Need deleteNeed(int id) {
        synchronized (needs) {
            Need removed = needs.remove(id);
            if (removed != null) {
                persist();
            }
            return removed;
        }
    }
}
