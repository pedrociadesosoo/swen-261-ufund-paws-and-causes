package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import org.springframework.beans.factory.annotation.Value;

/**
 * JSON-file persistence for {@linkplain FundingBasket funding baskets}.
 * Baskets are held in memory and written back to the file (given by the
 * {@code fundingbaskets.file} property) on every mutation, so the file is
 * always in sync with what the API has confirmed to clients.
 */
@Component
public class FundingBasketFileDAO implements FundingBasketDAO {

    private static final Logger LOG = Logger.getLogger(FundingBasketFileDAO.class.getName());

    private Map<Integer, FundingBasket> fundingbaskets = new TreeMap<>();
    private ObjectMapper objectMapper;
    private String filename;

    public FundingBasketFileDAO(@Value("${fundingbaskets.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Generates the next id for a new {@linkplain FundingBasket fb} by
     * scanning up from 1, so ids freed by deletion get reused.
     *
     * @return The next available id
     */
    private synchronized int nextId() {
        int id = 1;
        while (fundingbaskets.containsKey(id)) { id++; }
        return id;
    }

    /**
     * Serializes the in-memory baskets to the JSON file.
     *
     * @return true if written successfully
     * @throws IOException if the file cannot be written
     */
    private boolean save() throws IOException {
        FundingBasket[] fbArray = getFundingBasketArray();
        objectMapper.writeValue(new File(filename), fbArray);
        return true;
    }

    /**
     * Loads all baskets from the JSON file into memory. Runs once at
     * startup; the file must exist and hold a JSON array (may be empty).
     *
     * @return true if loaded successfully
     * @throws IOException if the file cannot be read or parsed
     */
    private boolean load() throws IOException {
        fundingbaskets = new TreeMap<>();

        FundingBasket[] fbArray = objectMapper.readValue(new File(filename), FundingBasket[].class);

        for (FundingBasket fb : fbArray) {
            fundingbaskets.put(fb.getId(), fb);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundingBasket[] getFundingBasketArray(){
        synchronized (fundingbaskets) {
            ArrayList<FundingBasket> fbList = new ArrayList<>();
            for (FundingBasket fb : fundingbaskets.values()){
                fbList.add(fb);
            }
            FundingBasket[] fbArray = new FundingBasket[fundingbaskets.size()];
            fbList.toArray(fbArray);
            return fbArray;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundingBasket getFundingBasket(int id){
        synchronized (fundingbaskets) {
            if (fundingbaskets.containsKey(id)){
                return fundingbaskets.get(id);
            } else {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundingBasket createFundingBasket(FundingBasket fb) throws IOException {
        synchronized (fundingbaskets) {
            try {
                fb.setId(nextId());
                fundingbaskets.put(fb.getId(), fb);
                save();
                return fb;
            } catch (IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteFundingBasket(int id) throws IOException {
        synchronized (fundingbaskets) {
            if (fundingbaskets.containsKey(id)){
                fundingbaskets.remove(id);
                save();
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNeed(FundingBasket fb, Need need) throws IOException {
        synchronized (fundingbaskets){
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                return false;
            }
            fb.addNeed(need);
            save();
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeNeed(FundingBasket fb, Need need) throws IOException {
        synchronized (fundingbaskets){
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                needs.remove(need.getId());
                save();
                return true;
            }
            return false;
        }
    }


}
