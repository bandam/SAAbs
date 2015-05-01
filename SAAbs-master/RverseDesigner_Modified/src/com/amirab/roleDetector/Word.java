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
public class Word implements Comparable<Word>{
    private int frequency;
    private String name;

    public Word(String name, int frequency){
        this.frequency = frequency;
        this.name = name;
    }
    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * increase frequency
     */
    public void increaseFrequency() {
        this.frequency++;
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
    public int compareTo(Word other) {
        if(this.getFrequency() > other.getFrequency()){
            return -1;
        }
        else if(this.getFrequency() < other.getFrequency()){
            return 1;
        }
        
        return 0;
    }
    
    
}
