/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andam
 */
public class Tagger {

    ArrayList<LibraryItem> libraryItems;
    ArrayList<Class> classes;
    private ArrayList<LibraryItem> unidentifiedLibraryItems;

    public Tagger(ArrayList<Class> classes) throws IOException {
        libraryItems = new ArrayList();
        this.classes = classes;
        loadLibraryItems("C:\\Users\\Andam\\Desktop\\T6\\LayerDetector\\libraries.txt");
        unidentifiedLibraryItems = new ArrayList<>();
    }

    // Read tagged libraries from text file
    public void loadLibraryItems(String s) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line = br.readLine();

            while (line != null) {
                LibraryItem item = new LibraryItem();
                String[] itemParts = line.split(",");

                if (itemParts.length > 0) {
                    item.setName(itemParts[0]);
                    item.setLayer(itemParts[1]);
                    libraryItems.add(item);
                }
                line = br.readLine();
            }
        }
    }

    public void tag() {
        // Get count of all layer imports
        for (Class c : classes) {
            countClassImports(c);
        }

        // Tag based on max layer
        for (Class c : classes) {
            int max = Math.max(Math.max(c.getModelCount(), Math.max(c.getViewCount(), c.getControllerCount())), c.getUnidentifiedCountCount());

            if (max == 0) {
                c.setTag("Model");
            } else {

                if (c.getViewCount() == max) {
                    c.setTag(c.getTag() + "View");
                }

                if (c.getModelCount() == max) {
                    c.setTag(c.getTag() + " Model");
                }

                if (c.getControllerCount() == max) {
                    c.setTag(c.getTag() + " Controller");
                }

                if (c.getUnidentifiedCountCount() == max) {
                    c.setTag(c.getTag() + " Unidentified");
                }

            }
        }

    }

    public void countClassImports(Class c) {
        for (String s : c.getImports()) {

            String[] importParts = s.split(" ");
            String[] packageParts = importParts[1].split("\\.");
//            System.out.println(packageParts.length);

//            String importString;
//            if (packageParts.length > 1) {
//                System.out.println(packageParts[1]);
//                System.out.println(packageParts[0] + "." + packageParts[1]);
//                importString = packageParts[0] + "." + packageParts[1];
//            } else {
//                importString = importParts[1];
//            }
            String layer = findLayer(packageParts);

            if (layer.equals("View")) {
                c.setViewCount(c.getViewCount() + 1);
            } else if (layer.equals("Controller")) {
                c.setControllerCount(c.getControllerCount() + 1);
            } else if (layer.equals("Model")) {
                c.setModelCount(c.getModelCount() + 1);
            } else {
                c.setUnidentifiedCountCount(c.getUnidentifiedCountCount() + 1);

                LibraryItem unidentifiedItem = new LibraryItem();
                unidentifiedItem.setName(s);
                unidentifiedItem.setLayer("unidentified");

                if (!isInUnidentifiedList(unidentifiedItem.getName())) {
                    unidentifiedLibraryItems.add(unidentifiedItem);
                }
            }

        }
    }

    public String findLayer(String[] importParts) {

        String layer = "unidentified";

        if (importParts.length < 1) {
            return layer;
        }

        String importString = importParts[0];

        for (int i = 1; i < importParts.length; i++) {
            for (LibraryItem li : libraryItems) {

                if (li.getName().equals(importString)) {
                    //System.out.println(li.getLayer());
                    layer = li.getLayer();
                }
            }
            importString += "." + importParts[i];
        }

        return layer;
    }

    public void print() {
        for (Class c : classes) {
            System.out.println(c.getName() + "\t ");
            System.out.println("\t" + c.getTag() + "\n\t" + c.getViewCount() + "\n\t"
                    + c.getModelCount() + "\n\t" + c.getControllerCount() + "\n\t" + c.getUnidentifiedCountCount() + "\n\n");
        }

        //getLayerCount();
    }

    public ArrayList<LayerObject> getLayerCount() {

        ArrayList<LayerObject> layers = new ArrayList();

        int[] layerCount = new int[4];

        for (Class c : classes) {
            switch (c.getTag()) {
                case "Model":
                    layerCount[0] = layerCount[0] + 1;
                case "View":
                    layerCount[1] = layerCount[1] + 1;
                case "Controller":
                    layerCount[2] = layerCount[2] + 1;
                default:
                    layerCount[3] = layerCount[3] + 1;
            }
//            layerCount[0] += c.getModelCount();
//            layerCount[1] += c.getViewCount();
//            layerCount[2] += c.getControllerCount();
//            layerCount[3] += c.getUnidentifiedCountCount();
        }

        layers.add(new LayerObject("Model", layerCount[0]));
        layers.add(new LayerObject("View", layerCount[1]));
        layers.add(new LayerObject("Controller", layerCount[2]));
        layers.add(new LayerObject("Unidentified", layerCount[3]));

        for (LayerObject lo : layers) {
            System.out.println(lo.getLayer() + ": " + lo.getCount());
        }
        return layers;
    }

    /**
     * @return the unidentifiedLibraryItems
     */
    public ArrayList<LibraryItem> getUnidentifiedLibraryItems() {
        return unidentifiedLibraryItems;
    }

    /**
     * @param unidentifiedLibraryItems the unidentifiedLibraryItems to set
     */
    public void setUnidentifiedLibraryItems(ArrayList<LibraryItem> unidentifiedLibraryItems) {
        this.unidentifiedLibraryItems = unidentifiedLibraryItems;
    }

    public boolean isInUnidentifiedList(String item) {

        for (LibraryItem e : unidentifiedLibraryItems) {
            if (e.getName().equals(item)) {
                return true;
            }
        }
        return false;

    }
}
