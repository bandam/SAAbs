/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file,  choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

import java.util.ArrayList;

/**
 *
 * @author Andam
 */
public class Class {
    private String name;
    private ArrayList<String> imports;
    private ArrayList<Stereotype> stereotypes;
    private String tag;
    
    
    // Constructors
    public Class(){
        imports = new ArrayList();
        name = "";
        tag = "";
    }
    
    public Class(String name, String tag){
        this.name = name;
        this.tag = tag;
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
     * @return the imports
     */
    public ArrayList<String> getImports() {
        return imports;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
