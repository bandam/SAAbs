/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

/**
 *
 * @author Andam
 */
public class Stereotype implements Comparable<Stereotype>{
    private int count;
    private String name;
    
    public Stereotype(String name){
        this.name = name;
        count = 0;
    }
    
    public void increaseCount(){
        count++;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
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

    @Override
    public int compareTo(Stereotype o) {
        return Integer.compare(o.getCount(), count);
    }
    
    
}
