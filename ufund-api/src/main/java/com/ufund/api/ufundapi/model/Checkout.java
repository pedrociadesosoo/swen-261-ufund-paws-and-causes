package com.ufund.api.ufundapi.model;

import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent UFund helper checkout 
 * @author Harikleia Sparakis
 */
public class Checkout {

    //#region Fields

    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    @JsonProperty("id") private int id;

    /**
     * the list of needs being checked out
     */
    @JsonProperty("needs") private Map<Integer, Need> needsToCheckout;

    /**
     * the total cost of all the needs in the checkout  
     */
    @JsonProperty("cost") private double totalCost;

    ///#endregion
    

    //#region Constructor

    /**
     * Constructor from array of needs
     * @param the FundingBasket object with needs being checked out
     */
     public Checkout(int id, FundingBasket basket){
        this.id = id;
        needsToCheckout = basket.getNeeds();
        totalCost = calculateTotalCost();
    }
    
    //#endregion


    //#region Getters

    /**
     * getter for array of needs
     * @return Checkout object's array of needs
     */
    public int getId(){
        return id;
    }

    /**
     * getter for array of needs
     * @return Checkout object's array of needs
     */
    public Map<Integer, Need> getNeeds(){
        return needsToCheckout;
    }

    /**
     * getter total cost
     * @return Checkout object's total cost
     */
    public double getTotalCost(){
        return totalCost;
    }

    //#endregion


    //#region Helper Methods

    /**
     * calculates the total cost of all needs in checkout
     * @return the sum of all costs 
     */
    private double calculateTotalCost(){
        
        //sum the costs of all the needs in checkout
        double cost = 0;
        for(Need need : needsToCheckout.values()){
            cost += (need.getCost() * need.getQuantity());
        }

        //round to nearest hundreth place and return (Delete if redundant)
        cost = Math.floor(cost * 100) / 100;
        return cost; 
    }

    //#endregion

}
