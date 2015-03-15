/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hafeez
 */
public class RelationshipReader {
String processFile = "";
protected String csvAssociation = "dataRM_Class_Assoc.csv";
protected String csvGeneralization = "dataRM_Class_Gen.csv";

RelationshipReader (String xmiFileRecv){
    String xmiFile = xmiFileRecv;    
    String command = "java -jar SDMetrics.jar -xmi " +  xmiFile  + " -relmat -f csv data";
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
}

public String readAssociation (String csvFile) throws FileNotFoundException, IOException{
    String [] row = null;
    String [] topRow = null;
    List<String> assocList;
    assocList = new ArrayList<>();
    String newList = null;
    String inListInvert = null;
    //String processCsv = csvFile;
    String plantUMLScript = "";
        
    //String path = getClass().getClassLoader().getResource(".").getPath();
    
    if (fileChecking(csvAssociation) == false){
        JOptionPane.showMessageDialog(null, "Association Not Found");
    } 
    
    CSVReader csvReader;
    csvReader = new CSVReader (new FileReader(csvFile));
        
    topRow = csvReader.readNext();
    
    int countTopRow = 1;
    int countList = 0;
    
    while (countTopRow < topRow.length ){
        int indexOfDot = topRow[countTopRow].lastIndexOf(".");
        topRow[countTopRow] = topRow[countTopRow].substring(indexOfDot + 1);
        countTopRow++;
    }
    
    while ((row = csvReader.readNext()) != null){
        int count = 1;
        int lastDotIndex = row[0].lastIndexOf(".") + 1;
        
        row[0] = row[0].substring(lastDotIndex);
        while (count < row.length){  
            if (Integer.parseInt(row[count]) > 0){
                String inList = null;
                inList = row[0] + " --> " + topRow[count];
                inListInvert =  topRow[count] + " --> " + row[0];

                if (assocList.size() != 0) {
                    int listLoop  = 0;
                    for (listLoop = 0; listLoop < assocList.size(); listLoop++)
                    {
                        if (assocList.get(listLoop).equalsIgnoreCase(inListInvert)){
                            newList = row[0] + " -- " + topRow[count];
                            assocList.set(listLoop, newList);
                            break;
                        } else {
                            newList = null;
                        }
                     } 
                    if (newList == null){
                        assocList.add(inList);    
                    }
                } else {
                       assocList.add(inList);
                   }              
            }

        count++;      
        }
        }
    csvReader.close();
    
    for (int x = 0; x < assocList.size(); x++){
        plantUMLScript += assocList.get(x) + "\n";

    }
    deletefile(csvAssociation);

    return plantUMLScript;
}

public String readAssociationPack (String csvFile) throws FileNotFoundException, IOException{
    String [] row = null;
    String [] topRow = null;
    List<String> assocList;
    assocList = new ArrayList<>();
    String newList = null;
    String inListInvert = null;
    String processCsv = csvFile;
    String plantUMLScript = "";
    
    String path = getClass().getClassLoader().getResource(".").getPath();
    
    if (fileChecking(csvAssociation) == false){
        JOptionPane.showMessageDialog(null, "Association Not Found");
    } 
    
    CSVReader csvReader;
    csvReader = new CSVReader (new FileReader(processCsv));
        
    topRow = csvReader.readNext();
    
    int countTopRow = 1;
    
    while (countTopRow < topRow.length ){
        topRow[countTopRow] = topRow[countTopRow].replaceAll("\\s", "_");
        countTopRow++;
    }
    
    while ((row = csvReader.readNext()) != null){
        int count = 1;
        
        row[0] = row[0].replaceAll("\\s", "_");
        while (count < row.length){  
        if (Integer.parseInt(row[count]) > 0){
                String inList = null;
                inList = row[0] + " --> " + topRow[count];
                inListInvert =  topRow[count] + " --> " + row[0];

                if (assocList.size() != 0) {
                    int listLoop  = 0;
                    for (listLoop = 0; listLoop < assocList.size(); listLoop++)
                    {
                        if (assocList.get(listLoop).equalsIgnoreCase(inListInvert))
                        {
                            newList = row[0] + " -- " + topRow[count];
                            assocList.set(listLoop, newList);
                            break;
                        } else {
                            newList = null;
                        }
                     } 
                    if (newList == null){
                        assocList.add(inList);    
                    }
                } else {
                       assocList.add(inList);
                   }              
            }
        count++;
        }
    }
    csvReader.close();
    deletefile(csvAssociation);
    
    for (int x = 0; x < assocList.size(); x++){
        plantUMLScript += assocList.get(x) + "\n";

    }
    
    return plantUMLScript;
}

public String getRelationshipMatrices (){
    String relationshipUML = "";
    
    try {
         relationshipUML += readAssociation(csvAssociation); 
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    try {
        relationshipUML += readGeneralization(csvGeneralization);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return relationshipUML;
    }

public String getRelationshipMatricesPack (){
    String relationshipUML = "";
    
    try {
         relationshipUML += readAssociationPack(csvAssociation); 
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    try {
        relationshipUML += readGeneralizationPack(csvGeneralization);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return relationshipUML;
    }

public String readGeneralization (String csvFileGen) throws FileNotFoundException, IOException{
    String [] row = null;
    String [] topRow = null;
    String processCsv = csvFileGen;
    String plantUMLScript = "";
    
    String path = getClass().getClassLoader().getResource(".").getPath();
    
    if (fileChecking(csvGeneralization) == false){
        JOptionPane.showMessageDialog(null, "Generalization Not Found");
    } 
        
    CSVReader csvReader;
    csvReader = new CSVReader (new FileReader(processCsv));
    
    topRow = csvReader.readNext();
    
    int countTopRow = 1;
    
    while (countTopRow < topRow.length ){
        int indexOfDot = topRow[countTopRow].lastIndexOf(".");
        topRow[countTopRow] = topRow[countTopRow].substring(indexOfDot + 1);
        countTopRow++;
    }
    
    while ((row = csvReader.readNext()) != null){
        int count = 1;
        int lastDotIndex = row[0].lastIndexOf(".") + 1;
        
        row[0] = row[0].substring(lastDotIndex);
        while (count < row.length){  
            if (Integer.parseInt(row[count]) > 0){
                plantUMLScript += row[0] + " --|> " + topRow[count] + "\n";
            }
        count++;
        }
    }
    csvReader.close();
    deletefile(csvGeneralization);
    return plantUMLScript;
 }

public String readGeneralizationPack (String csvFileGen) throws FileNotFoundException, IOException{
    String [] row = null;
    String [] topRow = null;
    String processCsv = csvFileGen;
    String plantUMLScript = "";
    
    String path = getClass().getClassLoader().getResource(".").getPath();
    
    if (fileChecking(csvGeneralization) == false){
        JOptionPane.showMessageDialog(null, "Generalization Not Found");
    } 
        
    CSVReader csvReader;
    csvReader = new CSVReader (new FileReader(processCsv));
    
    topRow = csvReader.readNext();
    
    int countTopRow = 1;
    
    while (countTopRow < topRow.length ){
        //int indexOfDot = topRow[countTopRow].lastIndexOf(".");
        topRow[countTopRow] = topRow[countTopRow].replaceAll("\\s", "_");
                //topRow[countTopRow].substring(indexOfDot + 1);
        countTopRow++;
    }
    
    while ((row = csvReader.readNext()) != null){
        int count = 1;
        //int lastDotIndex = row[0].lastIndexOf(".") + 1;
        
        //row[0] = row[0].substring(lastDotIndex);
        row[0] = row[0].replaceAll("\\s", "_");
        while (count < row.length){  
            if (Integer.parseInt(row[count]) > 0){
                
                plantUMLScript += row[0] + " --|> " + topRow[count] + "\n";
            }
        count++;
        }
    }
    csvReader.close();
    deletefile(csvGeneralization);
    return plantUMLScript;
 }

private static void deletefile(String file){
  File f1 = new File(file);
  boolean success = f1.delete();
  if (!success){
    System.out.println("Deletion failed.");
    System.exit(0);
  } else{
    System.out.println("File deleted.");
    }
  }

private boolean fileChecking (String fileName) {
    
    File f = new File(fileName);
      if(f.exists()){
    	  return true;
      }else{
	  return false;
      }
    }

}

