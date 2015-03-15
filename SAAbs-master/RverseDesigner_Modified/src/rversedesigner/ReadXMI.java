/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.util.XMLParser;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hafeez
 */
public class ReadXMI {
    List<String> classList = new ArrayList<>();
    List<String> classListPack = new ArrayList<>();
    protected String xmiFile = "";
    protected int xmiConfig = 0;
    protected static String plantUMLClass;
    protected static String plantUMLOperation;
    protected static String plantUMLAttribute;
    protected static String plantUMLGetSetOut;
    
public ReadXMI (String fileName, int XMIConf, ArrayList<String> classgen){
   xmiFile = fileName;
   xmiConfig = XMIConf;
   classList = classgen;
}

public String readClass (){
    String metaModelURL = ""; // metamodel definition to use
    String xmiTransURL = "";   // XMI tranformations to use
    String plantUMLXMI = "";
    plantUMLClass = "";
    plantUMLAttribute = "";
    plantUMLOperation = "";
    plantUMLGetSetOut = "";
    
    if (xmiConfig == 1){
        metaModelURL = "metamodel.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans1_0.xml";   // XMI tranformations to use
    }
    if (xmiConfig == 2){
        metaModelURL = "metamodel.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans1_1.xml";   // XMI tranformations to use
    }
    if (xmiConfig == 3){
        metaModelURL = "metamodel2.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans2_0.xml";   // XMI tranformations to use
    }

    XMLParser parser = null;
        try {
            parser = new XMLParser();
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
	MetaModel metaModel = new MetaModel();
        try {
            parser.parse(metaModelURL, metaModel.getSAXParserHandler());
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	XMITransformations trans=new XMITransformations(metaModel);
        try {
            parser.parse(xmiTransURL, trans.getSAXParserHandler());
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	Model model = new Model(metaModel);
	XMIReader xmiReader = new XMIReader(trans, model);
        try {
            parser.parse(xmiFile, xmiReader);
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	String[] filters = { "#.java", "#.javax", "#.org.xml" };
	model.setFilter(filters, false, true);
        
	int cntClsList = 0;
        String initColor = "#FFFFFF";
        String currColor = initColor;
        int darker = 1;
        
        for (cntClsList = 0; cntClsList < classList.size(); cntClsList++){
            //String source = "@startuml\n";
            ColorAdjuster changeColor = new ColorAdjuster();
            currColor = changeColor.colorCalculator(currColor,darker);
            plantUMLXMI += "class " + classList.get(cntClsList) + " \n";
            plantUMLClass += "class " + classList.get(cntClsList) + " \n";
            plantUMLGetSetOut += "class " + classList.get(cntClsList) + " \n";
        }
              
        //iterate over all model element types in the metamodel
	for (MetaModelElement type : metaModel) {
       	// iterate over all model elements of the current type
            List<ModelElement> elements = model.getAcceptedElements(type);
            for (ModelElement me : elements) {
                if (type.getName().matches("operation")){                    
                    for (int i = 0; i < classList.size(); i++){
                        if (classList.get(i).equalsIgnoreCase(me.getOwner().getName())){
                            plantUMLXMI += "class " +  me.getOwner().getName() + "{\n";
                            plantUMLXMI += me.getName() + "()\n";
                            plantUMLXMI += "}\n";
                            plantUMLOperation += "class " +  me.getOwner().getName() + "{\n";
                            plantUMLOperation += me.getName() + "()\n";
                            plantUMLOperation += "}\n";
                            if (me.getName().startsWith("get") || me.getName().endsWith("get") 
                                || me.getName().startsWith("set") || me.getName().endsWith("set") || me.getName().startsWith("to") 
                                    || me.getName().equalsIgnoreCase(me.getOwner().getName())) {
                                System.out.println("This is plantUMLGS : " + plantUMLGetSetOut);                                
                            } else {
                                plantUMLGetSetOut += "class " +  me.getOwner().getName() + "{\n";
                                plantUMLGetSetOut += me.getName() + "()\n";
                                plantUMLGetSetOut += "}\n";
                                System.out.println(me.getOwner().getName() + " : " + me.getName() + " : getters/setters found");
                            }
                        }
                    }                   
                }
            
                if (type.getName().matches("attribute")){
                   for (int i = 0; i < classList.size(); i++){                       
                        if (classList.get(i).equalsIgnoreCase(me.getOwner().getName())){
                            plantUMLXMI += "class " +  me.getOwner().getName() + "{\n";
                            plantUMLXMI += me.getName() + "\n";
                            plantUMLXMI += "}\n";
                            plantUMLAttribute += "class " +  me.getOwner().getName() + "{\n";
                            plantUMLAttribute += me.getName() + "\n";
                            plantUMLAttribute += "}\n"; 
                            plantUMLGetSetOut += "class " +  me.getOwner().getName() + "{\n";
                            plantUMLGetSetOut += me.getName() + "\n";
                            plantUMLGetSetOut += "}\n";                           
                        }
                   }
                }
            }
        }     
       return plantUMLXMI;
    }

public String readClassPack (){
    String metaModelURL = ""; // metamodel definition to use
    String xmiTransURL = "";   // XMI tranformations to use
    String plantUMLXMI = "";
    plantUMLClass = "";
    plantUMLAttribute = "";
    plantUMLOperation = "";
    
    classListPack = new ArrayList<>();
    
    if (xmiConfig == 1){
        metaModelURL = "metamodel.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans1_0.xml";   // XMI tranformations to use
    }
    if (xmiConfig == 2){
        metaModelURL = "metamodel.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans1_1.xml";   // XMI tranformations to use
    }
    if (xmiConfig == 3){
        metaModelURL = "metamodel2.xml"; // metamodel definition to use
        xmiTransURL = "xmiTrans2_0.xml";   // XMI tranformations to use
    }

 //   int loop = 0;
    
//    for(; loop < classList.size(); ++loop) {
//        String modified = classList.get(loop).replaceAll("_", " ");
//        classListPack.add(modified);
//    }
    
    XMLParser parser = null;
        try {
            parser = new XMLParser();
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
	MetaModel metaModel = new MetaModel();
        try {
            parser.parse(metaModelURL, metaModel.getSAXParserHandler());
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	XMITransformations trans=new XMITransformations(metaModel);
        try {
            parser.parse(xmiTransURL, trans.getSAXParserHandler());
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	Model model = new Model(metaModel);
	XMIReader xmiReader = new XMIReader(trans, model);
        try {
            parser.parse(xmiFile, xmiReader);
        } catch (Exception ex) {
            Logger.getLogger(ReverseDesignerApp.class.getName()).log(Level.SEVERE, null, ex);
        }

	String[] filters = { "#.java", "#.javax", "#.org.xml" };
	model.setFilter(filters, false, true);
        
	int cntClsList = 0;
        String initColor = "#FFFFFF";
        String currColor = initColor;
        int darker = 8;
        
        for (cntClsList = 0; cntClsList < classList.size(); cntClsList++){
            //String source = "@startuml\n";
            ColorAdjuster changeColor = new ColorAdjuster();
            currColor = changeColor.colorCalculator(currColor,darker);
            plantUMLXMI += "class " + classList.get(cntClsList) + " \n";
            plantUMLClass += "class " + classList.get(cntClsList) + " \n";
        }
              
        //iterate over all model element types in the metamodel
	for (MetaModelElement type : metaModel) {
       	// iterate over all model elements of the current type
            List<ModelElement> elements = model.getAcceptedElements(type);
            for (ModelElement me : elements) {
                if (type.getName().matches("operation")){                    
                    for (int i = 0; i < classListPack.size(); i++){                       
                        String stringReplace = "";
                        stringReplace = me.getOwner().getFullName().replaceAll("\\s", "_");
                        if (classListPack.get(i).equalsIgnoreCase(stringReplace)){
                            plantUMLXMI += "class " +  stringReplace + "{\n";
                            plantUMLXMI += me.getName() + "()\n";
                            plantUMLXMI += "}\n";
                            plantUMLOperation += "class " +  stringReplace + "{\n";
                            plantUMLOperation += me.getName() + "()\n";
                            plantUMLOperation += "}\n";
                        }
                    }                   
                }
            
                if (type.getName().matches("attribute")){
                    for (int i = 0; i < classListPack.size(); i++){      
                        String stringReplace = "";
                        stringReplace = me.getOwner().getFullName().replaceAll("\\s", "_");
                       if (classListPack.get(i).equalsIgnoreCase(stringReplace)){ 
                            plantUMLXMI += "class " +  stringReplace + "{\n";
                            plantUMLXMI += me.getName() + "\n";
                            plantUMLXMI += "}\n";
                            plantUMLAttribute += "class " +  stringReplace + "{\n";
                            plantUMLAttribute += me.getName() + "\n";
                            plantUMLAttribute += "}\n";                             
                        }
                   }
                }
            }
        }     
       return plantUMLXMI;
    }

public List<String> getClassList(){
    return classList;
}

public String getAttributePlantUML(){
    return plantUMLClass + plantUMLOperation;
}

public String getOperationPlantUML(){
    return plantUMLClass + plantUMLAttribute;
}

public String getClassPlantUML(){
    return plantUMLClass;
}

public String getPlantUMLGetSetOut(){
    return plantUMLGetSetOut;
}
}
