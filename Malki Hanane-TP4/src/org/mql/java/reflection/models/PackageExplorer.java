package org.mql.java.reflection.models;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                        ClassInfo classInfo = new ClassInfo(clazz);

                        if (classInfo.getSuperClass() != null) {
                            classInfo.addRelation(new RelationInfo(clazz.getName(), classInfo.getSuperClass(), "extends"));
                        }

                        for (String iface : classInfo.getImplementedInterfaces()) {
                            classInfo.addRelation(new RelationInfo(clazz.getName(), iface, "implements"));
                        }

                        for (Field field : clazz.getDeclaredFields()) {
                            Class<?> fieldType = field.getType();
                            if (!fieldType.isPrimitive()) {
                                if (isComposition(field)) {
                                    classInfo.addRelation(new RelationInfo(clazz.getName(), fieldType.getName(), "composition"));
                                } else {
                                    classInfo.addRelation(new RelationInfo(clazz.getName(), fieldType.getName(), "aggregation"));
                                }
                            }
                        }

                        for (Method method : clazz.getDeclaredMethods()) {
                            for (Class<?> paramType : method.getParameterTypes()) {
                                classInfo.addRelation(new RelationInfo(clazz.getName(), paramType.getName(), "uses"));
                            }
                            if (method.getReturnType() != void.class) {
                                classInfo.addRelation(new RelationInfo(clazz.getName(), method.getReturnType().getName(), "returns"));
                            }
                        }

                        classes.add(classInfo);
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

    private boolean isComposition(Field field) {
        return field.getType().getSimpleName().equals("Form"); 
    }

    public void printRelations(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes) {
        for (String packageName : packagesAndTypes.keySet()) {
            System.out.println("Package: " + packageName);
            Map<String, List<TypeInfo>> typesMap = packagesAndTypes.get(packageName);

            for (String typeCategory : typesMap.keySet()) {
                for (TypeInfo typeInfo : typesMap.get(typeCategory)) {
                    if (typeInfo instanceof ClassInfo) {
                        ClassInfo classInfo = (ClassInfo) typeInfo;
                        System.out.println("Class: " + classInfo.getName());
                        for (RelationInfo relation : classInfo.getRelations()) {
                            System.out.println("  " + relation);
                        }
                    }
                }
            }
        }
    }

    public String getWorkspacePath() { return workspacePath; }
    public void setWorkspacePath(String workspacePath) { this.workspacePath = workspacePath; }
}
