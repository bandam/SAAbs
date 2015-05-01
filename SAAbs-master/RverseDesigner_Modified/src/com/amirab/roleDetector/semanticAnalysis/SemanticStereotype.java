/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.semanticAnalysis;

import com.amirab.roleDetector.Word;
import java.util.ArrayList;

/**
 *
 * @author Andam
 */
public class SemanticStereotype {
    private String name;
    private ArrayList<Word> frequentlyUsedWords;
    private int totalFrequency;
    
    public SemanticStereotype(){
        this.name = "";
        this.frequentlyUsedWords = new ArrayList();
        this.totalFrequency = 0;
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
     * @return the frequentlyUsedWords
     */
    public ArrayList<Word> getFrequentlyUsedWords() {
        return frequentlyUsedWords;
    }

    /**
     * @param frequentlyUsedWords the frequentlyUsedWords to set
     */
    public void setFrequentlyUsedWords(ArrayList<Word> frequentlyUsedWords) {
        this.frequentlyUsedWords = frequentlyUsedWords;
    }

    /**
     * @return the totalFrequency
     */
    public int getTotalFrequency() {
        return totalFrequency;
    }

    /**
     * @param totalFrequency the totalFrequency to set
     */
    public void setTotalFrequency(int totalFrequency) {
        this.totalFrequency = totalFrequency;
    }

    
}
