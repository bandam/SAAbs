/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import java.io.*;
import  javax.swing.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.sdmetrics.util.XMLParser;
import com.sdmetrics.metrics.*;
import com.sdmetrics.model.*;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File; 
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date; 
import java.util.Random;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import jxl.*; 
import jxl.read.biff.BiffException;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;




/**
 *
 * @author Wei
 */
public class ReverseDesignerView extends javax.swing.JFrame {

    /**
     * Creates new form ReverseDesignerView
     */
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

     public class PredictRanking implements Comparator<String>{
 
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
                + Integer.parseInt(tmp1[21]) + Integer.parseInt(tmp1[22]) + Integer.parseInt(tmp1[22]);
        int i2 = Integer.parseInt(tmp2[15]) + Integer.parseInt(tmp2[16]) + Integer.parseInt(tmp2[20])
                + Integer.parseInt(tmp2[21]) + Integer.parseInt(tmp2[22]) + Integer.parseInt(tmp2[22]);
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}
    public ReverseDesignerView() {
        initComponents();
        //checkTreeInit();
        inDesignPatternFileName = "";
        
         
    }
    
    private void checkTreeInit() {
        
        top =
        new DefaultMutableTreeNode("Classes");
        createNodes(top);
        metricsSelection = new javax.swing.JTree(top);
        checkTreeManager = new CheckTreeManager(metricsSelection);
    }
    
    private void createNodes(DefaultMutableTreeNode top) {
    DefaultMutableTreeNode category = null;
    DefaultMutableTreeNode book = null;
    
    category = new DefaultMutableTreeNode("Books for Java Programmers");
    top.add(category);
    
    //original Tutorial
    book = new DefaultMutableTreeNode(
        "The Java Tutorial: A Short Course on the Basics1");
    category.add(book);
    
     book = new DefaultMutableTreeNode(
        "The Java Tutorial: A Long Course on the Basics2");
    category.add(book);
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
        xmiFileName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        metricsSelection = new javax.swing.JTree();
        wekaProcess = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultOutput = new javax.swing.JTextArea();
        checkSuggestion = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        loadIndesignPattern = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        inDesignPatternSourceMenu = new javax.swing.JMenu();
        manualSelectionMenuItem = new javax.swing.JRadioButtonMenuItem();
        patternFileMenuItem = new javax.swing.JRadioButtonMenuItem();
        classifierMenu = new javax.swing.JMenu();
        randomForestMenuItem = new javax.swing.JRadioButtonMenuItem();
        knn1MenuItem = new javax.swing.JRadioButtonMenuItem();

        fileChooser.setDialogTitle("Open new XMI files");
        fileChooser.setFileFilter(new xmiExtendName());
        fileChooser.setFileHidingEnabled(true);
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        xmiFileName.setText("(Click FIle\\Open to open a XMI file to process)");
        xmiFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xmiFileNameActionPerformed(evt);
            }
        });

        checkTreeInit();
        jScrollPane1.setViewportView(metricsSelection);

        wekaProcess.setText("Analyse");
        wekaProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wekaProcessActionPerformed(evt);
            }
        });

        resultOutput.setColumns(20);
        resultOutput.setRows(5);
        jScrollPane2.setViewportView(resultOutput);

        checkSuggestion.setText("Show Suggestion");
        checkSuggestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSuggestionActionPerformed(evt);
            }
        });

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
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        settingsMenu.setText("Settings");

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

        classifierMenu.setText("Choose Classifier");

        classifiers.add(randomForestMenuItem);
        randomForestMenuItem.setSelected(true);
        randomForestMenuItem.setText("RandomForest");
        classifierMenu.add(randomForestMenuItem);

        classifiers.add(knn1MenuItem);
        knn1MenuItem.setText("KNN (1)");
        knn1MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knn1MenuItemActionPerformed(evt);
            }
        });
        classifierMenu.add(knn1MenuItem);

        settingsMenu.add(classifierMenu);

        menuBar.add(settingsMenu);

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
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xmiFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkSuggestion)
                                .addGap(18, 18, 18)
                                .addComponent(wekaProcess)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xmiFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wekaProcess)
                    .addComponent(checkSuggestion))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        // TODO add your handling code here:
        //Read XMI file               
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();           
             xmiFileName.setText(file.getAbsolutePath());
        } else {
            System.out.println("File access cancelled by user.");
        }
        
        BufferedReader preReader = null;
        try {
            preReader = new BufferedReader(new FileReader(xmiFileName.getText()));
                
        } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] versionStrs = new String[100];
        String preLine = null;
        int i = 0;
        try {
            while ((preLine = preReader.readLine()) != null && i < 100) {
                versionStrs[i++] = preLine;
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
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
        String metaModelURL = "metamodel.xml";//sdmetrics";  // metamodel definition to use
	String xmiTransURL = "xmiTrans1_0.xml";   // XMI tranformations to use
	String xmiFile = xmiFileName.getText(); // XMI file with the UML model
        String metricsURL = "metrics.xml";
        
        XMLParser parser;
        MetaModel metaModel = new MetaModel();       
        XMITransformations trans;
        Model model = null ;
        XMIReader xmiReader;
        MetricStore metricStore = null;
        try {
                parser = new XMLParser();
                parser.parse(metaModelURL, metaModel.getSAXParserHandler()); //Read the metamodel
                
                trans=new XMITransformations(metaModel);
                parser.parse(xmiTransURL, trans.getSAXParserHandler()); //Read the XMI transformation file
                
                model = new Model(metaModel);
                xmiReader = new XMIReader(trans, model);
                parser.parse(xmiFile, xmiReader); //Read the XMI file with the UML model
                
                metricStore = new MetricStore(metaModel);
                parser.parse(metricsURL, metricStore.getSAXParserHandler()); //Read the metric definition file
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            String[] filters = { "#.java", "#.javax", "#.org.xml" };
            model.setFilter(filters, false, true);
            //Build a tree with class names generated from SDMetrics
            top.removeAllChildren();           
            Enumeration en;
            DefaultMutableTreeNode parent;
            DefaultMutableTreeNode tmp;
            
            numRowData = 1;
            for (MetaModelElement type : metaModel) {
            // iterate over all model elements of the current type
                List<ModelElement> elements = model.getAcceptedElements(type);
                    for (ModelElement me : elements) {
                        if (type.getName().matches("class")){
                           numRowData ++;
                           parent = top;                          
                           String[] list =me.getFullName().split("\\.");
                           en = top.preorderEnumeration();
                           i = 0;                          
                           while(en.hasMoreElements() ) {
                             tmp = (DefaultMutableTreeNode) en.nextElement();                             
                             if(tmp.toString().equals(list[i]) && tmp.getLevel() == i + 1) {
                                 parent = tmp;
                                 i++;                                                               
                             }
                           }
                           for(; i < list.length; ++i) {
                               tmp = new DefaultMutableTreeNode(list[i]);
                               parent.add(tmp);
                               parent = tmp;
                           }
                        }
                    }
              }
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
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (InterruptedException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }   

    //refreshing tree view
    metricsSelection = new javax.swing.JTree(top);
   // CheckTreeSelectionModel selectionModel = new CheckTreeSelectionModel(metricsSelection.getModel());
   // selectionModel.addSelectionPath(new TreePath(top.getPath()));
    checkTreeManager = new CheckTreeManager(metricsSelection);
    jScrollPane1.setViewportView(metricsSelection);
    
           BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data_Class.csv"));
                
        } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        String line = null;
        String[] dataLines = new String[numRowData];
        i = 0;
        try {
            while ((line = reader.readLine()) != null) {
                dataLines[i++] = line;
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        rawData = new ArrayList<String>();
        for(i = 1; i < dataLines.length; ++i) {
            rawData.add(dataLines[i]);
        }
   //progressBar.setValue(100);
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void xmiFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmiFileNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xmiFileNameActionPerformed

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileChooserActionPerformed

    private void wekaProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wekaProcessActionPerformed
        // TODO add your handling code here:
        //Get checked tree paths.
        int i = 0;
        int j = 0;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreePath[] paths = selectionModel.getSelectionPaths();
        for(TreePath p : paths) {
            System.out.println(p.toString());
        }
        String[] inDesigns = null;
        if (manualSelectionMenuItem.isSelected()) {
            TreePath checkedPaths[] = selectionModel.getSelectionPaths();
            inDesigns = new String[checkedPaths.length];
            
            for(TreePath tp : checkedPaths) {
            
                Object[] singlePath = tp.getPath();
                String tmp = singlePath[1].toString();
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
               Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
               }
               String line = null;
               List<String> inDesignList =  new ArrayList<String>();
               i = 0;
               try {
                    while ((line = reader.readLine()) != null) {
                        inDesignList.add(line);
                    }
                } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
                }
               inDesigns = (String[]) inDesignList.toArray();
            }
        
        
        
        //Read data from file generated from SDMetrics.
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data_Class.csv"));
                
        } catch (FileNotFoundException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        String line = null;
        String[] dataLines = new String[numRowData];
        i = 0;
        try {
            while ((line = reader.readLine()) != null) {
                dataLines[i++] = line;
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        j = 0;
        String[] sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
        
        //Include all needed attributes here

        dataLines[j] = dataLines[j] + ",InDesign";
        j++;
        boolean flagOfInDesign = false;
        for(; j < dataLines.length; j++) {
            flagOfInDesign = false;
            for(i = 0; i < inDesigns.length; ++i) {
                if(dataLines[j].startsWith(inDesigns[i])) {
                    flagOfInDesign = true;
                }
            }
            if(flagOfInDesign) {
                sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                dataLines[j] = dataLines[j] + ",Y";
                
            }else {
                 sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                dataLines[j] = dataLines[j] + ",N";
            }
        }
       /* for(i = 0; i < inDesigns.length; ++i) {
            while(j < dataLines.length && !dataLines[j].startsWith(inDesigns[i])) {
                sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                dataLines[j] = dataLines[j] + ",N";
                j++;
            }
            while(j < dataLines.length && dataLines[j].startsWith(inDesigns[i])){
                sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
                dataLines[j] = dataLines[j] + ",Y";
                j++;
            }
        }
        for(; j < dataLines.length; ++j) {
            sepLine = dataLines[j].split("\\,");
        dataLines[j] = sepLine[0] + "," + sepLine[1] + ","+ sepLine[2] + ","+ sepLine[3] + ","+ sepLine[4] + ","+ sepLine[5] + ","
                + sepLine[8] + ","+ sepLine[15] + ","+ sepLine[16] + ","+ sepLine[20] + ","+ sepLine[21] + ","+ sepLine[22] + ","+ sepLine[23];
            dataLines[j] = dataLines[j] + ",N";
            
        }*/
        
        for(i = 0; i < dataLines.length; ++i) {
            dataLines[i] = dataLines[i].replaceAll("\\s", "_");
            System.out.println(dataLines[i]);
            
        }
        Path path = Paths.get("data_Class_InDesign.csv");
        Path pathForTrain = Paths.get("data_Training.csv");
        List<String> ls, ls2;
            ls = new ArrayList<String>();
            ls2 = new ArrayList<String>(); 
        for(i = 0; i < dataLines.length; ++i) {
            ls.add(dataLines[i]);
        }
        for(i = 0; i < dataLines.length / 2; ++i) {
            ls2.add(dataLines[i]);
        }
            try {
                Files.write(path, ls, StandardCharsets.UTF_8);
                Files.write(pathForTrain, ls2, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            
/*        DataSource source = null;
        Instances data = null;
        try {
            source = new DataSource("data_Class_InDesign.csv");
            data = source.getDataSet();
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }

 // setting class attribute if the data format does not provide this information
 // For example, the XRFF format saves the class attribute information as well
        if (data.classIndex() == -1)
        data.setClassIndex(data.numAttributes() - 1);*/

            
/*        CSVLoader loader = new CSVLoader();
        Instances structure = null;
        
            try {
                loader.setFile(new File("data_Class_InDesign.csv"));
                System.out.println(loader.getDateAttributes());
                structure = loader.getStructure();
                structure.setClassIndex(structure.numAttributes() - 1);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        */
            

            CSVLoader loader = new CSVLoader();
            Instances data = null;
            Instances trainingData = null;
            Instances filterredData = null;
            Instances filterredTrainingData = null;
            Instances structure = null;
            Instances filterredstructure = null;
            File inputFile = new File("data_Class_InDesign.csv");
            File trainingFile = new File("data_Training.csv.csv");
            try {
                loader.setSource(inputFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                structure = loader.getStructure();
                data = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                loader.setSource(trainingFile);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                trainingData = loader.getDataSet();
                loader.reset();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = "1";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter        
        try {
            remove.setOptions(options);                           // set options
            remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
            remove.setInputFormat(trainingData);
            filterredData = Filter.useFilter(data, remove);
            filterredData.setClassIndex(filterredData.numAttributes() - 1);
            filterredTrainingData = Filter.useFilter(trainingData, remove);
            filterredTrainingData.setClassIndex(filterredTrainingData.numAttributes() - 1);
            filterredstructure = Filter.useFilter(structure, remove);
            filterredstructure.setClassIndex(filterredData.numAttributes() - 1);
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuffer forPredictionsPrinting = new StringBuffer();
        CSV output = new CSV();          
        output.setBuffer(forPredictionsPrinting);
        output.setOutputFile(new File("prediction.csv"));
        Evaluation eval = null;
        resultOutput.setText("");
        if (randomForestMenuItem.isSelected()) {
            
            //configure options for future use;
            try {
                options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 1");
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
           RandomForest rf = new RandomForest();
           

            try {
                rf.buildClassifier(filterredTrainingData);
                eval = new Evaluation(filterredData);
                eval.crossValidateModel(rf, filterredData, 10, new Random(1));
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
                        try {
                            output.setHeader(filterredstructure);
                output.print(rf, filterredData);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            resultOutput.append(rf.globalInfo() + "\n");

        //    List<String> outputMetrics = eval.getMetricsToDisplay();
        //    for(i = 0; i < outputMetrics.size(); ++i) {
        //        resultOutput.append(outputMetrics.get(i)+"\n");
        //    }
            
            //attach predict result to inDesign data;

            
            
          //  resultOutput.append(data.lastInstance().toString());
          //  resultOutput.append(tmpVal + "");
        }else if (knn1MenuItem.isSelected()) {
          //   try {
          //      options = weka.core.Utils.splitOptions("-I 10 -K 0 -S 1");
          //  } catch (Exception ex) {
          //      Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
          //  }
           IBk rf = new IBk();
           

            try {
                rf.buildClassifier(filterredTrainingData);
                eval = new Evaluation(filterredData);
                eval.crossValidateModel(rf, filterredData, 10, new Random(1));
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
                        try {
                            output.setHeader(filterredstructure);
                output.print(rf, filterredData);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            resultOutput.append(rf.globalInfo() + "\n");
        }
        
           resultOutput.append(eval.toSummaryString() + "\n");
            try {
                resultOutput.append(eval.toMatrixString() + "\n");
                resultOutput.append(eval.toClassDetailsString() + "\n");
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        predictResultData = forPredictionsPrinting.toString().split("\n");
            
            errByPredict = new String[dataLines.length];
            finalData = new ArrayList<String>();
            for(i = 0; i < predictResultData.length; i++) {
                String[] tmpSepStr = predictResultData[i].split("\\,");
                if(i != 0)
                    tmpSepStr[2] = tmpSepStr[2].substring(2);
                dataLines[i] = dataLines[i] + "," + tmpSepStr[2] + "," + tmpSepStr[4];
                if(i == 0)
                    matrixNames = dataLines[i];
                else
                    finalData.add(dataLines[i]);
                if(tmpSepStr[3].equals("+"))
                    errByPredict[i] = dataLines[i];
                else
                    errByPredict[i] = "";
            }
            
            Collections.sort(finalData, new PredictRanking());
            resultOutput.append(matrixNames + "\n");
            for (String s : finalData)
                resultOutput.append(s+ "\n");
            
           /*  ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    File out = new File("out.arff");
            try {
                saver.setFile(out);
                saver.writeBatch();
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArffLoader arffLoader = new ArffLoader();
            try {
                arffLoader.setFile(new File("out.arff"));
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
    Instances arffStructure = null;
            try {
                arffStructure = arffLoader.getStructure();
                arffStructure.setClassIndex(structure.numAttributes() - 1);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
    structure.setClassIndex(structure.numAttributes() - 1);

    // train NaiveBayes
    NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
            try {
                nb.buildClassifier(arffStructure);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
    Instance current;
            try {
                while ((current = arffLoader.getNextInstance(structure)) != null)
                  nb.updateClassifier(current);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }

    // output generated model
    System.out.println(nb);*/
            path = Paths.get("data_Pridicted.csv");

            ls = new ArrayList<String>();
        for(i = 0; i < dataLines.length; ++i) {
            ls.add(dataLines[i]);           
        }
            try {
                Files.write(path, ls, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                Logger.getLogger(ReverseDesignerView.class.getName()).log(Level.SEVERE, null, ex);
            }
    
    }//GEN-LAST:event_wekaProcessActionPerformed

    private void knn1MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knn1MenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_knn1MenuItemActionPerformed

    private void loadIndesignPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadIndesignPatternActionPerformed
        // TODO add your handling code here:
        int returnVal = inDesignChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = inDesignChooser.getSelectedFile();           
             inDesignPatternFileName = file.getAbsolutePath();
        } else {
            System.out.println("File access cancelled by user.");
        }
        patternFileMenuItem.setEnabled(true);
    }//GEN-LAST:event_loadIndesignPatternActionPerformed

    private void checkSuggestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkSuggestionActionPerformed
        // TODO add your handling code here:
        String inputValue = JOptionPane.showInputDialog("Please input the suggestion wight (between 1 and 100)");
        int percentage = (Integer.parseInt(inputValue) * (numRowData - 1)) / 100;
        
        //resultOutput.append(matrixNames);
        String[] suggestNames = new String[percentage * 4];
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
        }
        Enumeration en;
        DefaultMutableTreeNode tmp;
        CheckTreeSelectionModel selectionModel;
        selectionModel = checkTreeManager.getSelectionModel();
        TreeNode[] tn;
        TreePath tmpPath;
        
        for(int i = 0; i < 4 * percentage; ++i) {
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
     //   for(TreePath p : paths) {
      //      System.out.println(p.toString());
     //   }
       // selectionModel.addTreeSelectionListener(checkTreeManager);
       // metricsSelection.treeDidChange(); 
        //jScrollPane1.setViewportView(metricsSelection);
        /*CheckTreeSelectionModel selectionModel;
        //metricsSelection = new javax.swing.JTree(top.getFirstChild());
        selectionModel = checkTreeManager.getSelectionModel();
        DefaultMutableTreeNode[] tmpNodes = new DefaultMutableTreeNode[3];
        TreeNode[] tn = top.getNextNode().getNextNode().getPath();
        tmpNodes[0] = new DefaultMutableTreeNode(top);
        tmpNodes[1] = new DefaultMutableTreeNode("Books for Java Programmers");
        tmpNodes[2] = new DefaultMutableTreeNode(
        "The Java Tutorial: A Short Course on the Basics1");
        TreePath[] tmpPaths = new TreePath[3];
               DefaultMutableTreeNode tmp =
        new DefaultMutableTreeNode("Classes");
        createNodes(tmp);
        tmpPaths[0] = new TreePath(tn);
        tmpPaths[1] = new TreePath(tmpNodes[1]);
        tmpPaths[2] = new TreePath(tmpNodes[2]);
        selectionModel.addSelectionPaths(new TreePath[] {tmpPaths[0]});
        
        // metricsSelection.setCellRenderer(new CheckTreeCellRenderer(metricsSelection.getCellRenderer(), selectionModel)); 
        //metricsSelection.addMouseListener(checkTreeManager); 
        selectionModel.addTreeSelectionListener(checkTreeManager); 
        metricsSelection.treeDidChange(); 
       // jScrollPane1.setViewportView(metricsSelection);
            
    //checkTreeManager = new CheckTreeManager(metricsSelection);
    jScrollPane1.setViewportView(metricsSelection);*/
     
    }//GEN-LAST:event_checkSuggestionActionPerformed

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
            java.util.logging.Logger.getLogger(ReverseDesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReverseDesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReverseDesignerView().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton checkSuggestion;
    private javax.swing.JMenu classifierMenu;
    private javax.swing.ButtonGroup classifiers;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JFileChooser inDesignChooser;
    private javax.swing.JMenu inDesignPatternSourceMenu;
    private javax.swing.ButtonGroup inDesignSource;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButtonMenuItem knn1MenuItem;
    private javax.swing.JMenuItem loadIndesignPattern;
    private javax.swing.JRadioButtonMenuItem manualSelectionMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTree metricsSelection;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JRadioButtonMenuItem patternFileMenuItem;
    private javax.swing.JRadioButtonMenuItem randomForestMenuItem;
    private javax.swing.JTextArea resultOutput;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu settingsMenu;
    private javax.swing.JButton wekaProcess;
    private javax.swing.JTextField xmiFileName;
    // End of variables declaration//GEN-END:variables
    private CheckTreeManager checkTreeManager;
    private DefaultMutableTreeNode top;
    private int numRowData;
    private String inDesignPatternFileName;
    private String matrixNames;
    private List<String> rawData;
    private List<String> finalData;
    private String[] predictResultData;
    private String[] errByPredict;
}
