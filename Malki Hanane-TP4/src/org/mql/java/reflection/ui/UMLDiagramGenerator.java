package org.mql.java.reflection.ui;

import org.mql.java.reflection.xml.XMLExporter;
import org.mql.java.reflection.xml.XMLParser;
import org.mql.java.reflection.models.*;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class UMLDiagramGenerator {

    private String workspacePath;
    private String projectName;
    private String outputPath;

    public UMLDiagramGenerator(String workspacePath, String projectName, String outputPath) {
        this.workspacePath = workspacePath;
        this.projectName = projectName;
        this.outputPath = outputPath;
    }

    public void generateUMLDiagram() {
        try {
            PackageExplorer explorer = new PackageExplorer(workspacePath);
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);
            File directory = new File(outputPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            XMLExporter exporter = new XMLExporter();
            exporter.exportToXML(packagesAndTypes, outputPath);
            System.out.println("Fichier XML généré avec succès : " + outputPath);
            List<CustomPackage> packages = XMLParser.parse(outputPath);
            SwingUtilities.invokeLater(() -> {
                UMLDiagramFrame diagramFrame = new UMLDiagramFrame(packages);
                diagramFrame.setVisible(true);
            });
    
        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du diagramme UML :");
            e.printStackTrace();
        }
    }
}