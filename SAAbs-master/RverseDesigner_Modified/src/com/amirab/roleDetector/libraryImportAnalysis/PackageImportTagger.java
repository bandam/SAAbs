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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Andam
 */
public class PackageImportTagger {

    private static String projectSourceFile;
    public static boolean taggerDone = false;
    private static final JFrame frame = new JFrame("Project Source Directory");
    private static ArrayList<Class> projectClasses;
    final static String libraryItemsFileLoc = "roledetectorfiles\\libraries.txt";
    static ArrayList<LibraryItem> predefinedLibItems;

    public static ArrayList<Class> startTagger(ArrayList<Class> pc, String psf) {
        loadLibraryItems(libraryItemsFileLoc);
        projectSourceFile = psf;
        projectClasses = pc;
        projectClasses = PackageImportsExtractor.extractProjectClasses(projectSourceFile);

        tagClasses(projectClasses);
        findUnidentifiedImportsLocally(projectClasses);
        //Extractor.printProjectClassesInfo(projectClasses);
        taggerDone = true;
        return projectClasses;
    }

    private static void tagClasses(ArrayList<Class> projectClasses) {
        for (Class c : projectClasses) {
            for (String packageImport : c.getPackageImports()) {
                String[] importParts = packageImport.split("\\.");
                addcurrentImportStereotypeToClassStereotypeList(importParts, c);
            }

            // Remove all imports from class package imports
            c.setPackageImports((ArrayList<String>) new ArrayList());
        }
    }

    private static void addcurrentImportStereotypeToClassStereotypeList(String[] importParts, Class currentClass) {
        // Identify stereotype of current import
        String importString = "";
        String importStereotype = "unidentified";
        for (int q = 0; q < importParts.length; q++) {
            importString += importParts[q];
            //System.out.println(importString);
            String returnOfStereotypeSearch = findStereotypeOfImportInPredefinedLibrary(importString, predefinedLibItems);

            if (!returnOfStereotypeSearch.equals("unidentified")) {
                importStereotype = returnOfStereotypeSearch;
            }

            // Add unidentified import to list inside class
            if (importStereotype.equals("unidentified") && (q == importParts.length - 1)) {
                currentClass.getUnidentifiedImports().add(importString);
            }

            // Append . to next string f
            importString += ".";
        }
        //System.out.println(" " + importStereotype + "\n");
 
        // Add import stereotype to class stereotype list if its not already there
        Stereotype foundStereotypeToIncrease = currentClass.findStereotype(importStereotype,currentClass.getPackageImportStereotypes());
        if (foundStereotypeToIncrease != null) {
            foundStereotypeToIncrease.setCount(foundStereotypeToIncrease.getCount() + 1);
        } else {
            currentClass.getPackageImportStereotypes().add(new Stereotype(importStereotype, 1));
        }
    }

    private static String findStereotypeOfImportInPredefinedLibrary(String importString, ArrayList<LibraryItem> library) {
        String stereotype = "unidentified";

        for (LibraryItem libraryItem : library) {
            if (importString.equals(libraryItem.getName())) {
                stereotype = libraryItem.getStereotype();
            }
        }

        return stereotype;
    }

    // Read tagged libraries from text file
    private static void loadLibraryItems(String s) {
        predefinedLibItems = new ArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line = br.readLine();

            while (line != null) {
                LibraryItem item = new LibraryItem();
                String[] itemParts = line.split(",");

                if (itemParts.length > 0) {
                    item.setName(itemParts[0]);
                    item.setStereotype(itemParts[1]);
                    predefinedLibItems.add(item);
                    //System.out.println(itemParts[0] + "  " + itemParts[1]);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PackageImportTagger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PackageImportTagger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void findUnidentifiedImportsLocally(ArrayList<Class> projectClasses) {

        for (Class c : projectClasses) {
            ArrayList<String> importsResolved = new ArrayList();
            // Each unidentified import in a class
            for (String unidentifiedImport : c.getUnidentifiedImports()) {
                String[] importParts = unidentifiedImport.split("\\.");
                Class foundImportClass = null;

                //Search for class that matches import class declarationTypeName
                for (Class cf : projectClasses) {
                    if (importParts[importParts.length - 1].equals(cf.getName())) {
                        foundImportClass = cf;
                    }
                }

                // add stereotype count in found class to unidentified import class
                if (foundImportClass != null) {
                    //Decrease count of unidentified imports and add identified import to to be removed list
                    c.findStereotype("unidentified",c.getPackageImportStereotypes()).setCount(c.findStereotype("unidentified",c.getPackageImportStereotypes()).getCount() - 1);
                    importsResolved.add(unidentifiedImport);

                    //check if there are any stereotypes in found class
                    for (Stereotype s : foundImportClass.getPackageImportStereotypes()) {

                        if (!(s.getName().equals("unidentified"))) {
                            // check if stereotype in found class also exists in unidentified import item class
                            Stereotype stereotypeToIncrease = c.findStereotype(s.getName(),c.getPackageImportStereotypes());
                            // Add found class stereotype to class stereotype list if its not already there
                            if (stereotypeToIncrease != null) {
                                stereotypeToIncrease.setCount(stereotypeToIncrease.getCount() + s.getCount());
                            } else {
                                c.getPackageImportStereotypes().add(new Stereotype(s.getName(), s.getCount()));
                            }
                        }
                    }

                }
            }

            if (importsResolved.size() > 0) {
                for (String importString : importsResolved) {
                    c.deletePackageImport(importString);
                }
            }

        }
    }

    public static void main(String[] args) {
        selectProjectSRCMLFile();
    }

    private static void selectProjectSRCMLFile() {
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
                    //System.out.println(projectSourceFile);
                    frame.dispose();
                    // Run Program
                    startTagger(new ArrayList<Class>(), projectSourceFile);

                }
            }

        });
        frame.add(new JLabel("Please Select Project srcML file: "));
        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }
}
