/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andam
 */
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LayerDetector_Main {

    private static LayerDetector_Main layerDetector;
    // Data
    private static ArrayList<Class> classes;
    private static ArrayList<LibraryItem> unknownLibraryItems;
    static String projectSourceFile;
    static File selectedDirectory;

    // 
    static FileWalker filewalker;
    static Tagger tagger;

    private JFrame frame = new JFrame("Project Source Directory");
    private static final JFrame frame_unknownLibraries = new JFrame("Unknow Libraries");

    private LayerDetector_Main() {
    }

    public static LayerDetector_Main getInstance() {
        if (layerDetector == null) {
            layerDetector = new LayerDetector_Main();
        }

        return layerDetector;
    }

    public void start() {

        getFrame().setLayout(new FlowLayout());
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        JButton button = new JButton("Select File");

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File("."));
                fileChooser.setDialogTitle("choosertitle");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getName());
                    projectSourceFile = selectedFile.getAbsolutePath();
                    System.out.println(projectSourceFile);
                    getFrame().setVisible(false);

                    // Get Class Data from project
                    getDataFromProject(projectSourceFile);
                }
            }

        });

        getFrame().add(new JLabel("Please Select Project Source Directory: "));
        getFrame().add(button);
        getFrame().pack();
        getFrame().setVisible(true);

        // Test run
        // new LayerDetector_Main().fillClassesDummy();
    }

    public String tagPlantUMLString(String plantUMLString) {
        String temp = plantUMLString;
        for (Class c : getClasses()) {
            temp = temp.replace(c.getName() + " \n", c.getName() + "<<" + c.getTag() + ">> \n");
        }

        // Debug PrintOut
        System.out.println(plantUMLString);
        System.out.println(temp);

        return temp;
    }

    public static void getDataFromProject(String projectSource) {
        try {
            filewalker = new FileWalker();
            setClasses(filewalker.getData(projectSource));
            tagger = new Tagger(getClasses());
            tagger.tag();

            // get a list of unknow library items from tagger
            unknownLibraryItems = tagger.getUnidentifiedLibraryItems();

            // Print tags for debug
            tagger.print();
            showUnKnownLibraries();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void showUnKnownLibraries() {

        JPanel panel = new JPanel();
        JButton close = new JButton("Close");
        panel.setLayout(new BoxLayout(panel, 1));
        DefaultListModel<LibraryItem> unknowLibModel = new DefaultListModel<>();
        JList<LibraryItem> unknownLibJList = new JList<>(unknowLibModel);
        unknownLibJList.setCellRenderer(new UnknownLibraryItemRederer());

        for (LibraryItem li : unknownLibraryItems) {
            unknowLibModel.addElement(li);
        }

        panel.add(unknownLibJList);

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame_unknownLibraries.setVisible(false);
            }
        });

        panel.add(close);
        JScrollPane scrollPan = new JScrollPane(panel);
        scrollPan.setAutoscrolls(true);

        frame_unknownLibraries.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_unknownLibraries.setLocationRelativeTo(null);
        frame_unknownLibraries.getContentPane().add(scrollPan);
        frame_unknownLibraries.setPreferredSize(new Dimension(500, 600));
        frame_unknownLibraries.pack();
        frame_unknownLibraries.setVisible(true);
    }


    /**
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * @param frame the frame to set
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * @return the classes
     */
    public static ArrayList<Class> getClasses() {
        return classes;
    }

    /**
     * @param aClasses the classes to set
     */
    public static void setClasses(ArrayList<Class> aClasses) {
        classes = aClasses;
    }
}
