/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector;

/**
 *
 * @author Andam
 */
public class Stereotype implements Comparable<Stereotype>{
    private String name;
    private double count;
    
    
    public Stereotype(String name, double count){
        this.name = name; this.count = count;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the count
     */
    public double getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(double count) {
        this.count = count;
    }

    @Override
    public int compareTo(Stereotype otherStereotype) {
        if(this.getCount() > otherStereotype.getCount()){
            return -1;
        }
        else if(this.getCount() < otherStereotype.getCount()){
            return 1;
        }
        
        return 0;
    }
    
    
}
