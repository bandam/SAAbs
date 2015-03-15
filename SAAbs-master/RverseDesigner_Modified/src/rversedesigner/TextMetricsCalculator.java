package rversedesigner;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hafeez
 */
public class TextMetricsCalculator {
    protected static String csvRefFilename = "";
    protected static String csvInputFilename = "";
    
    public TextMetricsCalculator (){
    }
    
    public TextMetricsCalculator (String cvsReferenceFile, String csvInputFile){
    csvRefFilename = cvsReferenceFile;
    csvInputFilename = csvInputFile;    
    }
    
    public String[][] classNameExtract(String fullClassname){
        String [][] arrayKeyword = new String[40][60]; // previously 12
        int lastDotIndex = fullClassname.lastIndexOf(".");
        String inputFullName = fullClassname;
        
        int keyWordCount = 0;
        int stringLength = inputFullName.length();
        char[] char_arr = inputFullName.toCharArray();
        int count2 = lastDotIndex + 1;
        String words = "";
                
        while (count2 < stringLength){
             // if uppercase and character after is lowercase
            if(Character.isUpperCase(char_arr[count2]) == true && count2 != (stringLength - 1) &&
                Character.isLowerCase(char_arr[count2 + 1]) == true  ) {
                if (count2 != lastDotIndex + 1){
                    arrayKeyword[keyWordCount][0] = words;
                    words = "";      
                    keyWordCount++;                    
                }
                words = words + Character.toString(char_arr[count2]);
            }
                 // if uppercase, not first character and character before lower case, add space
            else if(Character.isUpperCase(char_arr[count2]) == true && Character.isLowerCase(char_arr[count2 - 1]) == true) {
                if (count2 != lastDotIndex + 1){
                    arrayKeyword[keyWordCount][0] = words;
                    words = "";      
                    keyWordCount++;
                }
                words = words + Character.toString(char_arr[count2]);
            }
                  // if uppercase and character before also uppercase
                else if(Character.isUpperCase(char_arr[count2]) == true && Character.isUpperCase(char_arr[count2 - 1]) == true) {
                    words = words + Character.toString(char_arr[count2]);
                 }
                  // if lowercase
                 else if (Character.isLowerCase(char_arr[count2]) == true) {
                    words = words + Character.toString(char_arr[count2]);   
                 }
                 else 
                    words = words + Character.toString(char_arr[count2]);
                 
            count2++;
                    
        }
        arrayKeyword[keyWordCount][0] = words;
        return arrayKeyword;
     }  
 
    public String [] initMetrics (int inputTypeSelect){
        String[] header = null;
        if (inputTypeSelect == 3){
            String[] terms = {
                "className",            // location [0]
                "NoKey",                // location [1]
                "maxInDoc",             // location [2]
                "totalInDoc",           // location [3]
                "maxInAllClasses",      // location [4]
                "maxInSpecClasses",     // location [5]
                "totalInAllClasses",    // location [6]
                "totalInSpecClasses",   // location [7]
                "weightInAllClasses",   // location [8]
                "weightInSpecClasses",  // location [9]      
                "NumAttr",              //location [10]       
                "NumOps",               //location [11]
                "NumPubOps",            //location [12]
                "Setters",              //location [13]
                "Getters",              //location [14]
                "Dep_Out",              //location [15]
                "Dep_In",               //location [16]
                "EC_Attr",              //location [17]
                "IC_Attr",              //location [18]
                "EC_Par",               //location [19]
                "IC_Par",               //location [20]
                "In_Design"             //location [21]
            };
            header = terms;
        }
        
        if (inputTypeSelect == 2){
            String[] terms = {  
                "NumAttr",              //location [0]       
                "NumOps",               //location [1]
                "NumPubOps",            //location [2]
                "Setters",              //location [3]
                "Getters",              //location [4]
                "Dep_Out",              //location [5]
                "Dep_In",               //location [6]
                "EC_Attr",              //location [7]
                "IC_Attr",              //location [8]
                "EC_Par",               //location [9]
                "IC_Par",               //location [10]
                "In_Design"             //location [11]
            };
            header = terms;
        }
        
        if (inputTypeSelect == 1){
            String[] terms = {
                "className",            // location [0]
                "NoKey",                // location [1]
                "maxInDoc",             // location [2]
                "totalInDoc",           // location [3]
                "maxInAllClasses",      // location [4]
                "maxInSpecClasses",     // location [5]
                "totalInAllClasses",    // location [6]
                "totalInSpecClasses",   // location [7]
                "weightInAllClasses",   // location [8]
                "weightInSpecClasses",  // location [9]      
                "In_Design"             //location [10]
            };
            header = terms;
        }
        return header;
    }
    
    public String[] calcMetrics (String clsName, String[][] clsRead, int noKeyword, String projFile, int projLoc){
        String [] designArray = null;
        String[] met = {
            "classname",                // location [0]
            "Number of Keyword",        // location [1]
            "Max in Document",          // location [2]
            "Total in Document",        // location [3]
            "Max in All Classes",       // location [4]
            "Max in Specific Classes",  // location [5]
            "Total in All Classes",     // location [6]
            "Total in Specific Classes",// location [7]
            "Weight in All Classes",    // location [8]
            "Weight in Specific Classes",//location [9]
            "NumAttr",                  //location [10]       
            "NumOps",                   //location [11]
            "NumPubOps",                //location [12]
            "Setters",                  //location [13]
            "Getters",                  //location [14]
            "Dep_Out",                  //location [15]
            "Dep_In",                   //location [16]
            "EC_Attr",                  //location [17]
            "IC_Attr",                  //location [18]
            "EC_Par",                   //location [19]
            "IC_Par",                   //location [20]     
            "InDesign"                  //location [21]
        };
                              
        met[0] = clsName; //insert classname
        met[1] = Integer.toString(noKeyword); // insert no of keyword
        met[2] = inDocMax(clsRead, noKeyword); // insert max inDocument
        met[3] = inDocTotal(clsRead, noKeyword);// insert total inDocument
        met[4] = maxInAllClasses(clsRead, noKeyword);// insert max words allClasses
        met[5] = maxInSpecClasses(clsRead, noKeyword, projLoc);// insert max words specific Classes
        met[6] = totalInAllClasses(clsRead, noKeyword);// insert total words allClasses
        met[7] = totalInSpecClasses(clsRead, noKeyword, projLoc);// insert total words specific Classes
        met[8] = weightInAllClasses(clsRead, noKeyword);// insert weight words allClasses
        met[9] = weightInSpecClasses(clsRead, noKeyword, projLoc);// insert weight words specific Classes   
        
        int count = 10;
        int count2 = 0;
        
        designArray = designMetrics(clsName, projFile);
        
        while (count < met.length){
             met[count] = designArray[count2 + 1];
             count2++;
        count++;
        }                
        return met;
    }
    
    public String[] calcTextMetrics (String clsName, String[][] clsRead, int noKeyword, String projFile, int projLoc){
        String[] designArray = null;
        String[] met = {
            "classname",                // location [0]
            "Number of Keyword",        // location [1]
            "Max in Document",          // location [2]
            "Total in Document",        // location [3]
            "Max in All Classes",       // location [4]
            "Max in Specific Classes",  // location [5]
            "Total in All Classes",     // location [6]
            "Total in Specific Classes",// location [7]
            "Weight in All Classes",    // location [8]
            "Weight in Specific Classes",//location [9]
            "InDesign"                  //location [10]
        };
                              
        met[0] = clsName; //insert classname      
        met[1] = Integer.toString(noKeyword); // insert no of keyword        
        met[2] = inDocMax(clsRead, noKeyword); // insert max inDocument        
        met[3] = inDocTotal(clsRead, noKeyword);// insert total inDocument        
        met[4] = maxInAllClasses(clsRead, noKeyword);// insert max words allClasses
        met[5] = maxInSpecClasses(clsRead, noKeyword, projLoc);// insert max words specific Classes
        met[6] = totalInAllClasses(clsRead, noKeyword);// insert total words allClasses
        met[7] = totalInSpecClasses(clsRead, noKeyword, projLoc);// insert total words specific Classes
        met[8] = weightInAllClasses(clsRead, noKeyword);// insert weight words allClasses
        met[9] = weightInSpecClasses(clsRead, noKeyword, projLoc);// insert weight words specific Classes
        
        designArray = designMetrics(clsName, projFile);
        met[10] = designArray[12]; // insert InDesign information

        return met;
    }

    public String[] calcDesignMetrics (String clsName, String projFile){
        String[] designArray = null;
        String[] met = {
            "NumAttr",                  //location [0]       
            "NumOps",                   //location [1]
            "NumPubOps",                //location [2]
            "Setters",                  //location [3]
            "Getters",                  //location [4]
            "Dep_Out",                  //location [5]
            "Dep_In",                   //location [6]
            "EC_Attr",                  //location [7]
            "IC_Attr",                  //location [8]
            "EC_Par",                   //location [9]
            "IC_Par",                   //location [10]     
            "InDesign"                  //location [11]
        };
                              
        int count = 0;
               
        designArray = designMetrics(clsName, projFile);
        
        while (count < met.length){
             met[count] = designArray[count];
             count++;
        } 
        
        return met;
    }
    
    private String inDocMax (String[][] clsRead, int keyword){
        String result = "0";
        int count = 0;
                
        while (count < keyword){
            int intRes = Integer.parseInt(result);
            int intClsInDoc = 0;
            
            if (clsRead[count][1] != null){
                intClsInDoc = Integer.parseInt(clsRead[count][1]);
            }
            
            if (intClsInDoc > intRes ){
                result = Integer.toString(intClsInDoc);                
            }  
            count++;
        }        
        return result;     
    }
    
    private String inDocTotal (String[][] clsRead, int keyword) {
        int count = 0;
        int intClsInDoc = 0;
                
        while (count < keyword){                
            if (clsRead[count][1] != null){
                intClsInDoc = intClsInDoc + Integer.parseInt(clsRead[count][1]);
            }                                   
            count++;
        } 
        return Integer.toString(intClsInDoc);     
    }
    
    private String maxInAllClasses (String[][] clsRead, int keyword){
        String result = "0";
        int count = 0;
        
        while (count < keyword){
            int intRes = Integer.parseInt(result);
            int intClsInDoc = 0;
            
            if (clsRead[count][2] != null){
                intClsInDoc = Integer.parseInt(clsRead[count][2]);
            }
            
            if (intClsInDoc > intRes ){
                result = Integer.toString(intClsInDoc);                
            }  
            count++;
        }        
        return result;     
    }
    
    private String maxInSpecClasses (String[][] clsRead, int keyword, int projLoc){
        String result = "0";
        int count = 0;
        
        while (count < keyword){
            int intRes = Integer.parseInt(result);
            int intClsInDoc = 0;
            
            if (clsRead[count][3] != null){
                intClsInDoc = Integer.parseInt(clsRead[count][3]);
            }
            
            if (intClsInDoc > intRes ){
                result = Integer.toString(intClsInDoc);                
            }  
            count++;
        }        
        return result;     
    }
    
    private String totalInAllClasses (String[][] clsRead, int keyword) {
        int count = 0;
        int intClsInDoc = 0;
                
        while (count < keyword){                
            if (clsRead[count][2] != null){
                intClsInDoc = intClsInDoc + Integer.parseInt(clsRead[count][2]);
            }                                   
            count++;
        } 
        return Integer.toString(intClsInDoc);     
    }
    
    private String totalInSpecClasses (String[][] clsRead, int keyword, int projLoc) {
        int count = 0;
        int intClsInDoc = 0;
                
        while (count < keyword){                
            if (clsRead[count][3] != null){//
                intClsInDoc = intClsInDoc + Integer.parseInt(clsRead[count][3]);
            }                                   
            count++;
        } 
        return Integer.toString(intClsInDoc);     
    }
    
    private String weightInAllClasses (String[][] clsRead, int keyword) {
        int count = 0;
        Double intClsInDoc = 0.0;
                
        while (count < keyword){                
            if (clsRead[count][2] != null){
                intClsInDoc = intClsInDoc + Integer.parseInt(clsRead[count][2]);
            }                                   
            count++;
        } 
        intClsInDoc = intClsInDoc / keyword;
        
        return Double.toString(intClsInDoc);     
    }
    
    private String weightInSpecClasses (String[][] clsRead, int keyword, int projLoc) {
        int count = 0;
        Double intClsInDoc = 0.0;
                
        while (count < keyword){                
            if (clsRead[count][3] != null){
                intClsInDoc = intClsInDoc + Integer.parseInt(clsRead[count][3]);
            }                                   
            count++;
        } 
        intClsInDoc = intClsInDoc / keyword;
        return Double.toString(intClsInDoc);     
    }
    
    private String [] designMetrics (String className, String projectFile){
        String[] metrics = {
        "className",    //location [0] 
        "NumAttr",      //location [1]       
        "NumOps",       //location [2]
        "NumPubOps",    //location [3]
        "Setters",      //location [4]
        "Getters",      //location [5]
        "Dep_Out",      //location [6]
        "Dep_In",       //location [7]
        "EC_Attr",      //location [8]
        "IC_Attr",      //location [9]
        "EC_Par",       //location [10]
        "IC_Par",       //location [11]
        "InDesign",     //location [12]
        };
        
        CSVReader csvReadProj = null;
        String csvProjName = projectFile;
        String[] rowInput = null;
        
        try {
            csvReadProj = new CSVReader (new FileReader(csvProjName));
            try {
                rowInput = csvReadProj.readNext();
                } catch (IOException ex) {
                    Logger.getLogger(TextMetricsCalculator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TextMetricsCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            int count = 0;
            while ((rowInput = csvReadProj.readNext()) != null) {                         
                if (rowInput[0].equalsIgnoreCase(className)){
                    metrics [0] = rowInput[0];  // Classname
                    metrics [1] = rowInput[1];  // NumAttr
                    metrics [2] = rowInput[2];  // NumOps
                    metrics [3] = rowInput[3];  // NumPubOps
                    metrics [4] = rowInput[4];  // Setters
                    metrics [5] = rowInput[5];  // Getters
                    metrics [6] = rowInput[6];  // Dep_Out
                    metrics [7] = rowInput[7];  // Dep_In
                    metrics [8] = rowInput[8];  // EC_Attr
                    metrics [9] = rowInput[9];  // IC_Attr
                    metrics [10] = rowInput[10];  // EC_Par
                    metrics [11] = rowInput[11]; // IC_Par  
                    metrics [12] = rowInput[12]; // InDesign
                    break;
                }
            count++;
            }
        } catch (IOException ex) {
            Logger.getLogger(TextMetricsCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return metrics;                              
    }    
}
