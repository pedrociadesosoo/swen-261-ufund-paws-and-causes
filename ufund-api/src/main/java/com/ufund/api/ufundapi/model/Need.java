package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Need{
    private int id;
    private String name;
    private int quantity;
    private String type;
    private double cost;

    public Need() {}

    public Need(int id, String name, double cost, int quantity, String type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}