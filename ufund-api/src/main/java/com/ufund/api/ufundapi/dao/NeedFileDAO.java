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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;



/**
 * Implements the functionality for JSON file-based peristance for Neeeds
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */


@Component
public class NeedFileDAO implements NeedDAO {
    private static final Logger LOG = Logger.getLogger(NeedFileDAO.class.getName());
    
    private Map<Integer, Need> needs = new TreeMap<>();
    private ObjectMapper objectMapper;
    private static int nextId; 
    private String filename;    

    /**
     * Creates a Needs File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */

    public NeedFileDAO(@Value("${needs.file}") String filename,ObjectMapper objectMapper) throws IOException {
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

    /**
     * Generates an array of {@linkplain Need needs} from the tree map
     *
     * @return The array of {@link Need needs}, may be empty
     */
    public Need[] getNeedArray() {
        return getNeedArray(null);
    }
    
    /**
     * Generates an array of {@linkplain Need needs} from the tree map for any
     * {@linkplain Need needs} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Need needs}
     * in the tree map
     * @param containsText The text to search for in need names
     * @return The array of {@link Need needs}, may be empty
     */
    public Need[] getNeedArray(String containsText) { 
        ArrayList<Need> needArrayList = new ArrayList<>();

        for (Need need : needs.values()) {
            if (containsText == null || need.getName().toLowerCase().contains(containsText.toLowerCase())) {
                needArrayList.add(need);
            }
        }

        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }

    public Need[] getNeedArray(NeedType type) { 
        ArrayList<Need> needArrayList = new ArrayList<>();

        for (Need need : needs.values()) {
            if (need.getType() == type) {
                needArrayList.add(need);
            }
        }

        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map for any
     * {@linkplain Need needs} that contains the text specified by containsText
     * and the need
     * If containsText is null, the array contains all of the {@linkplain Need needs}
     * in the tree map
     * @param containsText The text to search for in need names
     * @param type the type of need to search for
     * @return The array of {@link Need needs}, may be empty
     */
    public Need[] getNeedArray(String containsText, NeedType type) { 
        
        ArrayList<Need> needArrayList = new ArrayList<>();

        for (Need need : needs.values()) {
            if (
                need.getType() == type &&
                (containsText == null || 
                need.getName().toLowerCase().contains(containsText.toLowerCase()))
            ) {
                needArrayList.add(need);
            }
        }

        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }

    /**
     * Saves the {@linkplain Need needs} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link  Need needs} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Need[] needArray = getNeedArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), needArray);
        return true;
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        needs = new TreeMap<>();
        nextId = 0;

        Need[] needArray = objectMapper.readValue(new File(filename), Need[].class);
        
        for (Need need : needArray) {
            needs.put(need.getId(), need);
            if (need.getId() >= nextId)
                nextId = need.getId();
        }
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public List<Need> getAllNeeds() {
        synchronized (needs) {
            return new ArrayList<>(needs.values());
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Need getNeedById(int id) {
        synchronized (needs) {
            if (needs.containsKey(id))
                return needs.get(id);
            else
                return null;
        }
    }

   /**
    ** {@inheritDoc}
     */
    @Override
    public Need createNeed(Need need) {
        synchronized (needs) {
            try {
                if (getNeedArray(need.getName()) != null && getNeedArray(need.getName()).length > 0)
                    return null;

                need.setId(nextId());
                needs.put(need.getId(), need);
                save();
                return need;
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized (needs) {
            if (!needs.containsKey(need.getId()))
                return null; 
             
            needs.put(need.getId(), need);
            save(); 
            return need;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
        synchronized (needs) {
            if (needs.containsKey(id)) {
                needs.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public List<Need> findNeeds(String containsText) {
        synchronized (needs) {
            return new ArrayList<>(Arrays.asList(getNeedArray(containsText)));
        }
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public List<Need> findNeeds(String containsText, NeedType type) {
        synchronized (needs) {
            return new ArrayList<Need>(Arrays.asList(getNeedArray(containsText, type)));
        }
    }

    /**
    * {@inheritDoc}
     */
    @Override
    public List<Need> findNeeds(NeedType type) {
        synchronized (needs) {
            return new ArrayList<Need>(Arrays.asList(getNeedArray(type)));
        }
    }
}