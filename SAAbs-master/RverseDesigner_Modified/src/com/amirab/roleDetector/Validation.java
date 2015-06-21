/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template CBFile, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.roleDetector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * Displays a bar with a single series whose bars are different colors depending
 * upon the bar value. A custom legend is created and displayed for the bar
 * data. Bars in the chart are customized to include a text label of the bar's
 * data value above the bar.
 */
public class Validation extends Application {


    ArrayList<Class> projectClasses;

    @Override
    public void start(Stage stage) {
        projectClasses = RoleDetectorMain.getTaggedProjectClass();
        String directoryOfValidation = "roledetectorfiles\\validation\\Mappish\\";
        stage.setTitle("Bar Chart Sample");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelRotation(90);
        NumberAxis yAxis = new NumberAxis("Y-Axis", 0d, 100d, 5d);
        yAxis.setMinorTickLength(5);

        yAxis.setLabel("Percentage of Role Stereotype performed by Class");

        for (Class c : projectClasses) {
            if ((!c.getPackageImportStereotypes().isEmpty())
                    && (!c.getSemanticStereotypes().isEmpty())
                    && (!c.getCombinedStereotypes().isEmpty())) {

                BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);

                // PACKAGE IMPORT STEREOTYPE
                bc.setTitle(c.getName() + "(Package Import Stereotype)");
                bc.setCategoryGap(10);
                int pcc = 0;
                for (Stereotype s : c.getPackageImportStereotypes()) {
                    if (pcc < 9) {
                        XYChart.Series sr = new XYChart.Series();
                        sr.getData().add(new XYChart.Data(s.getName(), s.getCount()));
                        bc.getData().add(sr);
                        pcc++;
                    }
                }

                Scene PIScene = new Scene(bc, 800, 600);
                stage.setScene(PIScene);
                stage.show();
                stage.close();
                bc.setAnimated(false);
                WritableImage PIImage = bc.snapshot(new SnapshotParameters(), null);
                File PIFile = new File(directoryOfValidation + c.getName() + "(Package Import Stereotype).png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(PIImage, null), "png", PIFile);
                } catch (IOException e) {
                    // TODO: handle exception here
                }

                bc = new BarChart<String, Number>(xAxis, yAxis);
                // SEMANTIC STEREOTYPE
                bc.setTitle(c.getName() + "(Semantic Stereotype)");
                bc.setCategoryGap(10);
                pcc = 0;
                for (Stereotype s : c.getSemanticStereotypes()) {
                    if (pcc < 9) {
                        XYChart.Series sr = new XYChart.Series();
                        sr.getData().add(new XYChart.Data(s.getName(), s.getCount()));
                        bc.getData().add(sr);
                        pcc++;
                    }
                }
                Scene SMScene = new Scene(bc, 800, 600);
                stage.setScene(SMScene);
                stage.show();
                stage.close();
                bc.setAnimated(false);
                WritableImage SMImage = bc.snapshot(new SnapshotParameters(), null);
                File SMFile = new File(directoryOfValidation + c.getName() + "(Semantic Stereotype).png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(SMImage, null), "png", SMFile);
                } catch (IOException e) {
                    // TODO: handle exception here
                }

                // SEMANTIC STEREOTYPE
                bc = new BarChart<String, Number>(xAxis, yAxis);
                bc.setCategoryGap(10);
                bc.setTitle(c.getName() + "(Combined Stereotype)");
                pcc = 0;
                for (Stereotype s : c.getCombinedStereotypes()) {
                    if (pcc < 9) {
                        XYChart.Series sr = new XYChart.Series();
                        sr.getData().add(new XYChart.Data(s.getName(), s.getCount()));
                        bc.getData().add(sr);
                        pcc++;
                    }
                }
                Scene CBScene = new Scene(bc, 800, 600);
                stage.setScene(CBScene);
                stage.show();
                stage.close();
                bc.setAnimated(false);
                WritableImage CBImage = bc.snapshot(new SnapshotParameters(), null);
                File CBFile = new File(directoryOfValidation + c.getName() + "(Combined Stereotype).png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(CBImage, null), "png", CBFile);
                } catch (IOException e) {
                    // TODO: handle exception here
                }

            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
