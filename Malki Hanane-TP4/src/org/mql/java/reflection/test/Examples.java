package org.mql.java.reflection.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.FieldInfo;
import org.mql.java.reflection.models.MethodInfo;
import org.mql.java.reflection.models.PackageExplorer;
import org.mql.java.reflection.models.RelationInfo;
import org.mql.java.reflection.models.TypeInfo;
import org.mql.java.reflection.xml.XMLExporter;

public class Examples {

    public Examples() {
        affichageConsole();
    }

    public void affichageConsole() {
        String workspacePath = "D:\\Master MQL M1\\data"; 
        String projectName = "p03-reflection-and-annotations";
        String xmlOutputPath = "Malki Hanane-TP4/resources/xml/project_structure.xml";
        try {
            PackageExplorer explorer = new PackageExplorer(workspacePath);
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);
            display(packagesAndTypes);
            exportToXML(packagesAndTypes, xmlOutputPath);

        } catch (Exception e) {
            System.out.println("Erreur lors de la modélisation du projet :");
            e.printStackTrace();
        }
    }
    private void display(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
            sb.append("Package: ").append(packageEntry.getKey()).append("\n");
            Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
            for (Map.Entry<String, List<TypeInfo>> typeEntry : typesMap.entrySet()) {
                String typeCategory = typeEntry.getKey(); 
                sb.append("  Type: ").append(typeCategory).append("\n");
                for (TypeInfo type : typeEntry.getValue()) {
                    if (type instanceof ClassInfo) {
                        ClassInfo classInfo = (ClassInfo) type;
                        sb.append("    Class: ").append(classInfo.getName()).append("\n");
                        if (!classInfo.getFields().isEmpty()) {
                            sb.append("      Fields:\n");
                            for (FieldInfo fieldInfo : classInfo.getFields()) {
                                sb.append("        - ").append(fieldInfo.getName())
                                .append(" (").append(fieldInfo.getType()).append(")\n");
                            }
                        }
                        if (!classInfo.getMethods().isEmpty()) {
                            sb.append("      Methods:\n");
                            for (MethodInfo methodInfo : classInfo.getMethods()) {
                                sb.append("        - ").append(methodInfo.getName())
                                .append("() -> ").append(methodInfo.getReturnType()).append("\n");
                            }
                        }
                        if (!classInfo.getRelations().isEmpty()) {
                            sb.append("      Relations:\n");
                            for (RelationInfo relationInfo : classInfo.getRelations()) {
                                sb.append("        - ").append(relationInfo.getType())
                                .append(" -> ").append(relationInfo.getTarget()).append("\n");
                            }
                        }
                    }
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString()); 
    }

    private void exportToXML(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes, String outputPath) {
        try {
            File directory = new File(outputPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            XMLExporter exporter = new XMLExporter();
            exporter.exportToXML(packagesAndTypes, outputPath);
            System.out.println("Fichier XML généré avec succès : " + outputPath);

        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du fichier XML :");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Examples();
    }
}