/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.semanticAnalysis;

/**
 *
 * @author Andam
 */

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.amirab.roleDetector.Word;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andam
 */
public class GenerateMostUsedWordsForStereotypes {

    public static void main(String[] args) {
        try {
            String fileLocation = "roledetectorfiles\\StereotypeDescriptionsText\\";
            String[][] stereotypeDescriptions = {
                {"Concurrency","Concurrency"},
                {"Event/EventHandler","EventEventHandler"},
                {"Exception Handling","ExceptionHandling"},
                {"IO","IO"},
                {"Messaging","Messaging"},
                {"Network/Web","NetworkWeb"},
                {"ObjectModel","ObjectModel"},
                {"Parser/Interpreter","ParserInterpreter"},
                {"Persistence","Persistence"},
                {"Security/Validator","SecurityValidator"},
                {"Time/Date/Zone","TimeDateZone"},
                {"TransactionHandling","TransactionHandling"},
                {"UserInterface","UserInterface"}
            };
            
            for (String[] stereotypeDescription : stereotypeDescriptions) {
                PrintWriter fileToWriteTo = new PrintWriter(new BufferedWriter(new FileWriter("roledetectorfiles\\CommonNamesUsedInStereotypes.txt", true)));
                saveCommonWordsToFile(fileLocation,stereotypeDescription, fileToWriteTo);
                fileToWriteTo.close();
            }
            
           
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    public static void saveCommonWordsToFile(String fileLoc,String[] stereotypeDesc, PrintWriter p) {
        MostUsedWordsExtractor mostUsedWordsExtractor = new MostUsedWordsExtractor(fileLoc+stereotypeDesc[1]+".txt");
        mostUsedWordsExtractor.removeStopWords("roledetectorfiles\\stopWords.txt");
        ArrayList<Word> mostUsedWords = mostUsedWordsExtractor.getMostUsedWords(30);
        p.append(stereotypeDesc[0] + ":");
        
        for(Word word: mostUsedWords){
            p.append(word.getName()+"." + word.getFrequency()+",");
        }
        p.append("\n");
//                System.fileToWriteTo.println(file.getAbsolutePath());

    }
}


