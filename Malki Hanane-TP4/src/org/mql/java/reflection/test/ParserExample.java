package org.mql.java.reflection.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.reflection.examples.Commande;
import org.mql.java.reflection.examples.Facture;
import org.mql.java.reflection.examples.Produit;
import org.mql.java.reflection.examples.Utilisateur;
import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.CustomPackage;
import org.mql.java.reflection.models.FieldInfo;
import org.mql.java.reflection.models.MethodInfo;
import org.mql.java.reflection.models.RelationInfo;
import org.mql.java.reflection.models.TypeInfo;
import org.mql.java.reflection.xml.XMIExporter;
import org.mql.java.reflection.xml.XMIParser;

public class ParserExample {
    public ParserExample(){
        exp01();
    }

    void exp01(){
         try {
            // Step 1: Create sample classes and their relations
            ClassInfo utilisateurInfo = new ClassInfo(Utilisateur.class);
            ClassInfo produitInfo = new ClassInfo(Produit.class);
            ClassInfo commandeInfo = new ClassInfo(Commande.class);
            ClassInfo factureInfo = new ClassInfo(Facture.class);

            // Add sample relations
            utilisateurInfo.addRelation(new RelationInfo("ASSOCIATION", "Utilisateur", "Commande"));
            commandeInfo.addRelation(new RelationInfo("ASSOCIATION", "Commande", "Produit"));
            commandeInfo.addRelation(new RelationInfo("ASSOCIATION", "Commande", "Facture"));

            // Prepare data for export
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = new HashMap<>();
            Map<String, List<TypeInfo>> typesInPackage = new HashMap<>();
            List<TypeInfo> classList = new Vector<>(Arrays.asList(
                utilisateurInfo, produitInfo, commandeInfo, factureInfo
            ));
            typesInPackage.put("Classes", classList);
            packagesAndTypes.put("org.mql.java.reflection.examples", typesInPackage);

            // Step 2: Export to XMI
            String xmiFilePath = "user-cmd.xmi";
            XMIExporter exporter = new XMIExporter();
            exporter.exportToXMI(packagesAndTypes, xmiFilePath);

            // Step 3: Parse the generated XMI file
            XMIParser parser = new XMIParser(xmiFilePath);
            
            // Step 4: Print out parsed packages and their contents
            Map<String, CustomPackage> parsedPackages = parser.getPackages();
            System.out.println("Parsed Packages:");
            for (Map.Entry<String, CustomPackage> packageEntry : parsedPackages.entrySet()) {
                System.out.println("Package: " + packageEntry.getKey());
                CustomPackage customPackage = packageEntry.getValue();
                
                System.out.println("  Classes:");
                for (ClassInfo classInfo : customPackage.getClasses()) {
                    System.out.println("    - " + classInfo.getName());
                    
                    System.out.println("      Fields:");
                    for (FieldInfo field : classInfo.getFields()) {
                        System.out.println("        * " + field.getName() + " : " + field.getType());
                    }
                    
                    System.out.println("      Methods:");
                    for (MethodInfo method : classInfo.getMethods()) {
                        System.out.println("        * " + method.getName());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args) {
        new ParserExample();
    }
}
