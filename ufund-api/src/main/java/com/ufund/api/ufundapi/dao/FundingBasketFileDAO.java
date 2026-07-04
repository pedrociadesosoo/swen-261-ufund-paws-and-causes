package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import org.springframework.beans.factory.annotation.Value;

@Component
public class FundingBasketFileDAO implements FundingBasketDAO {

    private static final Logger LOG = Logger.getLogger(NeedFileDAO.class.getName());
    
    private Map<Integer, FundingBasket> fundingbaskets = new TreeMap<>();
    private ObjectMapper objectMapper;
    private static int nextId; 
    private String filename;
    
    public FundingBasketFileDAO(@Value("${fundingbaskets.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Generates the next id for a new {@linkplain Need need }
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private boolean save() throws IOException {
        FundingBasket[] fbArray = getFundingBasketArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), fbArray);
        return true;
    }

    private boolean load() throws IOException {
        fundingbaskets = new TreeMap<>();
        nextId = 0;

        FundingBasket[] fbArray = objectMapper.readValue(new File(filename), FundingBasket[].class);
        
        for (FundingBasket fb : fbArray) {
            fundingbaskets.put(fb.getId(), fb);
            if (fb.getId() >= nextId)
                nextId = fb.getId();
        }
        ++nextId;
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
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public boolean addNeed(FundingBasket fb, Need need){
        synchronized (fundingbaskets){
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                return false;
            }
            fb.addNeed(need);
            return true;
        }
    }

    @Override
    public boolean removeNeed(FundingBasket fb, Need need){
                synchronized (fundingbaskets){
            Map<Integer, Need> needs = fb.getNeeds();
            if (needs.containsKey(need.getId())){
                needs.remove(need.getId());
                return true;
            }
            return false;
        }
    }


}
