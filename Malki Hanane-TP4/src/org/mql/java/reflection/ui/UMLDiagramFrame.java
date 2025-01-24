package org.mql.java.reflection.ui;

import javax.swing.*;

import org.mql.java.reflection.models.CustomPackage;

import java.util.List;

public class UMLDiagramFrame extends JFrame {
    public UMLDiagramFrame(List<CustomPackage> packages) {
        setTitle("Diagramme UML");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UMLDiagramPanel diagramPanel = new UMLDiagramPanel(packages);
        JScrollPane scrollPane = new JScrollPane(diagramPanel); // Ajouter un JScrollPane pour gérer le défilement
        add(scrollPane); // Ajouter le JScrollPane à la JFrame
    }
}