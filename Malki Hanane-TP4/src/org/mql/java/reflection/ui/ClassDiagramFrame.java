package org.mql.java.reflection.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.mql.java.reflection.models.*;

public class ClassDiagramFrame extends JFrame {
    private List<CustomPackage> packages;

    public ClassDiagramFrame(List<CustomPackage> packages) {
        this.packages = packages;
        setTitle("Diagramme UML");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UMLDiagramPanel diagramPanel = new UMLDiagramPanel(packages);
        JScrollPane scrollPane = new JScrollPane(diagramPanel); // Ajouter un JScrollPane pour gérer le défilement
        add(scrollPane); // Ajouter le JScrollPane à la JFrame
    }
}