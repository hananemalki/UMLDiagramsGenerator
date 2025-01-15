package org.mql.java.reflection;

import java.util.List;
import java.util.Map;
import org.mql.java.reflection.models.*;

public class Examples {
    public Examples() {
        exp01();
    }

    void exp01() {
        System.out.println("------------------");

        // Chemin du workspace
        String workspacePath = "D:\\Master MQL M1\\data"; // Remplacez par le chemin de votre workspace
        String projectName = "p03-reflection-and-annotations"; // Remplacez par le nom de votre projet

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

    public static void main(String[] args) {
        new Examples();
    }
}