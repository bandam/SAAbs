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
public class LayerObject {
    private String layer;
    private int count;
    
    public LayerObject(String layer, int count){
        this.layer = layer;
        this.count = count;
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

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    
}
