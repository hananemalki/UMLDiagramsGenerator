package org.mql.java.reflection.ui;

import javax.swing.*;

import org.mql.java.reflection.models.CustomPackage;

import java.util.List;

public class UMLDiagramFrame extends JFrame {
    public UMLDiagramFrame(List<CustomPackage> packages) {
        setTitle("UML Diagram");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UMLDiagramPanel diagramPanel = new UMLDiagramPanel(packages);
        JScrollPane scrollPane = new JScrollPane(diagramPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        add(scrollPane);
    }
}