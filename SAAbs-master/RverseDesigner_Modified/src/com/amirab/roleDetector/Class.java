/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file,  choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector;

import java.util.ArrayList;

/**
 *
 * @author Andam
 */
public class Class {
    private String name;
    private String concernStereotypeList;
    private String biggestConcernSterotype;
    private ArrayList<Stereotype> combinedStereotypes;
    private ArrayList<Stereotype> packageImportStereotypes;
    private ArrayList<Stereotype> semanticStereotypes;
    private ArrayList<String> packageImports;
    private ArrayList<String> unidentifiedImports;
    private ArrayList<Word> semanticwords;
    
    
    
    public Class(){
        name = "";
        concernStereotypeList = "";
        this.packageImportStereotypes = new ArrayList();
        this.semanticStereotypes = new ArrayList();
        this.packageImports = new ArrayList();
        this.unidentifiedImports = new ArrayList();
        this.semanticwords = new ArrayList();
        this.combinedStereotypes = new ArrayList();
    }
    
    public Class(String name, String tag){
        this.name = name;
        this.concernStereotypeList = tag;
        this.packageImportStereotypes = new ArrayList();
        this.semanticStereotypes = new ArrayList();
        this.packageImports = new ArrayList();
        this.unidentifiedImports = new ArrayList();
        this.semanticwords = new ArrayList();
        this.combinedStereotypes = new ArrayList();
    }
    
    
    public Stereotype findStereotype(String name, ArrayList<Stereotype> list){
        for(Stereotype stereotype: list){
            if(stereotype.getName().equals(name))
                return stereotype;
        }
        return null;
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
     * @return the tag
     */
    public String getConcernStereotypeList() {
        return concernStereotypeList;
    }

    /**
     * @param tag the tag to set
     */
    public void setConcernStereotypeList(String tag) {
        this.concernStereotypeList = tag;
    }
    
    public void deleteUnidentifiedPackageImport(String key){
        unidentifiedImports.remove(key);
    }

    /**
     * @return the packageImportStereotypes
     */
    public ArrayList<Stereotype> getPackageImportStereotypes() {
        return packageImportStereotypes;
    }

    /**
     * @param stereotypes the packageImportStereotypes to set
     */
    public void setPackageImportStereotypes(ArrayList<Stereotype> stereotypes) {
        this.packageImportStereotypes = stereotypes;
    }

    /**
     * @return the unidentifiedImports
     */
    public ArrayList<String> getUnidentifiedImports() {
        return unidentifiedImports;
    }

    /**
     * @param unidentifiedImports the unidentifiedImports to set
     */
    public void setUnidentifiedImports(ArrayList<String> unidentifiedImports) {
        this.unidentifiedImports = unidentifiedImports;
    }

    /**
     * @return the packageImports
     */
    public ArrayList<String> getPackageImports() {
        return packageImports;
    }

    /**
     * @param packageImports the packageImports to set
     */
    public void setPackageImports(ArrayList<String> packageImports) {
        this.packageImports = packageImports;
    }

    /**
     * @return the semanticwords
     */
    public ArrayList<Word> getSemanticwords() {
        return semanticwords;
    }

    /**
     * @param semanticwords the semanticwords to set
     */
    public void setSemanticwords(ArrayList<Word> semanticwords) {
        this.semanticwords = semanticwords;
    }

    /**
     * @return the semanticStereotypes
     */
    public ArrayList<Stereotype> getSemanticStereotypes() {
        return semanticStereotypes;
    }

    /**
     * @param semanticStereotypes the semanticStereotypes to set
     */
    public void setSemanticStereotypes(ArrayList<Stereotype> semanticStereotypes) {
        this.semanticStereotypes = semanticStereotypes;
    }

    /**
     * @return the combinedStereotypes
     */
    public ArrayList<Stereotype> getCombinedStereotypes() {
        return combinedStereotypes;
    }

    /**
     * @param combinedStereotypes the combinedStereotypes to set
     */
    public void setCombinedStereotypes(ArrayList<Stereotype> combinedStereotypes) {
        this.combinedStereotypes = combinedStereotypes;
    }

    /**
     * @return the biggestConcernSterotype
     */
    public String getBiggestConcernSterotype() {
        return biggestConcernSterotype;
    }

    /**
     * @param biggestConcernSterotype the biggestConcernSterotype to set
     */
    public void setBiggestConcernSterotype(String biggestConcernSterotype) {
        this.biggestConcernSterotype = biggestConcernSterotype;
    }
    
    
}
