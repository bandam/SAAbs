/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amirab.layerdetector;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Andam
 */
public class UnknownLibraryItemRederer implements ListCellRenderer<LibraryItem> {

    @Override
    public Component getListCellRendererComponent(JList<? extends LibraryItem> list, LibraryItem value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setLayout(new GridLayout(1, 0));

        panel.add(new JLabel(value.getName()));
        panel.add(new JLabel("View"));
        panel.add(value.getViewCheckBox());
        panel.add(new JLabel("Model"));
        panel.add(value.getModelCheckBox());
        panel.add(new JLabel("Controller"));
        panel.add(value.getControllerCheckBox());

        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
        } else {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }

        return panel;
    }

}
