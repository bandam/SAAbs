/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector.libraryImportAnalysis;

import com.amirab.roleDetector.Stereotype;
import com.amirab.roleDetector.Class;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Andam
 */
public class PackageImportsExtractor {

    private static PackageImportsExtractor packageImportsExtractorInstance;
    private static final JFrame frame = new JFrame("Project Source Directory");
    private static String projectSourceFile;
    private static ArrayList<Class> projectClasses;
    public static boolean isProcessing = true;

    public static void selectProjectSRCMLFile() {
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JButton button = new JButton("Select File");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File("."));
                fileChooser.setDialogTitle("choosertitle");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //System.out.println(selectedFile.getName());
                    projectSourceFile = selectedFile.getAbsolutePath();
                    System.out.println(projectSourceFile);
                    frame.dispose();
                    readSRCMLFile(projectSourceFile);

                }
            }

        });
        frame.add(new JLabel("Please Select Project srcML file: "));
        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }

    public static void readSRCMLFile(String projectSourceFile) {
        try {
            File inputFile = new File(projectSourceFile);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//unit";
            NodeList classNodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            //System.out.println(classNodeList.getLength());

            projectClasses = new ArrayList();
            Element currentClass;
            for (int i = 0; i < classNodeList.getLength(); i++) {
                currentClass = (Element) classNodeList.item(i);
                extractClassAndImports(currentClass, projectClasses);
            }

            //Print
            getAllDeclarationsFromClasses(projectSourceFile, projectClasses);

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

    private static void extractClassAndImports(Element classNode, ArrayList projectClasses) {

        Node classNameNode = classNode.getAttributes().getNamedItem("filename");
        if (classNameNode != null) {
            // Add Class to projectClassList
            String[] classDirectoryParts = String.valueOf(classNameNode).split("/");
            String[] classNameParts = classDirectoryParts[classDirectoryParts.length - 1].split("\\.");

            Class currentClassObject = new Class();
            currentClassObject.setName(classNameParts[0]);

            // All imports belonging to current class
            NodeList classImports = classNode.getElementsByTagName("import");
            Element currentImportElement;
            for (int j = 0; j < classImports.getLength(); j++) {

                //Parts of one import
                currentImportElement = (Element) classImports.item(j);
                NodeList currentImportParts = currentImportElement.getElementsByTagName("name");
                String currentImportString = buildImportStringFromImportParts(currentImportParts);
                currentClassObject.getPackageImports().add(currentImportString);

            }

            // Add class to projectClasses list
            projectClasses.add(currentClassObject);
        }

    }

    private static String buildImportStringFromImportParts(NodeList importParts) {
        String importString = "";
        for (int q = 1; q < importParts.getLength(); q++) {
            importString += importParts.item(q).getFirstChild().getNodeValue();
            //System.out.println(importString);

            // Append . to next string f
            importString += ".";
        }

        return importString;
    }

    private static void getAllDeclarationsFromClasses(String projectSourceFile, ArrayList<Class> projectClasses) {
        try {
            File inputFile = new File(projectSourceFile);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//unit/class";
            NodeList classNodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            //System.out.println(classNodeList.getLength());

            //projectClasses = new ArrayList();
            Element currentClassElement;
            for (int i = 0; i < classNodeList.getLength(); i++) {
                currentClassElement = (Element) classNodeList.item(i);
                getClassDeclarations(currentClassElement, projectClasses);
                //System.out.println("\n");
            }

            isProcessing = false;
            //printProjectClassesInfo(projectClasses);

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

    private static void getClassDeclarations(Element classNode, ArrayList<Class> projectClasses) {

        Class classInProjectClasses = null;
        String className = ((Element) (classNode)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
        //System.out.println("(classnamesearchedfor):"+className);

        // If no class name is found exit
        if (className == null) {
            return;
        }
        classInProjectClasses = getClass(projectClasses, className);

        if (classInProjectClasses == null) {
            return;
        }
        NodeList classBlockUnit = classNode.getElementsByTagName("block");

        NodeList declarationsList = ((Element) (classBlockUnit.item(0))).getElementsByTagName("decl_stmt");

        // For each decaration statement
        for (int i = 0; i < declarationsList.getLength(); i++) {
            //NodeList currentDeclaration = declarationsList.item(i).getFirstChild().getNodeValue();
            Element type = (Element) ((Element) (declarationsList.item(i))).getElementsByTagName("type").item(0);
            String declarationTypeName = ((Element) (type.getElementsByTagName("name").item(0))).getFirstChild().getNodeValue();
            if (declarationTypeName == null) {
                NodeList nameparts = ((Element) (type.getElementsByTagName("name").item(0))).getElementsByTagName("name");
                if (nameparts.getLength() > 0) {
                    declarationTypeName = ((Element) ((Element) (type.getElementsByTagName("name").item(0))).getElementsByTagName("name").item(nameparts.getLength() - 1)).getFirstChild().getNodeValue();
                    classInProjectClasses.getPackageImports().add(declarationTypeName);
                    //System.out.println( declarationTypeName+ "--");

                }
            } else {
                classInProjectClasses.getPackageImports().add(declarationTypeName);
                //System.out.println(declarationTypeName);
            }

        }

    }

    private static Class getClass(ArrayList<Class> projectClasses, String name) {
        for (Class c : projectClasses) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    public static void printProjectClassesInfo(ArrayList<Class> projectClasses) {

        for (Class c : projectClasses) {
            System.out.println(c.getName());

            for (Stereotype stereotype : c.getPackageImportStereotypes()) {
                System.out.println(stereotype.getName() + "\t" + stereotype.getCount());
            }

            System.out.println("class stereotype: " + c.getStereotypeTag());

            if (!c.getUnidentifiedImports().isEmpty()) {
                System.out.println("Package Imports"
                        + "\n--------------------------");
                for (String packageImport : c.getUnidentifiedImports()) {
                    System.out.println(packageImport);
                }
            }

            System.out.println("\n");
        }
    }

    public static PackageImportsExtractor getInstance() {
        if (packageImportsExtractorInstance == null) {
            packageImportsExtractorInstance = new PackageImportsExtractor();
        }

        return packageImportsExtractorInstance;
    }

    public static ArrayList<Class> extractProjectClasses(String projectSource) {
        projectSourceFile = projectSource;
        readSRCMLFile(projectSource);

        return projectClasses;
    }

    public static void main(String[] args) {
        selectProjectSRCMLFile();
    }
}
