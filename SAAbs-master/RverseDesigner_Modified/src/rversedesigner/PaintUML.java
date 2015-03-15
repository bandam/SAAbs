package rversedesigner;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.amirab.layerdetector.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 *
 * @author Hafeez
 */
public class PaintUML {

    List<String> classList = new ArrayList<>();
    String plantUMLScript = new String();
    static String plantUMLOps = new String();
    static String plantUMLAttr = new String();
    static String plantUMLCls = new String();
    static String plantUMLGSOut = new String();
    static int callCount;

    /**
     * @param args the command line arguments
     */
    public PaintUML() {
        callCount = 0;
        plantUMLScript = "";
        plantUMLOps = "";
        plantUMLAttr = "";
        plantUMLCls = "";
    }

    public void convertToPlantUML(String xmiFile, int UML_XMI_Sel, ArrayList<String> ClassInfo) throws
            IOException, InterruptedException {

        int XMIConf = UML_XMI_Sel;
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        String xmiInput = xmiFile;
        String tempStr = "";
        classList.clear();

        ReadXMI xmiReader = new ReadXMI(xmiInput, XMIConf, ClassInfo);
        RelationshipReader rReader = new RelationshipReader(xmiInput);

        String source = "@startuml\n";
        source += "top to bottom direction \n";
        //source += "left to right direction \n";
        source += "skinparam class { \n";
        source += "BackgroundColor CornSilk \n";
        source += "BorderColor SeaGreen \n";

        // Andam added Layer Stereotypes
        source += "BackgroundColor<<Model>> B2BF00\n"
                + "BorderColor<<Model>> Tomato\n";
        source += "BackgroundColor<<View>> Wheat\n"
                + "BorderColor<<View>> Tomato\n";
        source += "BackgroundColor<<Controller>> DarkKhaki\n"
                + "BorderColor<<Controller>> Tomato\n";

        source += "classFontSize 32 \n";
        source += "classAttributeFontSize  32 \n";
        source += "} \n";

    //source += "skinparam class ATM classFontSize 32 \n";
        source += xmiReader.readClass();
        tempStr += rReader.getRelationshipMatrices();
        source += tempStr;

        plantUMLScript = source;
        plantUMLOps = xmiReader.getOperationPlantUML() + tempStr;
        plantUMLAttr = xmiReader.getAttributePlantUML() + tempStr;
        plantUMLCls = xmiReader.getClassPlantUML() + tempStr;
        plantUMLGSOut = xmiReader.getPlantUMLGetSetOut() + tempStr;
        source += "@enduml";

        // Andam call to tag function
        source = andam_tagClassLayers(source);

        System.out.println("This is source : " + source);
        classList = xmiReader.getClassList();

        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os;
        os = new ByteArrayOutputStream();
        // Write the first image to "os"
        String desc;
        desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();

        // The XML is stored into svg
        final String svg = new String(os.toByteArray());

        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(/*path + */"image2.svg"))) {
                out.write(svg);
            }
        } catch (IOException e) {
            System.out.println("Exception ");
        }
    }

    public void convertToPlantUMLPackage(String xmiFile, int UML_XMI_Sel, ArrayList<String> ClassInfo) throws
            IOException, InterruptedException {

        int XMIConf = UML_XMI_Sel;
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        String xmiInput = xmiFile;
        String tempStr = "";
        classList.clear();

        ReadXMI xmiReader = new ReadXMI(xmiInput, XMIConf, ClassInfo);
        RelationshipReader rReader = new RelationshipReader(xmiInput);

        String source = "@startuml\n";
        source += "top to bottom direction \n";
        //source += "left to right direction \n";
        source += "skinparam class { \n";
        source += "BackgroundColor CornSilk \n";
        source += "BorderColor SeaGreen \n";

        // Andam added Layer Stereotypes
        source += "BackgroundColor<<Model>> B2BF00 \n"
                + "BorderColor<<Model>> Tomato\n";
        source += "BackgroundColor<<View>> Wheat\n"
                + "BorderColor<<View>> Tomato\n";
        source += "BackgroundColor<<Controller>> DarkKhaki\n"
                + "BorderColor<<Controller>> Tomato\n";

        source += "classFontSize 32 \n";
        source += "classAttributeFontSize  32 \n";
        source += "} \n";

        source += xmiReader.readClassPack();
        tempStr += rReader.getRelationshipMatricesPack();
        source += tempStr;

        plantUMLScript = source;
        plantUMLOps = xmiReader.getOperationPlantUML() + tempStr;
        plantUMLAttr = xmiReader.getAttributePlantUML() + tempStr;
        plantUMLCls = xmiReader.getClassPlantUML() + tempStr;
        source += "@enduml";

        // Andam call to tag function
        source = andam_tagClassLayers(source);

        System.out.println("This is source : " + source);
        classList = xmiReader.getClassList();

        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os;
        os = new ByteArrayOutputStream();
        // Write the first image to "os"
        String desc;
        desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();

        // The XML is stored into svg
        final String svg = new String(os.toByteArray());

        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(/*path + */"image2.svg"))) {
                out.write(svg);
            }
        } catch (IOException e) {
            System.out.println("Exception ");
        }
    }

    public void convertToPlantUMLSlider(String plantUMLScript,
            List<String> allClass, int maxPosInt, int displayType) throws IOException, InterruptedException {
        // TODO code application logic here
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        String path = getClass().getClassLoader().getResource(".").getPath();
        String source = plantUMLScript;

        System.out.println("position " + maxPosInt);
        System.out.println("size " + allClass.size());

        int actualCountList = allClass.size() - 1;
        int actualCutOffPos = allClass.size() - maxPosInt;

        System.out.println("actualCountList " + actualCountList);
        System.out.println("actualCutOffPos " + actualCutOffPos);

        //String initColor = "#6698FF";
        String initColor = "#CCCCCC";
        String currColor = initColor;
        int darker = 6;

        for (int i = actualCutOffPos; i <= actualCountList; i++) {
            if (displayType == 1) {
                // hide deleted classes
                source += "hide " + allClass.get(i) + "\n";
            } else if (displayType == 2) {
                source += "class " + allClass.get(i) + " #EB937F" + " \n";
                //source += "skinparam BorderColor << " + allClass.get(i) + " >> LightGray \n";  
            } else if (displayType == 3) {
                ColorAdjuster changeColor = new ColorAdjuster();
                currColor = changeColor.colorCalculator(currColor, darker);
                source += "class " + allClass.get(i) + " " + currColor + " \n";
            }
            System.out.println("class deleted :  " + allClass.get(i));
        }

        if (displayType == 4) {
            //for (int i = allClass.size() - 1; i >= 0; i--){
            for (int i = 0; i < allClass.size(); i++) {
                ColorAdjuster changeColor = new ColorAdjuster();
                currColor = changeColor.colorCalculator(currColor, darker);
                source += "class " + allClass.get(i) + " " + currColor + " \n";
            }
        }
        source += "@enduml";

        // Andam call to tag function
        source = andam_tagClassLayers(source);

        System.out.println("source : " + source);

        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os;
        os = new ByteArrayOutputStream();
        // Write the first image to "os"
        String desc;
        desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        //System.out.println("This is desc" + desc);
        os.close();

        // The XML is stored into svg
        final String svg = new String(os.toByteArray());

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path + "image3.svg"));
            out.write(svg);
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");
        }
    }

    public String getPlantUMLScript() {
        return plantUMLScript;
    }

    public String getPlantUMLOps() {
        return plantUMLOps;
    }

    public String getPlantUMLAttr() {
        return plantUMLAttr;
    }

    public String getPlantUMLCls() {
        return plantUMLCls;
    }

    public String getPlantGSUML() {
        return plantUMLGSOut;
    }

    public List<String> getClassList() {
        return classList;
    }

    /**
     * Added this extra function to tag the classes
     *
     * @param source plantUMLString before tag
     * @return plantUMLString after tag
     */
    public String andam_tagClassLayers(String source) {
        LayerDetector_Main layerdetector = LayerDetector_Main.getInstance();
        if (callCount == 0) {
            callCount = callCount++;
            ///layerdetector.showUnKnownLibraries();
            return layerdetector.tagPlantUMLString(source);
            
        } else {
            layerdetector.setClasses(new ArrayList<com.amirab.layerdetector.Class>());
            layerdetector.getFrame().setVisible(true);
            layerdetector.start();
            return layerdetector.tagPlantUMLString(source);
        }

    }
}
