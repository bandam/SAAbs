/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.semanticAnalysis;

import com.amirab.roleDetector.Word;
import com.amirab.roleDetector.Class;
import com.amirab.roleDetector.Stereotype;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Andam
 */
public class SemanticsTagger {


    ArrayList<SemanticStereotype> semanticSetereotypes = new ArrayList();
    ArrayList<Class> projectClasses;

    public void startTagger(String projectSourceFiles, ArrayList<Class> pc) {
        projectClasses = pc;
        loadSemanticStereotypesFromFile();

        SemanticsExtractor semanticExtractor = SemanticsExtractor.getInstance();
        projectClasses = semanticExtractor.extractProjectClassWithSemantics(projectSourceFiles, projectClasses);
        tagClasses(projectClasses, semanticSetereotypes);
    }

    public void loadSemanticStereotypesFromFile() {

        try (BufferedReader br = new BufferedReader(new FileReader("roledetectorfiles\\CommonNamesUsedInStereotypes.txt"))) {
            String line = br.readLine();
            //System.out.println(line);

            while (line != null) {
                SemanticStereotype stereotype = new SemanticStereotype();
                String[] stereotypeParts = line.split(":");
                //System.out.println(stereotypeParts[0]);

                if (stereotypeParts.length > 1) {
                    stereotype.setName(stereotypeParts[0]);

                    String[] wordList = stereotypeParts[1].split(",");
                    //System.out.println(wordList[wordList.length - 1]);
                    if (wordList.length > 1) {
                        for (String wordString : wordList) {
                            String[] wordParts = wordString.split("\\.");
                            //System.out.println(wordParts[0]);
                            Word word = new Word(wordParts[0], Integer.valueOf(wordParts[1]));
                            stereotype.getFrequentlyUsedWords().add(word);
                        }
                    }
                }
                line = br.readLine();

                semanticSetereotypes.add(stereotype);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Set total frequency for each stereotype
        for (SemanticStereotype stereotype : semanticSetereotypes) {
            int total = 0;
            for (Word word : stereotype.getFrequentlyUsedWords()) {
                total += word.getFrequency();
            }

            stereotype.setTotalFrequency(total);
        }
    }

    public void tagClasses(ArrayList<Class> projectClasses, ArrayList<SemanticStereotype> semanticStereotypes) {
        for (Class c : projectClasses) {
            for (Word word : c.getSemanticwords()) {
                addWordStereotypeToClass(c, word, semanticStereotypes);
            }
            Collections.sort(c.getSemanticStereotypes());
        }
    }

    public void addWordStereotypeToClass(Class c, Word word, ArrayList<SemanticStereotype> semanticStereotypes) {
        for (SemanticStereotype semanticStereotype : semanticStereotypes) {
            for (Word stereotypeWord : semanticStereotype.getFrequentlyUsedWords()) {
                if (word.getName().equals(stereotypeWord.getName())) {
                    Stereotype stereotypeInClass = getStereotypeInClassStereotypes(semanticStereotype.getName(), c);
                    int wordweight = (int) Math.ceil(((double) stereotypeWord.getFrequency() / (double) semanticStereotype.getTotalFrequency()) * (double) word.getFrequency());
                    if (stereotypeInClass == null) {
                        c.getSemanticStereotypes().add(new Stereotype(semanticStereotype.getName(), wordweight));
                    } else {
                        stereotypeInClass.setCount(stereotypeInClass.getCount() + wordweight);
                    }
                }
            }
        }
    }

    public Stereotype getStereotypeInClassStereotypes(String stereotypeName, Class c) {
        for (Stereotype s : c.getSemanticStereotypes()) {
            if (stereotypeName.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }

    public void printStereotypes() {
        for (SemanticStereotype s : semanticSetereotypes) {
            System.out.println(s.getName());
            System.out.println(s.getTotalFrequency());
            System.out.println("--------------------------");
            for (Word w : s.getFrequentlyUsedWords()) {
                System.out.println(w.getName() + ":" + w.getFrequency());
            }

            System.out.println("\n");
        }
    }

    public void printClassInfo() {
        for (Class c : projectClasses) {
            System.out.println(c.getName());

            System.out.println("--------------------------");
            for (Stereotype s : c.getSemanticStereotypes()) {
                System.out.println(s.getName() + ":" + s.getCount());
            }

            System.out.println("\n");
        }
    }
    
    public void setOverallClassStereotype(ArrayList<Class> projectClasses){
        DecimalFormat df = new DecimalFormat("#.000");
        
        for(Class c: projectClasses){
            Collections.sort(c.getSemanticStereotypes());
            
            String classSetereotyp = "";
            for(Stereotype stereotype : c.getSemanticStereotypes()){
                
            }
        }
    }

    public static void main(String[] args) {
        SemanticsTagger semanticTagger = new SemanticsTagger();
        semanticTagger.startTagger("C:\\Users\\Andam\\Desktop\\T6\\Data\\srcMLFiles\\srcMLFiles\\xmls\\FiniteAutomata.xml", new ArrayList<Class>());
        semanticTagger.printClassInfo();
    }
}
