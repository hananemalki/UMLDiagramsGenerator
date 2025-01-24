package org.mql.java.reflection.test;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.CustomPackage;
import org.mql.java.reflection.models.FieldInfo;
import org.mql.java.reflection.models.MethodInfo;
import org.mql.java.reflection.models.PackageExplorer;
import org.mql.java.reflection.models.RelationInfo;
import org.mql.java.reflection.models.TypeInfo;
import org.mql.java.reflection.xml.XMLExporter;
import org.mql.java.reflection.xml.XMLParser;

public class ParserExample {
    public ParserExample(){
        exp01();
    }

    void exp01() {
    try {
        String workspacePath = "D:\\Master MQL M1\\data"; 
        String projectName = "p03-reflection-and-annotations"; 
        PackageExplorer explorer = new PackageExplorer(workspacePath);
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);
        String outputPath = "Malki Hanane-TP4/resources/xml/project_structure1.xml"; 
        File directory = new File("Malki Hanane-TP4/resources/xml");
        if (!directory.exists()) {
            directory.mkdirs();  
        }

        XMLExporter exporter = new XMLExporter();
        exporter.exportToXML(packagesAndTypes, outputPath);
        System.out.println("Le fichier XML a été généré avec succès : " + outputPath);
        List<CustomPackage> packages = XMLParser.parse(outputPath);
        for (CustomPackage customPackage : packages) {
            System.out.println("Package: " + customPackage.getName());
            for (ClassInfo classInfo : customPackage.getClasses()) {
                System.out.println("  Class: " + classInfo.getName());
                for (FieldInfo fieldInfo : classInfo.getFields()) {
                    System.out.println("    Field: " + fieldInfo.getName() + " (" + fieldInfo.getType() + ")");
                }
                for (MethodInfo methodInfo : classInfo.getMethods()) {
                    System.out.println("    Method: " + methodInfo.getName() + " -> " + methodInfo.getReturnType());
                }
                for (RelationInfo relationInfo : classInfo.getRelations()) {
                    System.out.println("    Relation: " + relationInfo.getType() + " -> " + relationInfo.getTarget());
                }
            }
        }

    } catch (Exception e) {
        System.out.println("Erreur détaillée :");
        e.printStackTrace();
    }
}
    public static void main(String[] args) {
        new ParserExample();
    }
}
