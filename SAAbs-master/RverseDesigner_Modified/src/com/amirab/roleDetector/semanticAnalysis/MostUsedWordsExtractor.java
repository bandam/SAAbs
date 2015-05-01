/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.semanticAnalysis;

import com.amirab.roleDetector.Word;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Andam
 */
public class MostUsedWordsExtractor {

    HashMap<String, Word> tempWordList;
    ArrayList<Word> mostUsedWords;
    Stemmer stemmer = Stemmer.getInstance();

    public MostUsedWordsExtractor(String textFileOfContentToAnalyze) {
        tempWordList = new HashMap();
        readFile(textFileOfContentToAnalyze);
    }

    public MostUsedWordsExtractor(ArrayList<String> wordList) {
        tempWordList = new HashMap();
        processWordList(wordList);
    }

    // Read Content from a file and save extracted words into wordlist
    private void readFile(String pathname) {
        try {
            // Read file with content
            File file = new File(pathname);
            StringBuilder fileContentsBuilder = new StringBuilder((int) file.length());
            Scanner scanner = new Scanner(file);
            String lineSeparator = System.getProperty("line.separator");

            while (scanner.hasNextLine()) {
                fileContentsBuilder.append(scanner.nextLine()).append(lineSeparator);
            }

            String fileContents = fileContentsBuilder.toString();

            // Break contents into words
            String[] contentArray1 = fileContents.split("\\s+");
            processWordList(new ArrayList(Arrays.asList(contentArray1)));
            

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void processWordList(ArrayList<String> wordList){
        ArrayList<String> contentArray = new ArrayList();
            for (String s : wordList) {
                if(s == null){
                    break;
                }
                for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                    //System.out.println(w);
                    contentArray.add(w);
                }
            }

            // Save unique words in hashmap
            for (String s : contentArray) {
                String string = stemmer.Stem(s.toLowerCase());
                if (string.length() > 2) {
                    if (tempWordList.containsKey(string)) {
                        tempWordList.get(string).increaseFrequency();
                    } else {
                        Word word = new Word(string, 1);
                        tempWordList.put(string, word);
                    }
                }

            }
    }

    // Removes stop words defined in a textfile
    public void removeStopWords(String stopWordsFile) {
        try {
            // Read file with content
            File file = new File(stopWordsFile);
            StringBuilder fileContentsBuilder = new StringBuilder((int) file.length());
            Scanner scanner = new Scanner(file);
            String lineSeparator = System.getProperty("line.separator");

            while (scanner.hasNextLine()) {
                fileContentsBuilder.append(scanner.nextLine()).append(lineSeparator);
            }

            String fileContents = fileContentsBuilder.toString();

            // Break contents into words
            String[] contentArray = fileContents.split(",");
            //System.out.println("stopwords length: " + contentArray.length);

            for (String string : contentArray) {
                if (tempWordList.containsKey(string)) {
                    tempWordList.remove(string);
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // Get most used words
    public ArrayList<Word> getMostUsedWords(int count) {
        mostUsedWords = new ArrayList();
        for (Map.Entry<String, Word> word : tempWordList.entrySet()) {
            mostUsedWords.add(word.getValue());
        }

        Collections.sort(mostUsedWords);

        if (count > mostUsedWords.size()) {
            return mostUsedWords;
        } else {
            return new ArrayList(mostUsedWords.subList(0, count));
        }
    }

    public void printWords() {
        for (Word word : getMostUsedWords(40)) {
            //String key = word.getKey();
            System.out.println(word.getName() + ","  + ":" + word.getFrequency());
            //Word value = word.getValue();
        }
    }

    public static void main(String[] args) {
        MostUsedWordsExtractor wordsExtractor = new MostUsedWordsExtractor("roledetectorfiles\\StereotypeDescriptionsText\\SecurityValidator.txt");

        wordsExtractor.removeStopWords("roledetectorfiles\\stopWords.txt");
        wordsExtractor.printWords();
    }
}
