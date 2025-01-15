package org.mql.java.reflection;

import org.mql.java.reflection.models.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PackageExplorer {
    private String workspacePath;
    private URLClassLoader classLoader;

    public PackageExplorer(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    public Map<String, Map<String, List<TypeInfo>>> getPackagesAndTypes(String projectName) {
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = new HashMap<>();
        
        try {
            File binFolder = new File(workspacePath, projectName + "/bin");
            if (!binFolder.exists() || !binFolder.isDirectory()) {
                System.out.println("Le dossier 'bin' n'existe pas. Compilez d'abord le projet.");
                return packagesAndTypes;
            }
            
            URL url = binFolder.toURI().toURL();
            classLoader = new URLClassLoader(new URL[]{url});
            
            exploreDirectory(binFolder, "", packagesAndTypes);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exploration: " + e.getMessage());
        } finally {
            try {
                if (classLoader != null) classLoader.close();
            } catch (Exception e) {
                System.err.println("Erreur lors de la fermeture du ClassLoader");
            }
        }
        
        return packagesAndTypes;
    }

    private void exploreDirectory(File directory, String currentPackage, 
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes) {
        File[] files = directory.listFiles();
        if (files == null) return;

        List<TypeInfo> classes = new ArrayList<>();
        List<TypeInfo> interfaces = new ArrayList<>();
        List<TypeInfo> enums = new ArrayList<>();
        List<TypeInfo> annotations = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                exploreDirectory(file, currentPackage + file.getName() + ".", packagesAndTypes);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().replace(".class", "");
                String fullClassName = currentPackage + className;

                try {
                    Class<?> clazz = classLoader.loadClass(fullClassName);
                    
                    if (clazz.isAnnotation()) {
                        annotations.add(new AnnotationInfo(clazz));
                    } else if (clazz.isEnum()) {
                        enums.add(new EnumInfo(clazz));
                    } else if (clazz.isInterface()) {
                        interfaces.add(new InterfaceInfo(clazz));
                    } else {
                        classes.add(new ClassInfo(clazz));
                    }
                    
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    System.err.println("Impossible de charger la classe: " + fullClassName);
                }
            }
        }

        if (!classes.isEmpty() || !interfaces.isEmpty() || 
            !enums.isEmpty() || !annotations.isEmpty()) {
            Map<String, List<TypeInfo>> typesMap = new HashMap<>();
            typesMap.put("Classes", classes);
            typesMap.put("Interfaces", interfaces);
            typesMap.put("Enums", enums);
            typesMap.put("Annotations", annotations);

            String packageName = currentPackage.isEmpty() ? "(default)" : 
                currentPackage.substring(0, currentPackage.length() - 1);
            packagesAndTypes.put(packageName, typesMap);
        }
    }
    
    public String getWorkspacePath() { return workspacePath; }
    public void setWorkspacePath(String workspacePath) { this.workspacePath = workspacePath; }
}