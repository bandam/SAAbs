/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rversedesigner;

import java.awt.*;
import javax.swing.*;
class javaCardLayout extends Frame
{
 Panel p1,p2,p3,p4,p5;
 Label l1,l2,l3,l4,l5;
 CardLayout myCard;
 public javaCardLayout()
 {
  setTitle("javaCardLayout");
  myCard=new CardLayout(20,20);
  setLayout(myCard);
  
  p1=new Panel();
  p2=new Panel();
  p3=new Panel();
  p4=new Panel();
  p5=new Panel();
  
  l1=new Label("This is the first Panel");
  p1.setBackground(Color.yellow);
  p1.add(l1);
  
  l2=new Label("This is the second Panel");
  p2.setBackground(Color.green);
  p2.add(l2);
  
  l3=new Label("This is the third Panel");
  p3.setBackground(Color.magenta);
  p3.add(l3);
  
  l4=new Label("This is the fourth Panel");
  p4.setBackground(Color.white);
  p4.add(l4);
  
  l5=new Label("This is the fifth Panel");
  p5.setBackground(Color.cyan);
  p5.add(l5);
  
  add(p1,"First");
  add(p2,"Second");
  add(p3,"Third");
  add(p4,"Fourth");
  add(p5,"Fifth");
  
  myCard.show(this,"Third");
  setExtendedState( Frame.MAXIMIZED_BOTH );

  setVisible(true);
 }
 public static void main(String args[])
 {
  System.out.println("Staring javaCardLayout...");
  javaCardLayout mainFrame=new javaCardLayout();
  //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }
}
