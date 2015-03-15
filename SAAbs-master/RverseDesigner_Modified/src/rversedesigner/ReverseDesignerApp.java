/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import au.com.bytecode.opencsv.CSVReader;
import com.amirab.layerdetector.LayerDetector_Main;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.tree.TreeNode;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import java.io.IOException;
import java.lang.*;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Range;
import weka.core.Utils;


/**
 *
 * @author Wei, Hafeez, Junwei
 */
public class ReverseDesignerApp extends javax.swing.JFrame {
    /**
     * Creates new form ReverseDesignerView
     */
    protected JSVGCanvas canvas;
    protected Document doc;
    protected Element svg;
    protected TextInformationSetting textInfo = new TextInformationSetting();
    protected ListofProjects lisProj = new ListofProjects();

    class xmiExtendName extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".txt")
                    || file.getAbsolutePath().endsWith(".xmi")
                    || file.getAbsolutePath().endsWith(".xml");
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "XMI files (*.xmi/xml/txt)";
        }
    }

    class csvExtendName extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".csv");

        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "CSV files (*.csv)";
        }
    }
    
public class PredictRankingUp implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        float f1 = Float.parseFloat(tmp1[tmp1.length - 1]) ;
        float f2 = Float.parseFloat(tmp2[tmp2.length - 1]) ;
        return (f1<f2 ? -1 : (f1==f2 ? 0 : 1));
    }
}

public class PredictRankingDown implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        float f1 = Float.parseFloat(tmp1[tmp1.length - 1]) ;
        float f2 = Float.parseFloat(tmp2[tmp2.length - 1]) ;
        return (f1>f2 ? -1 : (f1==f2 ? 0 : 1));
    }
}

public class NumOpsRanking implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1 = Integer.parseInt(tmp1[2]);
        int i2 = Integer.parseInt(tmp2[2]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}
    
public class NumPubOpsRanking implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1 = Integer.parseInt(tmp1[3]);
        int i2 = Integer.parseInt(tmp2[3]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}
        
public class NOCRanking implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1 = Integer.parseInt(tmp1[8]);
        int i2 = Integer.parseInt(tmp2[8]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}
   
public class CouplingRanking implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1 = Integer.parseInt(tmp1[15]) + Integer.parseInt(tmp1[16]) + Integer.parseInt(tmp1[20])
                + Integer.parseInt(tmp1[21]) + Integer.parseInt(tmp1[22]) + Integer.parseInt(tmp1[23]);
        int i2 = Integer.parseInt(tmp2[15]) + Integer.parseInt(tmp2[16]) + Integer.parseInt(tmp2[20])
                + Integer.parseInt(tmp2[21]) + Integer.parseInt(tmp2[22]) + Integer.parseInt(tmp2[23]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}

public class CouplingRankingResult implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1 = Integer.parseInt(tmp1[6]) + Integer.parseInt(tmp1[7]) + Integer.parseInt(tmp1[8])
                + Integer.parseInt(tmp1[9]) + Integer.parseInt(tmp1[10]) + Integer.parseInt(tmp1[11]);
        int i2 = Integer.parseInt(tmp2[6]) + Integer.parseInt(tmp2[7]) + Integer.parseInt(tmp2[8])
                + Integer.parseInt(tmp2[9]) + Integer.parseInt(tmp2[10]) + Integer.parseInt(tmp2[11]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}

public class SpecClassesRankingResult implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        Double i1 = Double.parseDouble(tmp1[5]) + Double.parseDouble(tmp1[7]) + Double.parseDouble(tmp1[9]);
        Double i2 = Double.parseDouble(tmp2[5]) + Double.parseDouble(tmp2[7]) + Double.parseDouble(tmp2[9]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}

public class TotalDesignMetricsRankingResult implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1;
        i1 = Integer.parseInt(tmp1[1]) + Integer.parseInt(tmp1[2]) + Integer.parseInt(tmp1[3])
            + Integer.parseInt(tmp1[4]) + Integer.parseInt(tmp1[5])  
            + Integer.parseInt(tmp1[6]) + Integer.parseInt(tmp1[7]) + Integer.parseInt(tmp1[8])
            + Integer.parseInt(tmp1[9]) + Integer.parseInt(tmp1[10]) + Integer.parseInt(tmp1[11]);
        int i2;
        i2 = Integer.parseInt(tmp2[1]) + Integer.parseInt(tmp2[2]) + Integer.parseInt(tmp2[3])
            + Integer.parseInt(tmp2[4]) + Integer.parseInt(tmp2[5]) 
            + Integer.parseInt(tmp2[6]) + Integer.parseInt(tmp2[7]) + Integer.parseInt(tmp2[8])
            + Integer.parseInt(tmp2[9]) + Integer.parseInt(tmp2[10]) + Integer.parseInt(tmp2[11]);        
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}

// calculate design meric based on the text + design metrics result
public class TotalDesignMetricsRankingResultAll implements Comparator<String>{
    @Override
    public int compare(String o1, String o2) {
        String[] tmp1 = o1.split("\\,");
        String[] tmp2 = o2.split("\\,");
        int i1;
        i1 = Integer.parseInt(tmp1[10]) + Integer.parseInt(tmp1[11]) + Integer.parseInt(tmp1[12])
            + Integer.parseInt(tmp1[13]) + Integer.parseInt(tmp1[14])  
            + Integer.parseInt(tmp1[15]) + Integer.parseInt(tmp1[16]) + Integer.parseInt(tmp1[17])
            + Integer.parseInt(tmp1[18]) + Integer.parseInt(tmp1[19]) + Integer.parseInt(tmp1[20]);
        int i2;
        i2 = Integer.parseInt(tmp2[10]) + Integer.parseInt(tmp2[11]) + Integer.parseInt(tmp2[12])
            + Integer.parseInt(tmp2[13]) + Integer.parseInt(tmp2[14]) 
            + Integer.parseInt(tmp2[15]) + Integer.parseInt(tmp2[16]) + Integer.parseInt(tmp2[17])
            + Integer.parseInt(tmp2[18]) + Integer.parseInt(tmp2[19]) + Integer.parseInt(tmp2[20]);        
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}

public ReverseDesignerApp() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        //this.resultTableCSV.setVisible(false);
        //this.rankingTable.setVisible(false);
        inDesignPatternFileName = "";
    }

    private void checkTreeInit() {

        top =
        new DefaultMutableTreeNode(null);
        createNodes(top);
        metricsSelection = new javax.swing.JTree(top);
        checkTreeManager = new CheckTreeManager(metricsSelection);
    }

    private void createNodes(DefaultMutableTreeNode top) {
    DefaultMutableTreeNode category = null;
    DefaultMutableTreeNode book = null;
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        classifiers = new javax.swing.ButtonGroup();
        inDesignChooser = new javax.swing.JFileChooser();
        inDesignSource = new javax.swing.ButtonGroup();
        xmiOption = new javax.swing.ButtonGroup();
        testOptionRButton = new javax.swing.ButtonGroup();
        csvFileChooser = new javax.swing.JFileChooser();
        xmiFileName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        metricsSelection = new javax.swing.JTree();
        wekaProcess = new javax.swing.JButton();
        checkSuggestion = new javax.swing.JButton();
        GenDiagram = new javax.swing.JButton();
        CouplingTab = new javax.swing.JTabbedPane();
        classRankingTab = new javax.swing.JScrollPane();
        rankingTable = new javax.swing.JTable();
        sizeTab = new javax.swing.JScrollPane();
        sizePredictorTable = new javax.swing.JTable();
        couplingTab = new javax.swing.JScrollPane();
        couplingPredictorTable = new javax.swing.JTable();
        textTab = new javax.swing.JScrollPane();
        textPredictorTable = new javax.swing.JTable();
        classificationTab = new javax.swing.JScrollPane();
        resultOutput = new javax.swing.JTextArea();
        progressBar = new javax.swing.JProgressBar();
        progressLabel = new javax.swing.JLabel();
        packageGen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        loadCSV = new javax.swing.JMenuItem();
        loadIndesignPattern = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        XmiUMLVersion = new javax.swing.JMenu();
        UML1X_XMI1_0 = new javax.swing.JRadioButtonMenuItem();
        UML1X_XMI1_1_3 = new javax.swing.JRadioButtonMenuItem();
        UML2X_XMI2_0_2_1 = new javax.swing.JRadioButtonMenuItem();
        inDesignPatternSourceMenu = new javax.swing.JMenu();
        manualSelectionMenuItem = new javax.swing.JRadioButtonMenuItem();
        patternFileMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        classifierMenu = new javax.swing.JMenu();
        randomForestMenuItem = new javax.swing.JRadioButtonMenuItem();
        knn1MenuItem = new javax.swing.JRadioButtonMenuItem();
        naiveBayesMenuItem = new javax.swing.JRadioButtonMenuItem();
        testOption = new javax.swing.JMenu();
        refineOptimize = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        crossVal10Folds = new javax.swing.JRadioButtonMenuItem();
        crossValLeave1Out = new javax.swing.JRadioButtonMenuItem();
        halfhalfMethod = new javax.swing.JRadioButtonMenuItem();
        trainingSet = new javax.swing.JRadioButtonMenuItem();
        predictorSet = new javax.swing.JMenu();
        designMetrics = new javax.swing.JCheckBoxMenuItem();
        textMetrics = new javax.swing.JCheckBoxMenuItem();
        textMiningFileChooser = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        fileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\aisli_000\\Dropbox\\MyShare\\Tool\\Input"));
        fileChooser.setDialogTitle("Open new XMI files");
        fileChooser.setFileFilter(new xmiExtendName());

        inDesignChooser.setCurrentDirectory(new java.io.File("C:\\Users\\aisli_000\\Dropbox\\MyShare\\Tool"));
        inDesignChooser.setFileFilter(new csvExtendName());

        csvFileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\aisli_000\\Dropbox\\MyShare\\Tool"));
        csvFileChooser.setDialogTitle("Open new CSV files");
        csvFileChooser.setFileFilter(new csvExtendName());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Software Architecture Abstractor (SAAbs) tool");

        checkTreeInit();
        metricsSelection.setDoubleBuffered(true);
        jScrollPane1.setViewportView(metricsSelection);

        wekaProcess.setText("Analyse");
        wekaProcess.setOpaque(false);
        wekaProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wekaProcessActionPerformed(evt);
            }
        });

        checkSuggestion.setText("Show Suggestion");
        checkSuggestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSuggestionActionPerformed(evt);
            }
        });

        GenDiagram.setText("Class Diagram");
        GenDiagram.setActionCommand("DisplaySVG");
        GenDiagram.setName("GenDiagram"); // NOI18N
        GenDiagram.setOpaque(false);
        GenDiagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenDiagramActionPerformed(evt);
            }
        });

        rankingTable.setAutoCreateRowSorter(true);
        rankingTable.setBackground(new java.awt.Color(153, 255, 255));
        rankingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Class Name", "In Design", "Prediction", "Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        rankingTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        rankingTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        rankingTable.setDoubleBuffered(true);
        rankingTable.setDragEnabled(true);
        classRankingTab.setViewportView(rankingTable);
        rankingTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        rankingTable.getColumnModel().getColumn(1).setPreferredWidth(450);
        rankingTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        rankingTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        rankingTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        CouplingTab.addTab("Class Ranking", null, classRankingTab, "");

        sizePredictorTable.setAutoCreateRowSorter(true);
        sizePredictorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Classnames", "Num. of Attribute", "Num. of Operation", "Num. of Public Operation", "Getters", "Setters"
            }
        ));
        sizeTab.setViewportView(sizePredictorTable);
        sizePredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        sizePredictorTable.getColumnModel().getColumn(1).setPreferredWidth(450);
        sizePredictorTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        sizePredictorTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        sizePredictorTable.getColumnModel().getColumn(4).setPreferredWidth(160);
        sizePredictorTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        sizePredictorTable.getColumnModel().getColumn(6).setPreferredWidth(80);

        CouplingTab.addTab("Size ", sizeTab);

        couplingPredictorTable.setAutoCreateRowSorter(true);
        couplingPredictorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Classname", "Dependency Out", "Dependency In", "Export Coupling Attribute", "Import Coupling Attribute", "Export Coupling Parameter", "Import Coupling Parameter"
            }
        ));
        couplingTab.setViewportView(couplingPredictorTable);

        CouplingTab.addTab("Coupling", couplingTab);

        textPredictorTable.setAutoCreateRowSorter(true);
        textPredictorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Classname", "No of Keyword", "Max in Document", "Total in Document", "Max in All Classes", "Max in Specific Classes", "Total in All Classes", "Total in Specific Classes", "Weight in All Classes", "Weight in Specific Clases"
            }
        ));
        textTab.setViewportView(textPredictorTable);
        textPredictorTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        textPredictorTable.getColumnModel().getColumn(1).setPreferredWidth(380);

        CouplingTab.addTab("Text", textTab);

        resultOutput.setColumns(20);
        resultOutput.setRows(5);
        classificationTab.setViewportView(resultOutput);

        CouplingTab.addTab("Classification Result", classificationTab);

        progressBar.setDoubleBuffered(true);
        progressBar.setStringPainted(true);

        progressLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        progressLabel.setToolTipText("");

        packageGen.setText("Package Diagram");
        packageGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                packageGenActionPerformed(evt);
            }
        });

        jLabel1.setText("Generate Diagram :");

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Load XMI file");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        loadCSV.setText("Load CSV file");
        loadCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCSVActionPerformed(evt);
            }
        });
        fileMenu.add(loadCSV);

        loadIndesignPattern.setText("Load Indesign Pattern");
        loadIndesignPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadIndesignPatternActionPerformed(evt);
            }
        });
        fileMenu.add(loadIndesignPattern);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        cutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        settingsMenu.setText("Settings");

        XmiUMLVersion.setText("XMI");

        xmiOption.add(UML1X_XMI1_0);
        UML1X_XMI1_0.setSelected(true);
        UML1X_XMI1_0.setText("UML1.x and XMI 1.0 ");
        XmiUMLVersion.add(UML1X_XMI1_0);

        xmiOption.add(UML1X_XMI1_1_3);
        UML1X_XMI1_1_3.setText("UML1.x and XMI 1.1-1.3");
        XmiUMLVersion.add(UML1X_XMI1_1_3);

        xmiOption.add(UML2X_XMI2_0_2_1);
        UML2X_XMI2_0_2_1.setText("UML2.x and XMI 2.0/2.1");
        XmiUMLVersion.add(UML2X_XMI2_0_2_1);

        settingsMenu.add(XmiUMLVersion);

        inDesignPatternSourceMenu.setText("InDesign Pattern Source");

        inDesignSource.add(manualSelectionMenuItem);
        manualSelectionMenuItem.setSelected(true);
        manualSelectionMenuItem.setText("Manual Selection");
        inDesignPatternSourceMenu.add(manualSelectionMenuItem);

        inDesignSource.add(patternFileMenuItem);
        patternFileMenuItem.setText("Use Pattern File");
        patternFileMenuItem.setEnabled(false);
        inDesignPatternSourceMenu.add(patternFileMenuItem);

        settingsMenu.add(inDesignPatternSourceMenu);
        settingsMenu.add(jSeparator1);

        classifierMenu.setText("Choose Classifier");

        classifiers.add(randomForestMenuItem);
        randomForestMenuItem.setSelected(true);
        randomForestMenuItem.setText("RandomForest");
        classifierMenu.add(randomForestMenuItem);

        classifiers.add(knn1MenuItem);
        knn1MenuItem.setText("KNN (1)");
        classifierMenu.add(knn1MenuItem);

        classifiers.add(naiveBayesMenuItem);
        naiveBayesMenuItem.setText("NaiveBayes");
        classifierMenu.add(naiveBayesMenuItem);

        settingsMenu.add(classifierMenu);

        testOption.setText("Test Option");

        refineOptimize.setText("Refine-Optimize");
        testOption.add(refineOptimize);
        testOption.add(jSeparator3);

        testOptionRButton.add(crossVal10Folds);
        crossVal10Folds.setSelected(true);
        crossVal10Folds.setText("Cross-Validation (10 Folds)");
        testOption.add(crossVal10Folds);

        testOptionRButton.add(crossValLeave1Out);
        crossValLeave1Out.setText("Cross-Validation (Leave 1 out)");
        testOption.add(crossValLeave1Out);

        testOptionRButton.add(halfhalfMethod);
        halfhalfMethod.setText("50/50 Method");
        halfhalfMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                halfhalfMethodActionPerformed(evt);
            }
        });
        testOption.add(halfhalfMethod);

        testOptionRButton.add(trainingSet);
        trainingSet.setText("Use Training Set");
        testOption.add(trainingSet);

        settingsMenu.add(testOption);

        predictorSet.setText("PredictorSet");

        designMetrics.setSelected(true);
        designMetrics.setText("Design Metrics");
        predictorSet.add(designMetrics);

        textMetrics.setText("Text Metrics");
        predictorSet.add(textMetrics);

        settingsMenu.add(predictorSet);

        textMiningFileChooser.setText("Text Information File");
        textMiningFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMiningFileChooserActionPerformed(evt);
            }
        });
        settingsMenu.add(textMiningFileChooser);

        menuBar.add(settingsMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CouplingTab, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkSuggestion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wekaProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(packageGen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GenDiagram))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xmiFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(xmiFileName)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addComponent(CouplingTab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(GenDiagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(packageGen)
                        .addComponent(jLabel1))
                    .addComponent(wekaProcess)
                    .addComponent(checkSuggestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed

        boolean choosed = false;
        //Read XMI file
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            xmiFileName.setText(file.getAbsolutePath());
            choosed = true;
        } else {
            System.out.println("File access cancelled by user.");
        }

        if (choosed == true){
            progressLabel.setText("Status : Processing");
            BufferedReader preReader = null;

            try {
                preReader = new BufferedReader(new FileReader(xmiFileName.getText()));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            String[] versionStrs = new String[100];
            String preLine = null;
            int i = 0;
            try {
                while ((preLine = preReader.readLine()) != null && i < 100) {
                    versionStrs[i++] = preLine;
                }
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            i = 0;
            String xmlVersion = null, xmiVersion = null, xmiMetaVersion = null;
            while(i < 100 && versionStrs[i] != null) {
                String[] tmp = versionStrs[i].split("\\<");
                for(int j = 0; j < tmp.length; ++j) {
                    if(tmp[j].startsWith("?xml version=")) {
                        xmlVersion = tmp[j].substring(14, 17);
                    break;
                    }
                }
                for(int j = 0; j < tmp.length; ++j) {
                    if(tmp[j].startsWith("XMI xmi.version=")) {
                        xmiVersion = tmp[j].substring(17, 20);
                        break;
                        }
                    }
                for(int j = 0; j < tmp.length; ++j) {
                    if(tmp[j].startsWith("XMI.metamodel xmi.name=")) {
                        xmiMetaVersion = tmp[j].substring(42, 45);
                        break;
                        }
                    }
                    i++;
            }

            System.out.println("Xml version is " + xmlVersion);
            System.out.println("Xmi version is " + xmiVersion);
            System.out.println("Xmi meta version is " + xmiMetaVersion);
            //Initialize SDMetrics
            String metaModelURL = "";//sdmetrics";  // metamodel definition to use
            String xmiTransURL = "";   // XMI tranformations to use
            String xmiFile = xmiFileName.getText(); // XMI file with the UML model
            String metricsURL = "";

            //SDMetrics Configuration

            if (UML1X_XMI1_0.isSelected()){
                metaModelURL = "metamodel.xml";//sdmetrics";  // metamodel definition to use
                xmiTransURL = "xmiTrans1_0.xml";   // XMI tranformations to use
                metricsURL = "metrics.xml";
                System.out.println("UML 1.X XMI 1.0");
            }
            if (UML1X_XMI1_1_3.isSelected()){
                metaModelURL = "metamodel.xml";//sdmetrics";  // metamodel definition to use
                xmiTransURL = "xmiTrans1_1.xml";   // XMI tranformations to use
                metricsURL = "metrics.xml";
                System.out.println("UML 1.X XMI 1.1-1.3");
            }
            if (UML2X_XMI2_0_2_1.isSelected()){
                metaModelURL = "metamodel2.xml";//sdmetrics";  // metamodel definition to use
                xmiTransURL = "xmiTrans2_0.xml";   // XMI tranformations to use
                metricsURL = "metrics2.xml";
                System.out.println("UML 2.0 XMI 2.1");
            }

            top.removeAllChildren();
            Enumeration en;
            DefaultMutableTreeNode parent;
            DefaultMutableTreeNode tmp;

            progressBar.setValue(5);
            progressBar.setStringPainted(true);


            String command = "java -jar SDMetrics.jar -xmi " +  xmiFile  + " -filter #.java -filter #.javax -filter #.org.xml -f csv data";
            Process process = null;
            try {
                    String s;
                    process = Runtime.getRuntime().exec(command);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while((s=bufferedReader.readLine()) != null)
                    System.out.println(s);
                    process.waitFor();
                } catch (IOException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }   catch (InterruptedException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader("data_Class.csv"));

                } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            progressBar.setValue(20);
            progressBar.setStringPainted(true);

            String rawCSV = "data_Class.csv";
            String [] rowInfo = null;
            CSVReader csvReader;
            try {
                csvReader = new CSVReader (new FileReader(rawCSV));
                try {
                    rowInfo = csvReader.readNext();
                } catch (IOException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

            progressBar.setValue(50);
            progressBar.setStringPainted(true);

            try {
                numRowData = 1;
                classArray = new ArrayList<String>();
                classArrayForPack = new ArrayList<String>();
                while ((rowInfo = csvReader.readNext())!= null){
                    numRowData ++;
                    parent = top;
                    classArrayForPack.add(rowInfo[0]);
                    int dotInd = rowInfo[0].lastIndexOf(".");
                    String sub = rowInfo[0].substring(dotInd + 1);
                    classArray.add(sub);
                    String[] list = rowInfo[0].split("\\.");

                    en = top.preorderEnumeration();
                    int x = 0;
                    while(en.hasMoreElements()) {
                            tmp = (DefaultMutableTreeNode) en.nextElement();
                        if (x < (list.length)){
                            if(tmp.toString().equals(list[x]) && tmp.getLevel() == (x + 1)) {
                                parent = tmp;
                                x++;
                                }
                            }
                        }
                        for(; x < list.length; ++x) {
                            tmp = new DefaultMutableTreeNode(list[x]);
                            parent.add(tmp);
                            parent = tmp;
                        }
                }
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

        progressBar.setValue(70);
        progressBar.setStringPainted(true);

        //refreshing tree view
        metricsSelection = new javax.swing.JTree(top);
        checkTreeManager = new CheckTreeManager(metricsSelection);
        jScrollPane1.setViewportView(metricsSelection);

        String line = null;
        String[] dataLines = new String[numRowData];
        i = 0;
        try {
            while ((line = reader.readLine()) != null) {
                dataLines[i++] = line;         
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        progressBar.setValue(90);
        progressBar.setStringPainted(true);

        rawData = new ArrayList<String>();
        for(i = 1; i < dataLines.length; ++i) {
            rawData.add(dataLines[i]);
            }
        progressBar.setValue(0);
        progressLabel.setText("Status : Done");
        csvFileInput = false;
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    @SuppressWarnings("empty-statement")
    private void wekaProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wekaProcessActionPerformed
        //Get checked tree paths.               
        int i = 0;
        int j = 0;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreePath[] paths = selectionModel.getSelectionPaths();
                
        if (selectionModel.isSelectionEmpty() == true){
            JOptionPane.showMessageDialog(null, "empty");
        } else {

            for(TreePath p : paths) {
                System.out.println( p.toString());
            }
            String[] inDesigns = null;

            if (manualSelectionMenuItem.isSelected()) {
                TreePath checkedPaths[] = selectionModel.getSelectionPaths();
                inDesigns = new String[checkedPaths.length];

                for(TreePath tp : checkedPaths) {

                    Object[] singlePath = tp.getPath();
                    String tmp = singlePath[1].toString();
                    System.out.println("package ： " +
                            singlePath[singlePath.length - 2].toString());
                    System.out.println("classname : " +
                            singlePath[singlePath.length - 1].toString());

                    for(i = 2; i < singlePath.length; ++i) {
                        tmp = tmp + "." + singlePath[i];
                    }
                    inDesigns[j++] = tmp;
                }
            } else if (patternFileMenuItem.isSelected()) {
                   BufferedReader reader = null;
                   try {
                        reader = new BufferedReader(new FileReader(inDesignPatternFileName));

                   } catch (FileNotFoundException ex) {
                   Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   String line = null;
                   List<String> inDesignList =  new ArrayList<String>();
                   i = 0;
                   try {
                        while ((line = reader.readLine()) != null) {
                            inDesignList.add(line);
                       }
                    } catch (IOException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   inDesigns = (String[]) inDesignList.toArray();
               }

            //Read data from file generated from SDMetrics.
            BufferedReader reader = null;
            try {
                if (csvFileInput == true){
                    reader = new BufferedReader(new FileReader(xmiFileName.getText()));    
                } else
                    reader = new BufferedReader(new FileReader("data_Class.csv"));

            } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Problem With Opening File");
            }
            String line = null;
            String[] dataLines = new String[numRowData];
            i = 0;

            try {
                while ((line = reader.readLine()) != null) {
                    dataLines[i++] = line;
                    //dataLines[i] = line;
                }
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                progressLabel.setText("Status : Failed");
                progressBar.setValue(0);
            }
            
            progressLabel.setText("Status : Data Processing");
            progressBar.setValue(20);
            
            j = 0;
            String[] sepLine = dataLines[j].split("\\,");
            
            if (csvFileInput == false){
                dataLines[j] = sepLine[0] +  "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                    + sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
            } else {            
                dataLines[j] = sepLine[0] +  "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                    + sepLine[6] + ","+ sepLine[7] + ","+ sepLine[8] + ","+ sepLine[9] + ","+ sepLine[10] + ","+ sepLine[11];
            }
            
            //Include all needed attributes here
            dataLines[j] = dataLines[j] + ",InDesign";
            j++;
            boolean flagOfInDesign = false;
            for(; j < dataLines.length; j++) {
            //for(j = 0; j < dataLines.length; j++) {
                flagOfInDesign = false;
                for(i = 0; i < inDesigns.length; ++i) {
                    if(dataLines[j].startsWith(inDesigns[i])) {
                        flagOfInDesign = true;
                    }
                }
                if(flagOfInDesign) {
                    if (csvFileInput == false){ 
                        sepLine = dataLines[j].split("\\,");
                        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                        + sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                        dataLines[j] = dataLines[j] + ",Y";
                    } else if (csvFileInput == true) {
                        sepLine = dataLines[j].split("\\,");
                        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                        + sepLine[6] + ","+ sepLine[7] + ","+ sepLine[8] + ","+ sepLine[9] + ","+ sepLine[10] + ","+ sepLine[11];
                        dataLines[j] = dataLines[j] + ",Y";    
                    }

                }else {
                    if (csvFileInput == false){ 
                        sepLine = dataLines[j].split("\\,");
                        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                        + sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                        dataLines[j] = dataLines[j] + ",N";
                    } else if (csvFileInput == true){
                        sepLine = dataLines[j].split("\\,");
                        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                        + sepLine[6] + ","+ sepLine[7] + ","+ sepLine[8] + ","+ sepLine[9] + ","+ sepLine[10] + ","+ sepLine[11];
                        dataLines[j] = dataLines[j] + ",N";    
                    }
                 }
            }

            progressBar.setValue(30);

            System.out.println("Dataline size : " + dataLines.length);
            for(i = 0; i < dataLines.length; ++i) {
                dataLines[i] = dataLines[i].replaceAll("\\s", "_");
                System.out.println(dataLines[i]);
            }

            Path path, pathForTrain, pathFirst, pathLast; 
            List<String> ls, ls2, ls3;
            
            //This if/else distinguish the 50/50 method with the full training set method.
            if(halfhalfMethod.isSelected()) {
                 path = Paths.get("data_Class_InDesign.csv");
                 pathFirst = Paths.get("data_FirstPart.csv");
                 pathLast = Paths.get("data_LastPart.csv");
            pathForTrain = Paths.get("data_Training.csv");
            
                ls = new ArrayList<String>();
                ls2 = new ArrayList<String>();
                ls3 = new ArrayList<String>();
                
            for(i = 0; i < dataLines.length; ++i) {
                ls.add(dataLines[i]);
            }
            for(i = 0; i < dataLines.length/2 + 1; ++i) {
                ls2.add(dataLines[i]);
            }
            ls3.add(dataLines[0]);
            for(i = dataLines.length/2 + 1; i < dataLines.length; ++i) {
                ls3.add(dataLines[i]);
            }

            try {
                Files.write(path, ls, StandardCharsets.UTF_8);
                Files.write(pathFirst, ls2, StandardCharsets.UTF_8);
                Files.write(pathLast, ls3, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error writing file");
                progressLabel.setText("Status : Failed");
                progressBar.setValue(0);
            }
            
        }else{
            path = Paths.get("data_Class_InDesign.csv");
            pathForTrain = Paths.get("data_Training.csv");
            
                ls = new ArrayList<String>();
                ls2 = new ArrayList<String>();
            for(i = 0; i < dataLines.length; ++i) {
                ls.add(dataLines[i]);
            }
            ls2 = ls;

            try {
                Files.write(path, ls, StandardCharsets.UTF_8);
                Files.write(pathForTrain, ls2, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error writing file");
                progressLabel.setText("Status : Failed");
                progressBar.setValue(0);
            }
            }
            
            ////////////////////training and text data read finish/////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////
            boolean selected = false;
            int inputType = 0;
            if (textMetrics.isSelected() == true && designMetrics.isSelected() == true){
                inputType = 3;
                Object[] projectCandidate =  lisProj.setProjectList(textInfo.getTextCSVFilename());
                String si;
                si = (String)JOptionPane.showInputDialog( this, "Please Select Your Project: ",
                 "Text Information Project Selection", JOptionPane.PLAIN_MESSAGE, null, projectCandidate,
                 projectCandidate[0].toString());

                progressLabel.setText("Status : Text Processing");
                           
                int choosen = -1;
                for (int loop = 0; loop < projectCandidate.length; loop ++){
                    if (projectCandidate[loop].toString().equals(si)){
                        choosen = loop;
                    }
                }
                // ------  text metrics source file----------
                // to fix
                if (choosen >= 0 ){
                     if(halfhalfMethod.isSelected()) {
                        TextMiningMetrics textMet_FirstPart = new TextMiningMetrics();
                        textMet_FirstPart.textProcess("data_FirstPart.csv", textInfo.getTextCSVFilename(), choosen, inputType);
                        
                        TextMiningMetrics textMet_LastPart = new TextMiningMetrics();
                        
                        textMet_LastPart.textProcess("data_LastPart.csv", textInfo.getTextCSVFilename(), choosen, inputType);
                        
                        BufferedReader readerTmet_FirstPart = null;
                        BufferedReader readerTmet_LastPart = null;
                        
                        try {
                            readerTmet_FirstPart = new BufferedReader(new FileReader("data_FirstPart.csv"));
                            readerTmet_LastPart = new BufferedReader(new FileReader("data_LastPart.csv"));

                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        String textLine = "";
                        int t = 0;

                        try {
                            while ((textLine = readerTmet_FirstPart.readLine()) != null) {
                                textLine = textLine.replaceAll("\"", "");
                                dataLines[t++] = textLine;
                            }
                            textLine = readerTmet_LastPart.readLine();
                            while ((textLine = readerTmet_LastPart.readLine()) != null) {
                                textLine = textLine.replaceAll("\"", "");
                                dataLines[t++] = textLine;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    //////////////////////////////////////////////////////////////////////////////////////////////
                        //////rest for the old method
                     }else {
                    TextMiningMetrics textMet = new TextMiningMetrics();
                    textMet.textProcess("data_Training.csv", textInfo.getTextCSVFilename(), choosen, inputType);

                    BufferedReader readerTmet = null;

                    try {
                    readerTmet = new BufferedReader(new FileReader("data_Training.csv"));

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    String textLine = "";
                    int t = 0;

                    try {
                    while ((textLine = readerTmet.readLine()) != null) {
                        textLine = textLine.replaceAll("\"", "");
                        dataLines[t++] = textLine;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                selected = true;
            }

            else if (designMetrics.isSelected() == true){
                inputType = 2;
                selected = true;
            }

            else if (textMetrics.isSelected() == true){
                inputType = 1;
                Object[] projectCandidate =  lisProj.setProjectList(textInfo.getTextCSVFilename());
                String si;
                si = (String)JOptionPane.showInputDialog( this, "Please Select Your Project: ",
                 "Text Information Project Selection", JOptionPane.PLAIN_MESSAGE, null, projectCandidate,
                 projectCandidate[0].toString());

                int choosen = -1;

                for (int loop = 0; loop < projectCandidate.length; loop ++){
                    if (projectCandidate[loop].toString().equals(si)){
                        choosen = loop;
                    }
                }
                if (choosen >= 0 ){
                    if(halfhalfMethod.isSelected()) {
                        TextMiningMetrics textMet_FirstPart = new TextMiningMetrics();
                        textMet_FirstPart.textProcess("data_FirstPart.csv", textInfo.getTextCSVFilename(), choosen, inputType);
                        
                        TextMiningMetrics textMet_LastPart = new TextMiningMetrics();                   
                        textMet_LastPart.textProcess("data_LastPart.csv", textInfo.getTextCSVFilename(), choosen, inputType);
                        
                        BufferedReader readerTmet_FirstPart = null;
                        BufferedReader readerTmet_LastPart = null;
                        
                        try {
                            readerTmet_FirstPart = new BufferedReader(new FileReader("data_FirstPart.csv"));
                            readerTmet_LastPart = new BufferedReader(new FileReader("data_LastPart.csv"));

                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        String textLine = "";
                        int t = 0;

                        try {
                            while ((textLine = readerTmet_FirstPart.readLine()) != null) {
                                textLine = textLine.replaceAll("\"", "");
                                dataLines[t++] = textLine;
                            }
                            while ((textLine = readerTmet_LastPart.readLine()) != null) {
                                textLine = textLine.replaceAll("\"", "");
                                dataLines[t++] = textLine;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }     
                     }else {
                    TextMiningMetrics textMet = new TextMiningMetrics();
                    textMet.textProcess("data_Training.csv", textInfo.getTextCSVFilename(), choosen, inputType);

                    BufferedReader readerTmet = null;

                    try {
                    readerTmet = new BufferedReader(new FileReader("data_Training.csv"));

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    String textLine = "";
                    int t = 0;

                    try {
                    while ((textLine = readerTmet.readLine()) != null) {
                        textLine = textLine.replaceAll("\"", "");
                        dataLines[t++] = textLine;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                selected = true;
            }
            
            progressLabel.setText("Status : Training");
            progressBar.setValue(50);

        if (selected == true){

            CSVLoader loader = new CSVLoader();
            Instances data = null;
            Instances trainingData = null;
            Instances filterredData = null;
            Instances filterredTrainingData = null;
            Instances structure = null;
            Instances filterredstructure = null;
            
            if(halfhalfMethod.isSelected()) {
            File inputFile = new File("data_FirstPart.csv");
            File trainingFile = new File("data_LastPart.csv");
            
            try {
                loader.setSource(inputFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                structure = loader.getStructure();
                data = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                loader.setSource(trainingFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                trainingData = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] options = new String[2];
            options[0] = "-R";                // "range"
            options[1] = "1";                 // first attribute
            Remove remove = new Remove();     // new instance of filter
            try {
                remove.setOptions(options);   // set options
                remove.setInputFormat(data);  // inform filter about dataset **AFTER** setting options
                remove.setInputFormat(trainingData);
                filterredData = Filter.useFilter(data, remove);
       //         filterredData = data;
                filterredData.setClassIndex(filterredData.numAttributes() - 1);
                filterredTrainingData = Filter.useFilter(trainingData, remove);
       //         filterredTrainingData = trainingData;
                filterredTrainingData.setClassIndex(filterredTrainingData.numAttributes() - 1);
                filterredstructure = Filter.useFilter(structure, remove);
       //         filterredstructure = structure;
                filterredstructure.setClassIndex(filterredData.numAttributes() - 1);
       //         filterredData.delete(0);
       //         filterredTrainingData.delete(0);
       //       filterredstructure.delete(0);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuffer forPredictionsPrinting_First = new StringBuffer();
           // StringBuffer forPredictionsPrinting2 = new StringBuffer(); // added
            CSV output = new CSV();
            //CSV output2 = new CSV(); // added
            output.setBuffer(forPredictionsPrinting_First);
            //boolean s1 = output.getOutputDistribution();
            //output.getOutputDistribution();
            output.setOutputFile(new File("prediction_FirstPart.csv"));
            
                        Evaluation eval = null;
            resultOutput.setText("");
            System.out.println("NUMBER OF INSTANCE : " + (filterredData.size()));

            progressLabel.setText("Status : Testing");
            progressBar.setValue(70);
            
            if (randomForestMenuItem.isSelected()) {
                //configure options for future use;
                try {
                 options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 2");
              } catch (Exception ex) {
                   Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
              }

               RandomForest rf = new RandomForest();
     //          rf.setDebug(false);
     //          rf.setMaxDepth(0);
     //          rf.setNumFeatures(0);
     //          rf.setNumTrees(10);
     //          rf.setSeed(1);

                try{
                    rf.buildClassifier(filterredTrainingData); // set the classifier test data
                    eval = new Evaluation(filterredData);
                    //StringBuffer predictions = new StringBuffer();
                    //predictions = java.lang.StringBuffer.new;
                    
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredTrainingData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("1"), true);
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("first,last"), true);
                        //
                        //String fromPrediction = "";//new String();
                        //fromPrediction = predictions.toString();
                        
                        //System.out.println("This is prediction = " + fromPrediction);
                        System.out.println("LEAVE ONE OUT");
                    
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(rf, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(rf, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(rf, filterredData);
                    //output.p
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                resultOutput.append(rf.globalInfo() +"\n");

                double[] getDistVal = null;
                int loopDist = 0;
                
                //System.out.println("Distribution For Instance : ");
                /*for(loopDist = 0; loopDist < filterredData.numInstances(); loopDist++)
                {
                    try {
                        getDistVal = rf.distributionForInstance(filterredData.instance(loopDist));                      
                    } catch (Exception ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(loopDist + " : " + Utils.arrayToString(getDistVal));
                }*/
                
            } else if (knn1MenuItem.isSelected()) {
              //   try {
              //      options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 1");
              //  } catch (Exception ex) {
              //      Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
              //  }
               IBk knn = new IBk();

                try {
                    knn.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(knn, filterredData, filterredData.size(), new Random(1));
                        System.out.println("LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(knn, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(knn, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                    try {
                        output.setHeader(filterredstructure);
                        output.print(knn, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(knn.globalInfo() + "\n");
                
            }else if(naiveBayesMenuItem.isSelected())   {

                NaiveBayes nb = new NaiveBayes();
                try{
                    nb.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(nb, filterredData, filterredData.size(), new Random(1));
                        System.out.println("TESTING METHOD : LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(nb, filterredData, 10, new Random(1));
                        System.out.println("TESTING METHOD : 10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(nb, filterredData);
                        System.out.println("TESTING METHOD : USE TRAINING SET");
                    }
                }catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(nb, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(nb.globalInfo() + "\n");
            }
               resultOutput.append(eval.toSummaryString() + "\n");
                try {
                    resultOutput.append(eval.toMatrixString() + "\n");
                    resultOutput.append(eval.toClassDetailsString() + "\n");
                    //eval.to
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                //For Part two
                
                inputFile = new File("data_LastPart.csv");
            trainingFile = new File("data_FirstPart.csv");
            
            try {
                loader.setSource(inputFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                structure = loader.getStructure();
                data = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                loader.setSource(trainingFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                trainingData = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

           
            try {
                remove.setOptions(options);   // set options
                remove.setInputFormat(data);  // inform filter about dataset **AFTER** setting options
                remove.setInputFormat(trainingData);
                filterredData = Filter.useFilter(data, remove);
       //         filterredData = data;
                filterredData.setClassIndex(filterredData.numAttributes() - 1);
                filterredTrainingData = Filter.useFilter(trainingData, remove);
       //         filterredTrainingData = trainingData;
                filterredTrainingData.setClassIndex(filterredTrainingData.numAttributes() - 1);
                filterredstructure = Filter.useFilter(structure, remove);
       //         filterredstructure = structure;
                filterredstructure.setClassIndex(filterredData.numAttributes() - 1);
       //         filterredData.delete(0);
       //         filterredTrainingData.delete(0);
       //       filterredstructure.delete(0);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuffer forPredictionsPrinting_Last = new StringBuffer();
           // StringBuffer forPredictionsPrinting2 = new StringBuffer(); // added
            output = new CSV();
            //CSV output2 = new CSV(); // added
            output.setBuffer(forPredictionsPrinting_Last);
            //boolean s1 = output.getOutputDistribution();
            //output.getOutputDistribution();
            output.setOutputFile(new File("prediction_LastPart.csv"));
            
                        eval = null;
            resultOutput.setText("");
            System.out.println("NUMBER OF INSTANCE : " + (filterredData.size()));

            progressLabel.setText("Status : Testing");
            progressBar.setValue(70);
            
            if (randomForestMenuItem.isSelected()) {
                //configure options for future use;
                try {
                 options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 2");
              } catch (Exception ex) {
                   Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
              }

               RandomForest rf = new RandomForest();
     //          rf.setDebug(false);
     //          rf.setMaxDepth(0);
     //          rf.setNumFeatures(0);
     //          rf.setNumTrees(10);
     //          rf.setSeed(1);

                try{
                    rf.buildClassifier(filterredTrainingData); // set the classifier test data
                    eval = new Evaluation(filterredData);
                    //StringBuffer predictions = new StringBuffer();
                    //predictions = java.lang.StringBuffer.new;
                    
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredTrainingData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("1"), true);
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("first,last"), true);
                        //
                        //String fromPrediction = "";//new String();
                        //fromPrediction = predictions.toString();
                        
                        //System.out.println("This is prediction = " + fromPrediction);
                        System.out.println("LEAVE ONE OUT");
                    
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(rf, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(rf, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(rf, filterredData);
                    //output.p
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                resultOutput.append(rf.globalInfo() +"\n");

                double[] getDistVal = null;
                int loopDist = 0;
                
                //System.out.println("Distribution For Instance : ");
                /*for(loopDist = 0; loopDist < filterredData.numInstances(); loopDist++)
                {
                    try {
                        getDistVal = rf.distributionForInstance(filterredData.instance(loopDist));                      
                    } catch (Exception ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(loopDist + " : " + Utils.arrayToString(getDistVal));
                }*/
                
            } else if (knn1MenuItem.isSelected()) {
              //   try {
              //      options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 1");
              //  } catch (Exception ex) {
              //      Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
              //  }
               IBk knn = new IBk();

                try {
                    knn.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(knn, filterredData, filterredData.size(), new Random(1));
                        System.out.println("LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(knn, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(knn, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                    try {
                        output.setHeader(filterredstructure);
                        output.print(knn, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(knn.globalInfo() + "\n");
                
            }else if(naiveBayesMenuItem.isSelected())   {

                NaiveBayes nb = new NaiveBayes();
                try{
                    nb.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(nb, filterredData, filterredData.size(), new Random(1));
                        System.out.println("TESTING METHOD : LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(nb, filterredData, 10, new Random(1));
                        System.out.println("TESTING METHOD : 10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(nb, filterredData);
                        System.out.println("TESTING METHOD : USE TRAINING SET");
                    }
                }catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(nb, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(nb.globalInfo() + "\n");
            }
               resultOutput.append(eval.toSummaryString() + "\n");
                try {
                    resultOutput.append(eval.toMatrixString() + "\n");
                    resultOutput.append(eval.toClassDetailsString() + "\n");
                    //eval.to
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            //test:
                System.out.println("Test something\n");
             
                //test over
             String tmpstr = forPredictionsPrinting_First.toString() + forPredictionsPrinting_Last.delete(0, 40).toString();
            predictResultData = tmpstr.split("\n");
            
            }else { //old method
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //whole training/ whole test/////////////////////////////////////////////////////////////////////////////
            File inputFile = new File("data_Training.csv");
            File trainingFile = new File("data_Training.csv");
            try {
                loader.setSource(inputFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                structure = loader.getStructure();
                data = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                loader.setSource(trainingFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                trainingData = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] options = new String[2];
            options[0] = "-R";                // "range"
            options[1] = "1";                 // first attribute
            Remove remove = new Remove();     // new instance of filter
            try {
                remove.setOptions(options);   // set options
                remove.setInputFormat(data);  // inform filter about dataset **AFTER** setting options
                remove.setInputFormat(trainingData);
                filterredData = Filter.useFilter(data, remove);
       //         filterredData = data;
                filterredData.setClassIndex(filterredData.numAttributes() - 1);
                filterredTrainingData = Filter.useFilter(trainingData, remove);
       //         filterredTrainingData = trainingData;
                filterredTrainingData.setClassIndex(filterredTrainingData.numAttributes() - 1);
                filterredstructure = Filter.useFilter(structure, remove);
       //         filterredstructure = structure;
                filterredstructure.setClassIndex(filterredData.numAttributes() - 1);
       //         filterredData.delete(0);
       //         filterredTrainingData.delete(0);
       //       filterredstructure.delete(0);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuffer forPredictionsPrinting = new StringBuffer();
           // StringBuffer forPredictionsPrinting2 = new StringBuffer(); // added
            CSV output = new CSV();
            //CSV output2 = new CSV(); // added
            output.setBuffer(forPredictionsPrinting);
            //boolean s1 = output.getOutputDistribution();
            //output.getOutputDistribution();
            output.setOutputFile(new File("prediction.csv"));
            
            //output.setBuffer(forPredictionsPrinting2);          // added
            //output.setOutputFile(new File("prediction2.csv"));  // added
            
            Evaluation eval = null;
            resultOutput.setText("");
            System.out.println("NUMBER OF INSTANCE : " + (filterredData.size()));

            progressLabel.setText("Status : Testing");
            progressBar.setValue(70);
            
            if (randomForestMenuItem.isSelected()) {
                //configure options for future use;
                try {
                 options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 2");
              } catch (Exception ex) {
                   Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
              }

               RandomForest rf = new RandomForest();
     //          rf.setDebug(false);
     //          rf.setMaxDepth(0);
     //          rf.setNumFeatures(0);
     //          rf.setNumTrees(10);
     //          rf.setSeed(1);

                try{
                    rf.buildClassifier(filterredTrainingData); // set the classifier test data
                    eval = new Evaluation(filterredData);
                    //StringBuffer predictions = new StringBuffer();
                    //predictions = java.lang.StringBuffer.new;
                    
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredTrainingData.size(), new Random(1));
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("1"), true);
                        //eval.crossValidateModel(rf, filterredData, filterredData.size(), new Random(1), output2, new Range("first,last"), true);
                        //
                        //String fromPrediction = "";//new String();
                        //fromPrediction = predictions.toString();
                        
                        //System.out.println("This is prediction = " + fromPrediction);
                        System.out.println("LEAVE ONE OUT");
                    
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(rf, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(rf, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(rf, filterredData);
                    //output.p
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                resultOutput.append(rf.globalInfo() +"\n");

                double[] getDistVal = null;
                int loopDist = 0;
                
                //System.out.println("Distribution For Instance : ");
                /*for(loopDist = 0; loopDist < filterredData.numInstances(); loopDist++)
                {
                    try {
                        getDistVal = rf.distributionForInstance(filterredData.instance(loopDist));                      
                    } catch (Exception ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(loopDist + " : " + Utils.arrayToString(getDistVal));
                }*/
                
            } else if (knn1MenuItem.isSelected()) {
              //   try {
              //      options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 1");
              //  } catch (Exception ex) {
              //      Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
              //  }
               IBk knn = new IBk();

                try {
                    knn.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(knn, filterredData, filterredData.size(), new Random(1));
                        System.out.println("LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(knn, filterredData, 10, new Random(1));
                        System.out.println("10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(knn, filterredData);
                        System.out.println("USE TRAINING SET");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                    try {
                        output.setHeader(filterredstructure);
                        output.print(knn, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(knn.globalInfo() + "\n");
                
            }else if(naiveBayesMenuItem.isSelected())   {

                NaiveBayes nb = new NaiveBayes();
                try{
                    nb.buildClassifier(filterredTrainingData);
                    eval = new Evaluation(filterredData);
                    if (crossValLeave1Out.isSelected()){
                        eval.crossValidateModel(nb, filterredData, filterredData.size(), new Random(1));
                        System.out.println("TESTING METHOD : LEAVE ONE OUT");
                    }
                    else if (crossVal10Folds.isSelected()){
                        eval.crossValidateModel(nb, filterredData, 10, new Random(1));
                        System.out.println("TESTING METHOD : 10 FOLDS CV");
                    }
                    else if (trainingSet.isSelected() || halfhalfMethod.isSelected()){
                        eval.evaluateModel(nb, filterredData);
                        System.out.println("TESTING METHOD : USE TRAINING SET");
                    }
                }catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    output.setHeader(filterredstructure);
                    output.print(nb, filterredData);
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultOutput.append(nb.globalInfo() + "\n");
            }
               resultOutput.append(eval.toSummaryString() + "\n");
                try {
                    resultOutput.append(eval.toMatrixString() + "\n");
                    resultOutput.append(eval.toClassDetailsString() + "\n");
                    //eval.to
                } catch (Exception ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            
            
         
                //test:
                System.out.println("Test something\n");
                StringBuffer tmpBuffer = new StringBuffer(forPredictionsPrinting);
                System.out.print(tmpBuffer.toString() + tmpBuffer.delete(0, 40).toString());
                //test over
            predictResultData = forPredictionsPrinting.toString().split("\n");
            }    
            
            
            ////////////////////////////////////////////////////////////////////////////////////
            //analyse part finished/////////////////////////////
            //post-process begin////////////////////////////////////////////////////////////////
            ////////////////////////////
            
            progressLabel.setText("Status : Output Preparation");
            progressBar.setValue(90);
            
                errByPredict = new String[dataLines.length];
                finalData = new ArrayList<String>();
                dataforYN = new ArrayList<String>();
                dataforNY = new ArrayList<String>();
                dataforNN = new ArrayList<String>();
                for(i = 0; i < predictResultData.length; i++) {
                    String[] tmpSepStr = predictResultData[i].split("\\,");
                    if(i == 0){
                        dataLines[i] = dataLines[i] + "," + "Prediction" + "," + "Probability Distribution";
                        matrixNames = dataLines[i];
                    }
                    if(i != 0 && i < dataLines.length){
                        tmpSepStr[1] = tmpSepStr[1].substring(2);
                        tmpSepStr[2] = tmpSepStr[2].substring(2);
                        //dataLines[i] = dataLines[i+1] + "," + tmpSepStr[2] + "," + tmpSepStr[4];
                        if ("N".equals(tmpSepStr[2])){
                            tmpSepStr[4] = Double.toString(1 - Double.parseDouble(tmpSepStr[4]));
                        }
                        dataLines[i] = dataLines[i] + "," + tmpSepStr[2] + "," + tmpSepStr[4];
                    }
                    // True positive ranking Y,Y
                    if((tmpSepStr[1].equals("Y")) && (tmpSepStr[2].equals("Y"))) {
                        finalData.add(dataLines[i]);
                        Collections.sort(finalData, new PredictRankingDown());
                        // rankingdown - descending
                    }
                    if((tmpSepStr[1].equals("Y")) && (tmpSepStr[2].equals("N"))){
                        dataforYN.add(dataLines[i]);
                        Collections.sort(dataforYN, new PredictRankingUp());
                        // rankingup - ascending
                    }
                    if((tmpSepStr[1].equals("N")) && (tmpSepStr[2].equals("Y"))){
                        dataforNY.add(dataLines[i]);
                        Collections.sort(dataforNY, new PredictRankingDown());
                    }
                    if((tmpSepStr[1].equals("N")) && (tmpSepStr[2].equals("N"))){
                        dataforNN.add(dataLines[i]);
                        Collections.sort(dataforNN, new PredictRankingUp());
                    }
                    if(tmpSepStr[3].equals("+"))
                        errByPredict[i] = dataLines[i];
                    else
                        errByPredict[i] = "";
                }

                finalData.addAll(dataforYN);
                finalData.addAll(dataforNY);
                finalData.addAll(dataforNN);
                
                if(/*!refineOptimize.isSelected()*/true) {
                // sort coupling or in specific text first before sorting using score 
                // this is done to sort the lower score value to be sort according to a popular predictor
                if (designMetrics.isSelected() && textMetrics.isSelected()){
                    Collections.sort(finalData, new TotalDesignMetricsRankingResultAll());
                    Collections.sort(finalData, new SpecClassesRankingResult ());
                    System.out.println("Using Spec Classes Ranking Result");
                } else if (designMetrics.isSelected()){
                    Collections.sort(finalData, new TotalDesignMetricsRankingResult());
                    Collections.sort(finalData, new CouplingRankingResult());
                    System.out.println("Using Coupling Ranking Result");                    
                }
                
                Collections.sort(finalData, new PredictRankingDown());
                resultOutput.append(matrixNames + "\n");
                for (String s : finalData)
                    resultOutput.append(s + "\n");

                path = Paths.get("result.csv");
                ls = new ArrayList<String>();
                ls = finalData;

                try {
                    Files.write(path, ls, StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                }else {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /////start refine optimize//////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    for(i = 0; i < finalData.size(); ++i) {
                        
                    }
                    
                }
            CSVReader openReader;
            String[] readLines;
            Object columnNames[] = { "No", "Class Name", "In Design", "Prediction", "Score"};
            Object columnNamesSize[] = {"No", "Class Name", "NumAttr", "NumOps", "NumPubOps", "Getters", "Setters"};
            Object columnNamesCoupling[] = {"No", "Class Name", "Dep_In", "Dep_Out", "EC_Attr", "IC_Attr", "EC_Par", "IC_Par"};
            Object columnNamesText[] = {"No", "Class Name", "NoKey", "MaxInDoc", "TotalInDoc", "maxInAllClasses", 
                "maxInSpecClasses", "TotalInAllClasses", "TotalInSpecClasses", "WeightInAllClasses", "weightInSpecClasses"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            DefaultTableModel sizePredictor = new DefaultTableModel(columnNamesSize, 0);
            DefaultTableModel couplingPredictor = new DefaultTableModel(columnNamesCoupling, 0);
            DefaultTableModel textPredictor = new DefaultTableModel(columnNamesText, 0);
            classArray.clear();
            classArrayForPack.clear();
            
            int readCounter = 0;
                try {
                    openReader = new CSVReader(new FileReader("result.csv"));
                    try {
                        while ((readLines = openReader.readNext()) != null){
                            model.addRow(new String [] {Integer.toString(readCounter + 1),readLines[0],
                                readLines[readLines.length - 3], readLines[readLines.length - 2], readLines[readLines.length - 1]});
                                int dotIn = readLines[0].lastIndexOf(".");
                                String sub = readLines[0].substring(dotIn + 1);
                                classArray.add(sub);
                                classArrayForPack.add(readLines[0]);
                                
                                if (inputType == 1){
                                    textPredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[1], 
                                    readLines[2], readLines[3], readLines[4], readLines[5], readLines[6], readLines[7], readLines[8], readLines[9]});
                                }
                                
                                else if (inputType == 2){
                                    sizePredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[1], 
                                    readLines[2], readLines[3], readLines[4], readLines[5]});
                                    
                                    couplingPredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[6], 
                                    readLines[7], readLines[8], readLines[9], readLines[10], readLines[11]});                                                                        
                                }
                                
                                else if (inputType == 3){                                                              
                                    textPredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[1], 
                                    readLines[2], readLines[3], readLines[4], readLines[5], readLines[6], readLines[7], readLines[8], readLines[9]});
                                    
                                    sizePredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[10], 
                                    readLines[11], readLines[12], readLines[13], readLines[14]});
                                    
                                    couplingPredictor.addRow(new String [] {Integer.toString(readCounter + 1), readLines[0], readLines[15], 
                                    readLines[16], readLines[17], readLines[18], readLines[19], readLines[20]});                                                                                                                                                                                                                                                 
                                }                            
                            readCounter++;
                        }
                        rankingTable.setModel(model);
                        
                        rankingTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                        rankingTable.getColumnModel().getColumn(1).setPreferredWidth(450);
            
                        if (inputType == 1){
                            textPredictorTable.setModel(textPredictor); 
                            sizePredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            sizePredictorTable.getColumnModel().getColumn(1).setPreferredWidth(450);
                            sizePredictorTable.setModel(new DefaultTableModel());
                            couplingPredictorTable.setModel(new DefaultTableModel());
                        } else if (inputType == 2){
                            sizePredictorTable.setModel(sizePredictor);
                            couplingPredictorTable.setModel(couplingPredictor);
                            sizePredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            sizePredictorTable.getColumnModel().getColumn(1).setPreferredWidth(450);
                            couplingPredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            couplingPredictorTable.getColumnModel().getColumn(1).setPreferredWidth(400); 
                            textPredictorTable.setModel(new DefaultTableModel());                            
                        } else if (inputType == 3){
                            textPredictorTable.setModel(textPredictor);                        
                            sizePredictorTable.setModel(sizePredictor);
                            couplingPredictorTable.setModel(couplingPredictor);
                            sizePredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            sizePredictorTable.getColumnModel().getColumn(1).setPreferredWidth(450);
                            couplingPredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            couplingPredictorTable.getColumnModel().getColumn(1).setPreferredWidth(400);         
                            textPredictorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
                            textPredictorTable.getColumnModel().getColumn(1).setPreferredWidth(380);                        
                        }
                        
                    } catch (IOException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }           
      }
            progressLabel.setText("Status : Done");
            progressBar.setValue(0);
    }//GEN-LAST:event_wekaProcessActionPerformed

    private void loadIndesignPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadIndesignPatternActionPerformed
          // TODO add your handling code here:
      if (csvFileInput == false){          
        int returnVal = inDesignChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = inDesignChooser.getSelectedFile();
             inDesignPatternFileName = file.getAbsolutePath();
        } else {
            System.out.println("File access cancelled by user.");
        }
        patternFileMenuItem.setEnabled(true);
        //start
        CSVReader csvReader = null;
        String csvFilename = "";
        csvFilename = inDesignPatternFileName;
        String [] row = null;

        try{
            List<String> classListLocal = new ArrayList<>();
            if (csvFilename.isEmpty() == false) {
                try {
                    csvReader = new CSVReader (new FileReader(csvFilename));
                    try {
                        row = csvReader.readNext();
                    } catch (IOException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            int count = 0;

            while ((row = csvReader.readNext()) != null){
                String inDesign = row[12];
                if(inDesign.equals("Y"))
                {
                    //int DotIndex = row[0].indexOf(".") + 1;
                    classListLocal.add(row[0]);
                    // classListLocal.add(row[0].substring(DotIndex));
                }
               count++;
                }
        Enumeration en;
        DefaultMutableTreeNode tmp;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreeNode[] tn;
        TreePath tmpPath;
        for(int i = 0; i < classListLocal.size(); ++i) {
            String[] sepNames1 = new String[classListLocal.size()];
            sepNames1[i] = classListLocal.get(i);
            //sepNames1[i] = "EA Model.Class Model." + sepNames1[i];
            //sepNames1[i] = "EA Model." + sepNames1[i];
            String []   sepNames = sepNames1[i].split("\\.");

            System.out.println(i + sepNames1[i]);

            en = top.preorderEnumeration();
               int j = 0;
               while(en.hasMoreElements() ) {
                   tmp = (DefaultMutableTreeNode) en.nextElement();
                   if(tmp.toString().equals(sepNames[j]) && tmp.getLevel() == j + 1) {
                     if(j == sepNames.length - 1) {
                         tn = tmp.getPath();
                         tmpPath = new TreePath(tn);
                        boolean selected = selectionModel.isPathSelected(tmpPath);
                        if(!selected)
                            selectionModel.addSelectionPath(tmpPath);
                         break;
                     }
                     j++;
                    }                 
               }
            }
    }//GEN-LAST:event_loadIndesignPatternActionPerformed
       try {
                csvReader.close();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    }


    private void checkSuggestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkSuggestionActionPerformed
        // TODO add your handling code here:
        String inputValue = JOptionPane.showInputDialog("Please input the suggestion weight (between 1 and 100)");
        int percentage = (Integer.parseInt(inputValue) * (numRowData - 1)) / 100;

        String[] suggestNames = new String[percentage];
        Collections.sort(rawData, new CouplingRanking());
        Collections.sort(rawData, new NumOpsRanking());
        Collections.sort(rawData, new NOCRanking());
        Collections.sort(rawData, new NumPubOpsRanking());
        
        for(int i = 0; i < percentage; ++i) {
            String[] tmp = rawData.get(i).split("\\,");
            suggestNames[i] = tmp[0];
        }
 
        /*String[] suggestNames = new String[percentage * 4];
        Collections.sort(rawData, new NumOpsRanking());
        for(int i = 0; i < percentage; ++i) {
            String[] tmp = rawData.get(i).split("\\,");
            suggestNames[i] = tmp[0];
        }
        Collections.sort(rawData, new NumPubOpsRanking());
        for(int i = percentage; i < 2 * percentage; ++i) {
            String[] tmp = rawData.get(i).split("\\,");
            suggestNames[i] = tmp[0];
        }
        Collections.sort(rawData, new NOCRanking());
        for(int i = 2 * percentage; i < 3 * percentage; ++i) {
            String[] tmp = rawData.get(i).split("\\,");
            suggestNames[i] = tmp[0];
        }
        Collections.sort(rawData, new CouplingRanking());
        for(int i = 3 * percentage; i < 4 * percentage; ++i) {
            String[] tmp = rawData.get(i).split("\\,");
            suggestNames[i] = tmp[0];
        }*/
        Enumeration en;
        DefaultMutableTreeNode tmp;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreeNode[] tn;
        TreePath tmpPath;

//        for(int i = 0; i < 4 * percentage; ++i) {
          for(int i = 0; i < percentage; ++i) {
            String[] sepNames = suggestNames[i].split("\\.");

            en = top.preorderEnumeration();
                           int j = 0;
                           while(en.hasMoreElements() ) {
                             tmp = (DefaultMutableTreeNode) en.nextElement();
                             if(tmp.toString().equals(sepNames[j]) && tmp.getLevel() == j + 1) {
                                 if(j == sepNames.length - 1) {
                                     tn = tmp.getPath();
                                     tmpPath = new TreePath(tn);
                                     boolean selected = selectionModel.isPathSelected(tmpPath, true);
                                     if(!selected)
                                         selectionModel.addSelectionPath(tmpPath);
                                     //selectionModel.addSelectionPaths(new TreePath[] {tmpPath});
                                     break;
                                 }
                                 j++;
                             }
                           }
        }
        TreePath[] paths = selectionModel.getSelectionPaths();

    }//GEN-LAST:event_checkSuggestionActionPerformed

    private void GenDiagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenDiagramActionPerformed
        JFrame f = new JFrame("SAAbs Viewer");
        String xmiName = xmiFileName.getText();
        int UML_XMI_Sel = 0;

        if (UML1X_XMI1_0.isSelected()){
            UML_XMI_Sel = 1;
        }
        if (UML1X_XMI1_1_3.isSelected()){
            UML_XMI_Sel = 2;
        }
        if (UML2X_XMI2_0_2_1.isSelected()){
            UML_XMI_Sel = 3;
        }

        System.out.println("Filename : " + xmiName + " " + UML_XMI_Sel);

    if (xmiName.isEmpty() != true){
        try {
        PaintUML P = new PaintUML();
            try {
                P.convertToPlantUML(xmiName, UML_XMI_Sel, classArray);
                //System.out.println(classArray);
                DisplaySVG app = new DisplaySVG(f, P.getPlantUMLScript(), P.getClassList(),
                        P.getPlantUMLOps(), P.getPlantUMLAttr(), P.getPlantUMLCls(), P.getPlantGSUML());
                // Add components to the frame.
                f.getContentPane().add(app.createComponents());
                f.setSize(400, 400);
                f.setVisible(true);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    else
       JOptionPane.showMessageDialog(null,"Please choose an XMI File");
    }//GEN-LAST:event_GenDiagramActionPerformed

    private void inDesignChecker(String csvFile){
        
        patternFileMenuItem.setEnabled(true);
        //start
        CSVReader csvReader = null;
        String csvFilename = "";
        csvFilename = csvFile;
        String [] row = null;

        try{
            List<String> classListLocal = new ArrayList<>();
            if (csvFilename.isEmpty() == false) {
                try {
                    csvReader = new CSVReader (new FileReader(csvFilename));
                    try {
                        row = csvReader.readNext();
                    } catch (IOException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            int count = 0;

            while ((row = csvReader.readNext()) != null){
                String inDesign = row[12];
                if(inDesign.equals("Y"))
                {
                    classListLocal.add(row[0]);
                }
               count++;
                }
        Enumeration en;
        DefaultMutableTreeNode tmp;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreeNode[] tn;
        TreePath tmpPath;
        for(int i = 0; i < classListLocal.size(); ++i) {
            String[] sepNames1 = new String[classListLocal.size()];
            sepNames1[i] = classListLocal.get(i);
            String []   sepNames = sepNames1[i].split("\\.");

            System.out.println(i + sepNames1[i]);

            en = top.preorderEnumeration();
               int j = 0;
               while(en.hasMoreElements() ) {
                   tmp = (DefaultMutableTreeNode) en.nextElement();
                   if(tmp.toString().equals(sepNames[j]) && tmp.getLevel() == j + 1) {
                     if(j == sepNames.length - 1) {
                         tn = tmp.getPath();
                         tmpPath = new TreePath(tn);
                        boolean selected = selectionModel.isPathSelected(tmpPath);
                        if(!selected)
                            selectionModel.addSelectionPath(tmpPath);
                         break;
                     }
                     j++;
                    }                 
               }
            }
    }                                                   
       try {
                csvReader.close();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void textMiningFileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMiningFileChooserActionPerformed
        //TextInformationSetting setTextInfo = new TextInformationSetting();
        textInfo.setVisible(true);
    }//GEN-LAST:event_textMiningFileChooserActionPerformed

    private void loadCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCSVActionPerformed
       
        boolean choosed = false;
        //read CSV file
        int returnVal = csvFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = csvFileChooser.getSelectedFile();
            xmiFileName.setText(file.getAbsolutePath());
            choosed = true;
        } else {
            System.out.println("File access cancelled by user.");
        }

        if (choosed == true){
            progressLabel.setText("Status : Processing");

            top.removeAllChildren();
            Enumeration en;
            DefaultMutableTreeNode parent;
            DefaultMutableTreeNode tmp;

            progressBar.setValue(5);
            progressBar.setStringPainted(true);

            BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new FileReader(xmiFileName.getText()));

                } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            progressBar.setValue(20);
            progressBar.setStringPainted(true);

            String rawCSV = xmiFileName.getText();
            String [] rowInfo = null;
            CSVReader csvReader;
            try {
                csvReader = new CSVReader (new FileReader(rawCSV));
                try {
                    rowInfo = csvReader.readNext();
                } catch (IOException ex) {
                    Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

            progressBar.setValue(50);
            progressBar.setStringPainted(true);

            try {
                numRowData = 1;
                classArray = new ArrayList<String>();
                classArrayForPack = new ArrayList<String>();
                while ((rowInfo = csvReader.readNext())!= null){
                    numRowData ++;
                    parent = top;
                    int dotInd = rowInfo[0].lastIndexOf(".");
                    String sub = rowInfo[0].substring(dotInd + 1);
                    classArrayForPack.add(rowInfo[0]);
                    classArray.add(sub);
                    
                    String[] list = rowInfo[0].split("\\.");

                    en = top.preorderEnumeration();
                    int x = 0;
                    while(en.hasMoreElements()) {
                            tmp = (DefaultMutableTreeNode) en.nextElement();
                        if (x < (list.length)){
                            if(tmp.toString().equals(list[x]) && tmp.getLevel() == (x + 1)) {
                                parent = tmp;
                                x++;
                                }
                            }
                        }
                        for(; x < list.length; ++x) {
                            tmp = new DefaultMutableTreeNode(list[x]);
                            parent.add(tmp);
                            parent = tmp;
                        }
                }
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
                }

        progressBar.setValue(70);
        progressBar.setStringPainted(true);

        //refreshing tree view
        metricsSelection = new javax.swing.JTree(top);
        checkTreeManager = new CheckTreeManager(metricsSelection);
        jScrollPane1.setViewportView(metricsSelection);

        String line = null;
        String[] dataLines = new String[numRowData];
        int i = 0;
        try {
            while ((line = reader.readLine()) != null) {
                dataLines[i++] = line;
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        progressBar.setValue(90);
        progressBar.setStringPainted(true);

        rawData = new ArrayList<String>();
        for(i = 1; i < dataLines.length; ++i) {
            rawData.add(dataLines[i]);
            }
        
        inDesignChecker(xmiFileName.getText());
        progressBar.setValue(0);
        progressLabel.setText("Status : Done");
        csvFileInput = true;
        }                                
    }//GEN-LAST:event_loadCSVActionPerformed

    private void packageGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_packageGenActionPerformed
        JFrame f = new JFrame("SAAbs Viewer");
        String xmiName = xmiFileName.getText();
        int UML_XMI_Sel = 0;

        if (UML1X_XMI1_0.isSelected()){
            UML_XMI_Sel = 1;
        }
        if (UML1X_XMI1_1_3.isSelected()){
            UML_XMI_Sel = 2;
        }
        if (UML2X_XMI2_0_2_1.isSelected()){
            UML_XMI_Sel = 3;
        }

        System.out.println("Filename : " + xmiName + " " + UML_XMI_Sel);

    if (xmiName.isEmpty() != true){
        try {
        PaintUML P = new PaintUML();
            try {
                P.convertToPlantUMLPackage(xmiName, UML_XMI_Sel, classArrayForPack);
                DisplaySVG app = new DisplaySVG(f, P.getPlantUMLScript(), P.getClassList(),
                        P.getPlantUMLOps(), P.getPlantUMLAttr(), P.getPlantUMLCls(), P.getPlantGSUML());
                // Add components to the frame.
                f.getContentPane().add(app.createComponents());
                f.setSize(400, 400);
                f.setVisible(true);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    else
       JOptionPane.showMessageDialog(null,"Please choose an XMI File");
    }//GEN-LAST:event_packageGenActionPerformed

    private void cutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cutMenuItemActionPerformed

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void halfhalfMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_halfhalfMethodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_halfhalfMethodActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReverseDesignerApp().setVisible(true);
                
                // Andam Added to show to start layer analysis.
                LayerDetector_Main layerdetector = LayerDetector_Main.getInstance();
                    layerdetector.start();

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane CouplingTab;
    private javax.swing.JButton GenDiagram;
    private javax.swing.JRadioButtonMenuItem UML1X_XMI1_0;
    private javax.swing.JRadioButtonMenuItem UML1X_XMI1_1_3;
    private javax.swing.JRadioButtonMenuItem UML2X_XMI2_0_2_1;
    private javax.swing.JMenu XmiUMLVersion;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton checkSuggestion;
    private javax.swing.JScrollPane classRankingTab;
    private javax.swing.JScrollPane classificationTab;
    private javax.swing.JMenu classifierMenu;
    private javax.swing.ButtonGroup classifiers;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JTable couplingPredictorTable;
    private javax.swing.JScrollPane couplingTab;
    private javax.swing.JRadioButtonMenuItem crossVal10Folds;
    private javax.swing.JRadioButtonMenuItem crossValLeave1Out;
    private javax.swing.JFileChooser csvFileChooser;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JCheckBoxMenuItem designMetrics;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JRadioButtonMenuItem halfhalfMethod;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JFileChooser inDesignChooser;
    private javax.swing.JMenu inDesignPatternSourceMenu;
    private javax.swing.ButtonGroup inDesignSource;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JRadioButtonMenuItem knn1MenuItem;
    private javax.swing.JMenuItem loadCSV;
    private javax.swing.JMenuItem loadIndesignPattern;
    private javax.swing.JRadioButtonMenuItem manualSelectionMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTree metricsSelection;
    private javax.swing.JRadioButtonMenuItem naiveBayesMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton packageGen;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JRadioButtonMenuItem patternFileMenuItem;
    private javax.swing.JMenu predictorSet;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JRadioButtonMenuItem randomForestMenuItem;
    private javax.swing.JTable rankingTable;
    private javax.swing.JCheckBoxMenuItem refineOptimize;
    private javax.swing.JTextArea resultOutput;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu settingsMenu;
    private javax.swing.JTable sizePredictorTable;
    private javax.swing.JScrollPane sizeTab;
    private javax.swing.JMenu testOption;
    private javax.swing.ButtonGroup testOptionRButton;
    private javax.swing.JCheckBoxMenuItem textMetrics;
    private javax.swing.JMenuItem textMiningFileChooser;
    private javax.swing.JTable textPredictorTable;
    private javax.swing.JScrollPane textTab;
    private javax.swing.JRadioButtonMenuItem trainingSet;
    private javax.swing.JButton wekaProcess;
    private javax.swing.JTextField xmiFileName;
    private javax.swing.ButtonGroup xmiOption;
    // End of variables declaration//GEN-END:variables
    private CheckTreeManager checkTreeManager;
    private DefaultMutableTreeNode top;
    private int numRowData;
    private String inDesignPatternFileName;
    private String matrixNames;
    private List<String> rawData;
    private List<String> finalData;
    private List<String> dataforYN;
    private List<String> dataforNY;
    private List<String> dataforNN;
    private ArrayList<String> classArray;
    private ArrayList<String> classArrayForPack;
    private String[] predictResultData;
    private String[] errByPredict;
    private boolean csvFileInput;
}
