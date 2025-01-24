package org.mql.java.reflection.test;
import java.io.File;
import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.reflection.examples.Commande;
import org.mql.java.reflection.examples.Facture;
import org.mql.java.reflection.examples.Produit;
import org.mql.java.reflection.examples.Utilisateur;
import org.mql.java.reflection.models.*;
import org.mql.java.reflection.xml.XMIExporter;
import org.mql.java.reflection.xml.XMLExporter;
import org.mql.java.reflection.xml.XMLGenerator;
import org.mql.java.reflection.xml.XMLParser;

public class Examples {
    public Examples()  {
        try {
            exp06();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void exp03() {
        List<ClassInfo> classes = new Vector<ClassInfo>();
        String packageName = "org.mql.java.reflection.examples"; 

        ClassInfo classInfo = new ClassInfo(Produit.class);
        
        fillClassInfo(classInfo, Produit.class);
        
        classes.add(classInfo);

        System.out.println("Classes: " + classes);
        
        try {
            XMLGenerator.generateXML(classes, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillClassInfo(ClassInfo classInfo, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            classInfo.addField(new FieldInfo(field));
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            classInfo.addMethod(new MethodInfo(method));
        }
    }

   
    void exp01() {
        System.out.println("------------------");

        String workspacePath = "D:\\Master MQL M1\\data"; 
        String projectName = "p03-reflection-and-annotations"; 

        PackageExplorer explorer = new PackageExplorer(workspacePath);
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);

        for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
            System.out.println("Package: " + packageEntry.getKey());

            Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
            for (Map.Entry<String, List<TypeInfo>> typeEntry : typesMap.entrySet()) {
                System.out.println("- " + typeEntry.getKey() + ":");
                for (TypeInfo type : typeEntry.getValue()) {
                    System.out.println("  - " + type.getName());
                }
            }
        }
    }

    void exp02() {
        PackageExplorer explorer = new PackageExplorer("D:\\Master MQL M1\\data");
        Map<String, Map<String, List<TypeInfo>>> result = explorer.getPackagesAndTypes("p03-reflection-and-annotations");
        explorer.printRelations(result);
    }

    void exp04() {
        String workspacePath = "D:\\Master MQL M1\\data"; 
        String projectName = "p03-reflection-and-annotations"; 
    
        PackageExplorer explorer = new PackageExplorer(workspacePath);
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);
    
        for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
            System.out.println("Package: " + packageEntry.getKey());
            Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
            for (Map.Entry<String, List<TypeInfo>> typeEntry : typesMap.entrySet()) {
                System.out.println("- " + typeEntry.getKey() + ":");
                for (TypeInfo type : typeEntry.getValue()) {
                    System.out.println("  - " + type.getName());
                }
            }
        }
    
        String outputPath = "Malki Hanane-TP4/resources/xml/project_structure1.xml"; 
    
        File directory = new File("Malki Hanane-TP4/resources/xml");
        if (!directory.exists()) {
            directory.mkdirs();  
        }
    
        XMLExporter exporter = new XMLExporter();
        try {
            exporter.exportToXML(packagesAndTypes, outputPath);
            System.out.println("Le fichier XML a été généré avec succès : " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération du fichier XML.");
        }
    }

    void exp05() {
    String workspacePath = "D:\\Master MQL M1\\data"; 
    String projectName = "p03-reflection-and-annotations"; 

    PackageExplorer explorer = new PackageExplorer(workspacePath);
    Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);

    for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
        System.out.println("Package: " + packageEntry.getKey());
        Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
        for (Map.Entry<String, List<TypeInfo>> typeEntry : typesMap.entrySet()) {
            System.out.println("- " + typeEntry.getKey() + ":");
            for (TypeInfo type : typeEntry.getValue()) {
                System.out.println("  - " + type.getName());
                if (type instanceof ClassInfo) {
                    ClassInfo classInfo = (ClassInfo) type;
                    System.out.println("    Relations:");
                    for (RelationInfo relation : classInfo.getRelations()) {
                        System.out.println("      " + relation.getType() + " -> " + relation.getTarget());
                    }
                }
            }
        }
    }

    String outputPath = "Malki Hanane-TP4/resources/xmi/project_structure.xmi";
    File directory = new File("Malki Hanane-TP4/resources/xmi");
    if (!directory.exists()) {
        directory.mkdirs();
    }

    XMIExporter xmiExporter = new XMIExporter();
    try {
        xmiExporter.exportToXMI(packagesAndTypes, outputPath);
        System.out.println("Le fichier XMI a été généré avec succès : " + outputPath);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Erreur lors de la génération du fichier XMI.");
    }
    }

    
void exp06() {
    try {
        // Génération du fichier XML
        String workspacePath = "D:\\Master MQL M1\\data"; 
        String projectName = "p03-reflection-and-annotations"; 

        PackageExplorer explorer = new PackageExplorer(workspacePath);
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = explorer.getPackagesAndTypes(projectName);

        String outputPath = "Malki Hanane-TP4/resources/xml/project_structure10.xml"; 

        File directory = new File("Malki Hanane-TP4/resources/xml");
        if (!directory.exists()) {
            directory.mkdirs();  
        }

        XMLExporter exporter = new XMLExporter();
        exporter.exportToXML(packagesAndTypes, outputPath);
        System.out.println("Le fichier XML a été généré avec succès : " + outputPath);

        // Parsing du fichier XML généré
        List<CustomPackage> packages = XMLParser.parse(outputPath);

        // Affichage des packages et classes parsés
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
        new Examples();
    }
}
