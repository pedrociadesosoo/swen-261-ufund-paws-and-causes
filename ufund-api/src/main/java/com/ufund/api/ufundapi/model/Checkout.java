package com.ufund.api.ufundapi.model;

import java.util.List;

/**
 * Class to represent UFund helper checkout 
 * @author Harikleia Sparakis
 */
public class Checkout {

    //#region Fields

    /**
     * the list of needs being checked out
     */
    private Need[] needsToCheckout;

    /**
     * the total cost of all the needs in the checkout
     */
    private double totalCost;

    ///#endregion
    

    //#region Constructors

    /**
     * Constructor from array of needs
     * @param array of needs
     */
    public Checkout(Need[] needs){
        needsToCheckout = needs;
        totalCost = calculateTotalCost();
    }

    /**
     * Constructor from list of needs
     * @param array of needs
     */
    public Checkout(List<Need> needs){
        needsToCheckout = (Need[]) needs.toArray();
        totalCost = calculateTotalCost();
    }

    /**
     * Constructor from array of needs
     * @param the FundingBasket object with needs being checked out
     */
     public Checkout(FundingBasket basket){
        //needsToCheckout = basket.getNeeds();
        //needsToCheckout = (Need[]) basket.getNeeds().toArray();
    }
    
    //#endregion


    //#region Getters

    /**
     * getter for array of needs
     * @return Checkout object's array of needs
     */
    public Need[] getNeeds(){
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


    //#region toString

    /**
     * toString override for checkout
     * @return string representation of the funding basket
     */
    @Override
    public String toString(){
        //TODO: format toString
        return "";
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
        for(Need need : needsToCheckout){
            cost += (need.getCost() * need.getQuantity());
        }

        //round to nearest hundreth place and return (Delete if redundant)
        cost = Math.floor(cost * 100) / 100;
        return cost; 
    }

    //#endregion

    //checkout should not be editable in itself, so no setters

}
