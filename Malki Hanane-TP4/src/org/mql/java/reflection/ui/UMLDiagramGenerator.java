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
            // Étape 1 : Explorer le projet et extraire les packages et types
            PackageExplorer explorer = new PackageExplorer(workspacePath);
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);

            // Étape 2 : Exporter les données en XML
            File directory = new File(outputPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            XMLExporter exporter = new XMLExporter();
            exporter.exportToXML(packagesAndTypes, outputPath);
            System.out.println("Fichier XML généré avec succès : " + outputPath);

            // Étape 3 : Parser le fichier XML
            List<CustomPackage> packages = XMLParser.parse(outputPath);

            // Étape 4 : Générer et afficher le diagramme UML
            SwingUtilities.invokeLater(() -> {
                UMLDiagramFrame diagramFrame = new UMLDiagramFrame(packages);
                diagramFrame.setVisible(true);
            });

        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du diagramme UML :");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String workspacePath = "D:\\Master MQL M1\\data";
        String projectName = "p03-reflection-and-annotations";
        String outputPath = "Malki Hanane-TP4/resources/xml/project_structure10.xml";

        UMLDiagramGenerator generator = new UMLDiagramGenerator(workspacePath, projectName, outputPath);
        generator.generateUMLDiagram();
    }
}