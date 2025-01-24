package org.mql.java.reflection.xml;

import java.util.List;
import java.util.Vector;
import org.mql.java.reflection.models.*;

public class XMLParser {

    public static List<CustomPackage> parse(String filePath) {
        List<CustomPackage> packages = new Vector<>();
        XMLNode root = new XMLNode(filePath); 
        for (XMLNode packageNode : root.children()) {
            if (packageNode.getName().equals("package")) {
                CustomPackage customPackage = parsePackage(packageNode);
                packages.add(customPackage);
            }
        }

        return packages;
    }

    private static CustomPackage parsePackage(XMLNode packageNode) {
        String packageName = packageNode.attribute("name");
        CustomPackage customPackage = new CustomPackage(packageName);
        for (XMLNode child : packageNode.children()) {
            switch (child.getName()) {
                case "class":
                    customPackage.addClass(parseClass(child));
                    break;
                case "interface":
                    customPackage.addInterface(parseInterface(child));
                    break;
                case "enum":
                    customPackage.addEnum(parseEnum(child));
                    break;
                case "annotation":
                    customPackage.addAnnotation(parseAnnotation(child));
                    break;
            }
        }

        return customPackage;
    }

    private static ClassInfo parseClass(XMLNode classNode) {
        String className = classNode.child("name").getValue();
        ClassInfo classInfo = new ClassInfo();
        classInfo.setName(className);
        XMLNode fieldsNode = classNode.child("fields");
        if (fieldsNode != null) {
            for (XMLNode fieldNode : fieldsNode.children()) {
                classInfo.addField(parseField(fieldNode));
            }
        }
        XMLNode methodsNode = classNode.child("methods");
        if (methodsNode != null) {
            for (XMLNode methodNode : methodsNode.children()) {
                classInfo.addMethod(parseMethod(methodNode));
            }
        }
        XMLNode relationsNode = classNode.child("relations");
        if (relationsNode != null) {
            for (XMLNode relationNode : relationsNode.children()) {
                classInfo.addRelation(parseRelation(relationNode));
            }
        }

        return classInfo;
    }

    private static FieldInfo parseField(XMLNode fieldNode) {
        String fieldName = null;
        String fieldType = null;
        String modifier = null;

        XMLNode nameNode = fieldNode.child("name");
        if (nameNode != null) {
            fieldName = nameNode.getValue();
        }

        XMLNode typeNode = fieldNode.child("type");
        if (typeNode != null) {
            fieldType = typeNode.getValue();
        }

        XMLNode modifierNode = fieldNode.child("modifier");
        if (modifierNode != null) {
            modifier = modifierNode.getValue();
        }

        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(fieldName);
        fieldInfo.setType(fieldType);
        fieldInfo.setModifiers(modifier != null && modifier.equals("public") ? 1 : modifier != null && modifier.equals("private") ? 2 : 4); 
        return fieldInfo;
    }

    private static MethodInfo parseMethod(XMLNode methodNode) {
        String methodName = null;
        String returnType = null;
        String modifier = null;

        XMLNode nameNode = methodNode.child("name");
        if (nameNode != null) {
            methodName = nameNode.getValue();
        }

        XMLNode returnTypeNode = methodNode.child("returnType");
        if (returnTypeNode != null) {
            returnType = returnTypeNode.getValue();
        }

        XMLNode modifierNode = methodNode.child("modifier");
        if (modifierNode != null) {
            modifier = modifierNode.getValue();
        }

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setName(methodName);
        methodInfo.setReturnType(returnType);
        methodInfo.setModifiers(modifier != null && modifier.equals("public") ? 1 : modifier != null && modifier.equals("private") ? 2 : 4); 

        XMLNode parametersNode = methodNode.child("parameters");
        if (parametersNode != null) {
            for (XMLNode parameterNode : parametersNode.children()) {
                methodInfo.addParameter(parseParameter(parameterNode));
            }
        }

        return methodInfo;
    }

    private static ParameterInfo parseParameter(XMLNode parameterNode) {
        String paramName = null;
        String paramType = null;
        XMLNode nameNode = parameterNode.child("name");
        if (nameNode != null) {
            paramName = nameNode.getValue();
        }
        XMLNode typeNode = parameterNode.child("type");
        if (typeNode != null) {
            paramType = typeNode.getValue();
        }
        ParameterInfo parameterInfo = new ParameterInfo();
        parameterInfo.setName(paramName);
        parameterInfo.setType(paramType);
        return parameterInfo;
    }

    private static RelationInfo parseRelation(XMLNode relationNode) {
        String type = null;
        String target = null;
        String source = null;
    
        XMLNode typeNode = relationNode.child("type");
        if (typeNode != null) {
            type = typeNode.getValue();
        }
    
        XMLNode targetNode = relationNode.child("target");
        if (targetNode != null) {
            target = targetNode.getValue();
        }
    
        if (target == null && type != null) {
            target = type;
        }
    
        RelationInfo relation = new RelationInfo(null, target, type);
        return relation;
    }

    private static InterfaceInfo parseInterface(XMLNode interfaceNode) {
        String interfaceName = null;
        XMLNode nameNode = interfaceNode.child("name");
        if (nameNode != null) {
            interfaceName = nameNode.getValue();
        }
        return new InterfaceInfo();
    }

    private static EnumInfo parseEnum(XMLNode enumNode) {
        String enumName = null;
        XMLNode nameNode = enumNode.child("name");
        if (nameNode != null) {
            enumName = nameNode.getValue();
        }
        return new EnumInfo(); 
    }

    private static AnnotationInfo parseAnnotation(XMLNode annotationNode) {
        String annotationName = null;
        XMLNode nameNode = annotationNode.child("name");
        if (nameNode != null) {
            annotationName = nameNode.getValue();
        }
        return new AnnotationInfo();
    }
}