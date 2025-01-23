package org.mql.java.reflection.xml;

import org.mql.java.reflection.models.*;

public class XMLParser {

    public static CustomPackage parseXML(String xmlFilePath) {
        XMLNode rootNode = new XMLNode(xmlFilePath);  // Charger le fichier XML
        CustomPackage customPackage = new CustomPackage(rootNode.getName());  // Créer un package personnalisé

        // Analyser les classes
        XMLNode[] classNodes = rootNode.children();  // Récupérer toutes les classes
        for (XMLNode classNode : classNodes) {
            ClassInfo classInfo = parseClass(classNode);  // Analyser une classe
            customPackage.addClass(classInfo);  // Ajouter la classe au package
        }

        // Ajouter des interfaces
        XMLNode interfacesNode = rootNode.child("interfaces");
        if (interfacesNode != null) {
            XMLNode[] interfaceNodes = interfacesNode.children();
            for (XMLNode interfaceNode : interfaceNodes) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setName(interfaceNode.getValue());  // Définir le nom de l'interface
                customPackage.addInterface(interfaceInfo);  // Ajouter l'interface au package
            }
        }

        // Ajouter des énumérations
        XMLNode enumsNode = rootNode.child("enums");
        if (enumsNode != null) {
            XMLNode[] enumNodes = enumsNode.children();
            for (XMLNode enumNode : enumNodes) {
                EnumInfo enumInfo = new EnumInfo();
                enumInfo.setName(enumNode.getValue());  // Définir le nom de l'énumération
                customPackage.addEnum(enumInfo);  // Ajouter l'énumération au package
            }
        }

        // Ajouter des annotations
        XMLNode annotationsNode = rootNode.child("annotations");
        if (annotationsNode != null) {
            XMLNode[] annotationNodes = annotationsNode.children();
            for (XMLNode annotationNode : annotationNodes) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setName(annotationNode.getValue());  // Définir le nom de l'annotation
                customPackage.addAnnotation(annotationInfo);  // Ajouter l'annotation au package
            }
        }

        return customPackage;  // Retourner le package complet
    }

    private static ClassInfo parseClass(XMLNode classNode) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setName(classNode.getValue());  // Définir le nom de la classe

        // Analyser les superclasses
        XMLNode superClassNode = classNode.child("superClass");
        if (superClassNode != null) {
            classInfo.setSuperClass(superClassNode.getValue());
        }

        // Analyser les interfaces
        XMLNode interfacesNode = classNode.child("interfaces");
        if (interfacesNode != null) {
            XMLNode[] interfaceNodes = interfacesNode.children();
            for (XMLNode interfaceNode : interfaceNodes) {
                classInfo.addInterface(interfaceNode.getValue());  // Ajouter l'interface
            }
        }

        // Analyser les champs
        XMLNode fieldsNode = classNode.child("fields");
        if (fieldsNode != null) {
            XMLNode[] fieldNodes = fieldsNode.children();
            for (XMLNode fieldNode : fieldNodes) {
                FieldInfo fieldInfo = parseField(fieldNode);  // Parser un champ
                classInfo.addField(fieldInfo);  // Ajouter le champ à la classe
            }
        }

        // Analyser les méthodes
        XMLNode methodsNode = classNode.child("methods");
        if (methodsNode != null) {
            XMLNode[] methodNodes = methodsNode.children();
            for (XMLNode methodNode : methodNodes) {
                MethodInfo methodInfo = parseMethod(methodNode);  // Parser une méthode
                classInfo.addMethod(methodInfo);  // Ajouter la méthode à la classe
            }
        }

        return classInfo;  // Retourner la classe complète
    }

    private static FieldInfo parseField(XMLNode fieldNode) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(fieldNode.child("name").getValue());  // Définir le nom du champ
        fieldInfo.setType(fieldNode.child("type").getValue());  // Définir le type du champ

        // Ajouter les annotations au champ
        XMLNode annotationsNode = fieldNode.child("annotations");
        if (annotationsNode != null) {
            XMLNode[] annotationNodes = annotationsNode.children();
            for (XMLNode annotationNode : annotationNodes) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setName(annotationNode.getValue());  // Définir le nom de l'annotation
                fieldInfo.addAnnotation(annotationInfo);  // Ajouter l'annotation au champ
            }
        }

        return fieldInfo;  // Retourner le champ complet
    }

    private static MethodInfo parseMethod(XMLNode methodNode) {
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setName(methodNode.child("name").getValue());  // Définir le nom de la méthode
        methodInfo.setReturnType(methodNode.child("returnType").getValue());  // Définir le type de retour

        // Ajouter les paramètres
        XMLNode parametersNode = methodNode.child("parameters");
        if (parametersNode != null) {
            XMLNode[] paramNodes = parametersNode.children();
            for (XMLNode paramNode : paramNodes) {
                ParameterInfo parameterInfo = parseParameter(paramNode);  // Parser un paramètre
                methodInfo.addParameter(parameterInfo);  // Ajouter le paramètre à la méthode
            }
        }

        // Ajouter les annotations à la méthode
        XMLNode annotationsNode = methodNode.child("annotations");
        if (annotationsNode != null) {
            XMLNode[] annotationNodes = annotationsNode.children();
            for (XMLNode annotationNode : annotationNodes) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setName(annotationNode.getValue());  // Définir le nom de l'annotation
                methodInfo.addAnnotation(annotationInfo);  // Ajouter l'annotation à la méthode
            }
        }

        return methodInfo;  // Retourner la méthode complète
    }

    private static ParameterInfo parseParameter(XMLNode paramNode) {
        ParameterInfo parameterInfo = new ParameterInfo();
        parameterInfo.setName(paramNode.child("name").getValue());  // Définir le nom du paramètre
        parameterInfo.setType(paramNode.child("type").getValue());  // Définir le type du paramètre

        // Ajouter les annotations au paramètre
        XMLNode annotationsNode = paramNode.child("annotations");
        if (annotationsNode != null) {
            XMLNode[] annotationNodes = annotationsNode.children();
            for (XMLNode annotationNode : annotationNodes) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setName(annotationNode.getValue());  // Définir le nom de l'annotation
                parameterInfo.addAnnotation(annotationInfo);  // Ajouter l'annotation au paramètre
            }
        }

        return parameterInfo;  // Retourner le paramètre complet
    }
}
