/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.libraryImportAnalysis;

/**
 *
 * @author Andam
 */
public class LibraryItem {
    private String name;
    private String stereotype;

    
    
    public LibraryItem(){
    }
    
    public LibraryItem(String name, String stereotype){
        this.name = name;
        this.stereotype = stereotype;
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
     * @return the stereotype
     */
    public String getStereotype() {
        return stereotype;
    }

    /**
     * @param stereotype the stereotype to set
     */
    public void setStereotype(String stereotype) {
        this.stereotype = stereotype;
    }

}
