/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

import javax.swing.JCheckBox;

/**
 *
 * @author Andam
 */
public class LibraryItem {
    private String name;
    private String layer;
    private final JCheckBox viewCheckBox;
    private final JCheckBox controllerCheckBox;
    private final JCheckBox modelCheckBox;
    
    
    public LibraryItem(){
        viewCheckBox = new JCheckBox();
        controllerCheckBox = new JCheckBox();
        modelCheckBox = new JCheckBox();
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
     * @return the layer
     */
    public String getLayer() {
        return layer;
    }

    /**
     * @param layer the layer to set
     */
    public void setLayer(String layer) {
        this.layer = layer;
    }
    
    public String selectedLayer(){
        
        if(viewCheckBox.isSelected())
            return "View";
        
        if(modelCheckBox.isSelected())
            return "Model";
        
        if(controllerCheckBox.isSelected())
            return "Controller";
        
        return "Unidentified";
    }
    
    @Override
    public String toString(){
        return name + ": " + layer;
    }

    /**
     * @return the viewCheckBox
     */
    public JCheckBox getViewCheckBox() {
        return viewCheckBox;
    }

    /**
     * @return the controllerCheckBox
     */
    public JCheckBox getControllerCheckBox() {
        return controllerCheckBox;
    }

    /**
     * @return the modelCheckBox
     */
    public JCheckBox getModelCheckBox() {
        return modelCheckBox;
    }
    
}
