package com.amirab.roleDetector;

import com.amirab.roleDetector.libraryImportAnalysis.PackageImportTagger;
import com.amirab.roleDetector.libraryImportAnalysis.LibraryItem;
import com.amirab.roleDetector.semanticAnalysis.SemanticsTagger;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoleDetectorMain {

    private static RoleDetectorMain roleDetectorInstance;

    private static final JFrame frame = new JFrame("Project Source Directory");
    static String projectSourceFile;
    static JPanel messagePanel;
    static JPanel controlsPanel;
    static JLabel messageLabel;

    private static ArrayList<Class> projectClasses;
    static ArrayList<LibraryItem> predefinedLibItems;
    private static String stereotypePredictor = "Import/Variable Type Analysis";

    final static String libraryItemsFileLoc = "roledetectorfiles\\libraries.txt";

    public static void main(String[] args) {
        start();
    }

    public static void selectProjectSRCMLFile() {
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 
        messagePanel = new JPanel(new FlowLayout());
        controlsPanel = new JPanel(new FlowLayout());

        messageLabel = new JLabel();
        JButton button = new JButton("Select File");
        String[] stereotypePredictors = {"Import/Variable Type Analysis", "Semantic Analysis", "Combined Analysis"};
        JComboBox stereotypePredictorCB = new JComboBox(stereotypePredictors);

        messagePanel.add(messageLabel);
        controlsPanel.add(new JLabel("Please Select Project srcML file: "));
        controlsPanel.add(button);
        controlsPanel.add(stereotypePredictorCB);

        stereotypePredictorCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();

                if (projectSourceFile != null) {
                    stereotypePredictor = cb.getSelectedItem().toString();

                    frame.dispose();

                    // Run Program
                    processProject();
                }
            }
        });

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
                    messageLabel.setText(projectSourceFile);
                    //System.out.println(projectSourceFile);
                    frame.pack();

                }
            }
        });

        //frame
        frame.add(messagePanel, BorderLayout.NORTH);
        frame.add(controlsPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void start() {
        selectProjectSRCMLFile();
    }

    private static void processProject() {
        projectClasses = PackageImportTagger.startTagger(projectClasses, projectSourceFile);
        SemanticsTagger semanticsTagger = new SemanticsTagger();
        semanticsTagger.startTagger(projectSourceFile, projectClasses);
        normalizeProjectClassStereotypes(projectClasses);
        setCombinedStereotypes(projectClasses);

        setOverallClassStereotype(projectClasses);
        printProjectClassesInfo(projectClasses);
    }

    public static ArrayList<Class> getTaggedProjectClass() {
        
    //        projectClasses = PackageImportTagger.startTagger(projectClasses, projectSourceFile);
    //        SemanticsTagger semanticsTagger = new SemanticsTagger();
    //        semanticsTagger.startTagger(projectSourceFile, projectClasses);
    //        setOverallClassStereotype(projectClasses);
    //        normalizeProjectClassStereotypes(projectClasses);
    //        setCombinedStereotypes(projectClasses);

        return projectClasses;
    }

    public static void printProjectClassesInfo(ArrayList<Class> projectClasses) {
        DecimalFormat df = new DecimalFormat("#.000");

        System.out.println("Number of Classes in project: " + projectClasses.size() + "\n");
        for (Class c : projectClasses) {
            System.out.print(c.getName());
            System.out.print("," + c.getPackageImportStereotypes().size());
            System.out.print("," + c.getSemanticStereotypes().size());
            System.out.print("," + c.getCombinedStereotypes().size());
            System.out.println();
        }

        System.out.println("Number of Classes in project: " + projectClasses.size()+ "\n");
        for (Class c : projectClasses) {
            System.out.print(c.getName());
//            System.out.print("," + c.getPackageImports().size());
//            System.out.print("," +  c.getUnidentifiedImports().size()) ;
//            System.out.println();
//        }
            for(String uimport: c.getUnidentifiedImports()){
                System.out.println(uimport);
            }
            System.out.println("Package Import Stereotypes:\n -----------------------------------------");
            for (Stereotype stereotype : c.getPackageImportStereotypes()) {
                System.out.println(stereotype.getName() + "\t" + df.format(stereotype.getCount()));
            }

            //System.out.println("class stereotype: " + c.getStereotypeTag());
            System.out.println("\n");
            if (!c.getSemanticStereotypes().isEmpty()) {
                System.out.println("Semantic Stereotypes"
                        + "\n-----------------------------------");
                for (Stereotype stereotype : c.getSemanticStereotypes()) {
                    System.out.println(stereotype.getName() + ": " + df.format(stereotype.getCount()));
                }
            }

            System.out.println("\n");
            if (!c.getCombinedStereotypes().isEmpty()) {
                System.out.println("Combined Stereotypes:\n---------------------------------------------");
                for (Stereotype stereotype : c.getCombinedStereotypes()) {
                    System.out.println(stereotype.getName() + "\t" + df.format(stereotype.getCount()));
                }

                System.out.println("\n");
            }
        }
    }

    private static void setCombinedStereotypes(ArrayList<Class> projectClasses) {
        for (Class c : projectClasses) {

            c.getPackageImportStereotypes().remove(c.findStereotype("unidentified", c.getPackageImportStereotypes()));

            // Add all package import stereotypes
            for (Stereotype s : c.getPackageImportStereotypes()) {
                c.getCombinedStereotypes().add(new Stereotype(s.getName(), s.getCount() / 2));
            }

            // Add Semantic Stereotypes if not already in list, otherwise increase score
            for (Stereotype s : c.getSemanticStereotypes()) {
                Stereotype cbs = c.findStereotype(s.getName().trim(), c.getCombinedStereotypes());

                if (cbs == null) {
                    c.getCombinedStereotypes().add(new Stereotype(s.getName(), s.getCount() / 2));
                } else {
                    cbs.setCount(cbs.getCount() + (s.getCount()) / 2);
                }

            }

            Collections.sort(c.getCombinedStereotypes());
        }
    }

    private static void setOverallClassStereotype(ArrayList<Class> projectClasses) {

        DecimalFormat df = new DecimalFormat("#.00");

        for (Class c : projectClasses) {

            ArrayList<Stereotype> selectedStereotypeToUse;

            if (stereotypePredictor.equals("Combined Analysis")) {
                selectedStereotypeToUse = c.getCombinedStereotypes();
            } else if (stereotypePredictor.equals("Semantic Analysis")) {
                selectedStereotypeToUse = c.getSemanticStereotypes();

            } else {
                selectedStereotypeToUse = c.getPackageImportStereotypes();
            }

            Collections.sort(selectedStereotypeToUse);

            long totalStereotypePoints = 0;
            for (Stereotype stereotype : selectedStereotypeToUse) {
                if (!(stereotype.getName().equals("unidentified"))) {
                    totalStereotypePoints += stereotype.getCount();
                }
            }

            String classStereotype = "";
            //System.out.println(totalStereotypePoints);

            if (totalStereotypePoints == 0) {
                classStereotype = "<<Object_Model>><<" + df.format((int) ((1 / 1) * 100)) + "%>>";
                c.setBiggestConcernSterotype("Object_Model");
            } else {
                for (Stereotype stereotype : selectedStereotypeToUse) {
                    if (!stereotype.getName().equals("unidentified")) {
                        c.setBiggestConcernSterotype(selectedStereotypeToUse.get(0).getName());

                        classStereotype += "<<" + stereotype.getName() + ">><<" + df.format(stereotype.getCount()) + "%>>";
                    }

                }

            }
            c.setConcernStereotypeList(classStereotype);
        }
    }

    public static String tagPlantUMLString(String plantUMLString) {
        String temp = plantUMLString;
        for (Class c : projectClasses) {
            temp = temp.replace(" " + c.getName() + " \n", " " + c.getName() + c.getConcernStereotypeList() + " \n");
        }

        // Debug PrintOut
        System.out.println(plantUMLString);
        System.out.println(temp);

        return temp;
    }

    public static RoleDetectorMain getInstance() {
        if (roleDetectorInstance == null) {
            roleDetectorInstance = new RoleDetectorMain();
        }

        return roleDetectorInstance;
    }

    private static void normalizeProjectClassStereotypes(ArrayList<Class> projectClasses) {
        for (Class c : projectClasses) {
            if (!c.getSemanticStereotypes().isEmpty()) {
                normalizeStereotypes(c.getSemanticStereotypes());
            }

            if (!c.getPackageImportStereotypes().isEmpty()) {
                normalizeStereotypes(c.getPackageImportStereotypes());
            }

            // normalizeStereotypes(c.getCombinedStereotypes());
        }
    }

    private static void normalizeStereotypes(ArrayList<Stereotype> stereotypes) {
        DecimalFormat df = new DecimalFormat("#.00");
        double totalScoreOfSteroetypes = 0.0;

        // Get the total of all stereotypes
        for (Stereotype stereotype : stereotypes) {
            if (!stereotype.getName().equals("unidentified")) {
                totalScoreOfSteroetypes += stereotype.getCount();
            }
        }

        for (Stereotype stereotype : stereotypes) {
            if (!stereotype.getName().equals("unidentified")) {
                stereotype.setCount(Double.valueOf(df.format((((double) stereotype.getCount() / (double) totalScoreOfSteroetypes) * 100))));
            }
        }
    }

    /**
     * @return the projectClasses
     */
    public static ArrayList<Class> getProjectClasses() {
        return projectClasses;
    }

    /**
     * @param aProjectClasses the projectClasses to set
     */
    public static void setProjectClasses(ArrayList<Class> aProjectClasses) {
        projectClasses = aProjectClasses;
    }

    /**
     * @return the stereotypePredictor
     */
    public static String getStereotypePredictor() {
        return stereotypePredictor;
    }

    
}
