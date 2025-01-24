package org.mql.java.reflection.xml;

import org.mql.java.reflection.models.*;
import java.util.*;

public class XMIParser {
    private XMLNode root; 
    private Map<String, CustomPackage> packages; 

    public XMIParser(String xmiFilePath) {
        this.root = new XMLNode(xmiFilePath);
        this.packages = new HashMap<>();
        parse(); 
    }

    private void parse() {
        XMLNode modelNode = findModelNode(root);
        if (modelNode != null) {
            parsePackages(modelNode); 
        }
    }

    private XMLNode findModelNode(XMLNode node) {
        for (XMLNode child : node.children()) {
            if ("uml:Model".equals(child.getName())) {
                return child;
            }
        }
        return null; 
    }

    private void parsePackages(XMLNode modelNode) {
        for (XMLNode packageElement : modelNode.children()) {
            if ("uml:Package".equals(packageElement.attribute("xmi:type"))) {
                CustomPackage customPackage = parsePackage(packageElement);
                packages.put(customPackage.getName(), customPackage);
            }
        }
    }

    private CustomPackage parsePackage(XMLNode packageNode) {
        String packageName = packageNode.attribute("name");
        CustomPackage customPackage = new CustomPackage(packageName);

        for (XMLNode typeElement : packageNode.children()) {
            String xmiType = typeElement.attribute("xmi:type");
            if (xmiType != null) {
                switch (xmiType) {
                    case "uml:Class":
                        customPackage.addClass(parseClass(typeElement));
                        break;
                    case "uml:Interface":
                        customPackage.addInterface(parseInterface(typeElement));
                        break;
                    case "uml:Enumeration":
                        customPackage.addEnum(parseEnum(typeElement));
                        break;
                }
            }
        }

        return customPackage;
    }

    private ClassInfo parseClass(XMLNode classNode) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setName(classNode.attribute("name"));

        for (XMLNode memberNode : classNode.children()) {
            switch (memberNode.getName()) {
                case "ownedAttribute":
                    classInfo.addField(parseField(memberNode));
                    break;
                case "ownedOperation":
                    classInfo.addMethod(parseMethod(memberNode));
                    break;
            }
        }

        return classInfo;
    }

    private FieldInfo parseField(XMLNode fieldNode) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(fieldNode.attribute("name"));
        String typeHref = fieldNode.child("type").attribute("href");

        if (typeHref != null) {
            if (typeHref.contains("String")) {
                fieldInfo.setType("String");
            } else if (typeHref.contains("List")) {
                fieldInfo.setType("List");
            } else if (typeHref.contains("Utilisateur")) {
                fieldInfo.setType("Utilisateur");
            } else if (typeHref.contains("Commande")) {
                fieldInfo.setType("Commande");
            } else {
                fieldInfo.setType("Unknown");
            }
        } else {
            fieldInfo.setType("Unknown");
        }

        return fieldInfo;
    }

    private MethodInfo parseMethod(XMLNode methodNode) {
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setName(methodNode.attribute("name"));

        for (XMLNode paramNode : methodNode.children()) {
            if ("parameter".equals(paramNode.getName())) {
                methodInfo.addParameter(parseParameter(paramNode));
            }
        }

        return methodInfo;
    }

    private ParameterInfo parseParameter(XMLNode paramNode) {
        ParameterInfo paramInfo = new ParameterInfo();
        paramInfo.setName(paramNode.attribute("name"));
        paramInfo.setType(paramNode.attribute("type"));
        return paramInfo;
    }

    private InterfaceInfo parseInterface(XMLNode interfaceNode) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setName(interfaceNode.attribute("name"));
        return interfaceInfo;
    }

    private EnumInfo parseEnum(XMLNode enumNode) {
        EnumInfo enumInfo = new EnumInfo();
        enumInfo.setName(enumNode.attribute("name"));
        return enumInfo;
    }

    public Map<String, CustomPackage> getPackages() {
        return packages;
    }
}
