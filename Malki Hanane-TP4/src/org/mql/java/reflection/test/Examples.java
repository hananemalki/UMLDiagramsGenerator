package org.mql.java.reflection.test;
import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.reflection.examples.Produit;
import org.mql.java.reflection.models.*;
import org.mql.java.reflection.xml.XMLGenerator;

public class Examples {
    public Examples()  {
        try {
            exp02();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    void exp03() {
        List<ClassInfo> classes = new Vector<ClassInfo>(); // Précise le type générique
        String packageName = "org.mql.java.reflection.examples"; // Assure-toi que packageName est défini

        // Crée un objet ClassInfo pour Produit
        ClassInfo classInfo = new ClassInfo(Produit.class);
        
        // Remplir la classe avec ses informations (champs, méthodes, etc.)
        fillClassInfo(classInfo, Produit.class);
        
        // Ajouter la classe à la liste
        classes.add(classInfo);

        // Affiche les classes pour vérifier
        System.out.println("Classes: " + classes);
        
        try {
            // Générer le XML
            XMLGenerator.generateXML(classes, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillClassInfo(ClassInfo classInfo, Class<?> clazz) {
        // Remplir les champs
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            classInfo.addField(field);
        }

        // Remplir les méthodes
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            classInfo.addMethod(method);
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

    public static void main(String[] args) {
        new Examples();
    }
}
