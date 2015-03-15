/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import java.awt.Color;
import java.awt.color.*;
/**
 *
 * @author aislimau
 */
public class ColorAdjuster {


public String colorCalculator (String Hexacolor, int minus){
    int red = 0;
    int red2 = 0;
    int green = 0;
    int blue = 0;
    String RedStr = "";
    String GreenStr = "";
    String BlueStr = "";
    String newRGB = ""; 
    
    //Color adColor = Color.decode(Hexacolor);
    Color adColor = Color.decode(Hexacolor);
    
    red = adColor.getRed() - 3;
    green = adColor.getGreen() - 3;
    blue = adColor.getBlue() - 3;
    //adColor = adColor.darker();
    //System.out.println("coor : " + adColor.getRGB());
    //System.out.println("darker : " + adColor.darker());
    
    //RedStr = Integer.toHexString(red);
    //red2 = Integer.valueOf(Integer.toString(red),16);
    //GreenStr = Integer.toHexString(green);
    //BlueStr = Integer.toHexString(blue);
    
    adColor.getRed();
    
    //RedStr = Integer.toHexString(adColor.darker().getRed());
    RedStr = String.format("%02X", red & 0xFF );
    GreenStr = String.format("%02X", green & 0xFF );
    BlueStr = String.format("%02X", blue & 0xFF );
    //GreenStr = Integer.toHexString(adColor.darker().getGreen());
    //BlueStr = Integer.toHexString(adColor.darker().getBlue());
    
    //System.out.println("This is red" + RedStr);
    //System.out.println("This is green" + GreenStr);
    //System.out.println("This is blue" + BlueStr);
    //System.out.println("#" + RedStr + GreenStr + BlueStr);
//return "#" + adColor.darker().;
//return "#" + "EB" + "EB" + BlueStr;
    return "#" + RedStr + GreenStr + BlueStr;
}


}


