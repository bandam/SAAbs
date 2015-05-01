/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.semanticAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.amirab.roleDetector.Class;
import com.amirab.roleDetector.Word;

/**
 *
 * @author Andam
 */
public class SemanticsExtractor {
    private static SemanticsExtractor semanticExtractorInstance;
    private  ArrayList<Class> projectClasses;

    public  void startExtractor(String projectSourceFile) {
        readSRCMLFile(projectSourceFile);
    }

    public  void readSRCMLFile(String projectSourceFile) {
        try {
            File inputFile = new File(projectSourceFile);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//class";
            NodeList classNodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            //System.out.println(classNodeList.getLength());
            
            Element currentClass;
            for (int i = 0; i < classNodeList.getLength(); i++) {
                currentClass = (Element) classNodeList.item(i);
                extractSemanticWords(currentClass);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private  void extractSemanticWords(Element classNode) {

        Class classInProjectClasses = null;

        String className = ((Element) (classNode.getElementsByTagName("name")).item(0)).getFirstChild().getNodeValue();

        if (className != null) {

            classInProjectClasses = findClass(className);

            
            ArrayList<String> tempWordList = new ArrayList();
            tempWordList.add(className);
            NodeList classBlockUnit = classNode.getElementsByTagName("block");

            NodeList declarationsList = ((Element) (classBlockUnit.item(0))).getElementsByTagName("function");

            // For each decaration statement
            for (int i = 0; i < declarationsList.getLength(); i++) {
                Element type = (Element) ((Element) (declarationsList.item(i))).getElementsByTagName("name").item(1);

                if (type != null) {
                    String declarationTypeName = type.getFirstChild().getNodeValue();
                    tempWordList.add(declarationTypeName);
                }

            }

            MostUsedWordsExtractor mostUsedWordExtractor = new MostUsedWordsExtractor(tempWordList);
            mostUsedWordExtractor.removeStopWords("roledetectorfiles\\stopWords.txt");
            classInProjectClasses.setSemanticwords(mostUsedWordExtractor.getMostUsedWords(30));

        }
    }

    public  Class findClass(String className) {
        Class currentClass = new Class();
        for (Class c : projectClasses) {
            if (c.getName().equals(className)) {
                currentClass = c;
            }
        }
        return currentClass;
    }
    
    public static SemanticsExtractor getInstance(){
        if(semanticExtractorInstance == null){
            semanticExtractorInstance = new SemanticsExtractor();
        }
        
        return semanticExtractorInstance;
    }
    
    public  ArrayList<Class> extractProjectClassWithSemantics(String projectSource, ArrayList<Class> pc){
        projectClasses = pc;
        readSRCMLFile(projectSource);
        return projectClasses;
    }

    public static void printProjectClassesInfo(ArrayList<Class> projectClasses) {

        for (Class c : projectClasses) {
            System.out.println(c.getName());

            if (!c.getSemanticwords().isEmpty()) {
                System.out.println("Words In Class:"
                        + "\n--------------------------");
                for (Word word : c.getSemanticwords()) {
                    System.out.println(word.getName() + ":" + word.getFrequency());
                }
            }

            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
        SemanticsExtractor semanticsExtractor = SemanticsExtractor.getInstance();
        printProjectClassesInfo(semanticsExtractor.extractProjectClassWithSemantics("C:\\Users\\Andam\\Desktop\\T6\\Data\\srcMLFiles\\srcMLFiles\\xmls\\FiniteAutomata.xml", new ArrayList<Class>()));
    }
}
