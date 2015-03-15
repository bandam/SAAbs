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
    private String tag;
    private int viewCount;
    private int modelCount;
    private int controllerCount;
    private int unidentifiedCount;
    
    
    
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
     * @param imports the imports to set
     */
    public void setImports(ArrayList<String> imports) {
        this.imports = imports;
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

    /**
     * @return the view
     */
    public int getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount the view to set
     */
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * @return the modelCount
     */
    public int getModelCount() {
        return modelCount;
    }

    /**
     * @param modelCount the modelCount to set
     */
    public void setModelCount(int modelCount) {
        this.modelCount = modelCount;
    }

    /**
     * @return the controllerCount
     */
    public int getControllerCount() {
        return controllerCount;
    }

    /**
     * @param controllerCount the controllerCount to set
     */
    public void setControllerCount(int controllerCount) {
        this.controllerCount = controllerCount;
    }

    /**
     * @return the unidentifiedCount
     */
    public int getUnidentifiedCountCount() {
        return unidentifiedCount;
    }

    /**
     * @param unidentifiedCount the unidentifiedCount to set
     */
    public void setUnidentifiedCountCount(int unidentifiedCount) {
        this.unidentifiedCount = unidentifiedCount;
    }
    
    
    
    
}
