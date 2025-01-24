package org.mql.java.reflection.xml;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.FieldInfo;
import org.mql.java.reflection.models.MethodInfo;
import org.mql.java.reflection.models.ParameterInfo;
import org.mql.java.reflection.models.RelationInfo;
import org.mql.java.reflection.models.TypeInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;
import java.util.Map;

public class XMLExporter {
    private Document document;

    public void exportToXML(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes, String outputPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element rootElement = document.createElement("project");
            document.appendChild(rootElement);

            for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                Element packageElement = document.createElement("package");
                packageElement.setAttribute("name", packageEntry.getKey());
                rootElement.appendChild(packageElement);

                Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();

                processTypeList(typesMap.get("Classes"), "class", packageElement);

                processTypeList(typesMap.get("Interfaces"), "interface", packageElement);

                processTypeList(typesMap.get("Enums"), "enum", packageElement);

                processTypeList(typesMap.get("Annotations"), "annotation", packageElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(outputPath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTypeList(List<TypeInfo> types, String elementName, Element parentElement) {
        if (types == null || types.isEmpty()) return;
    
        for (TypeInfo type : types) {
            Element typeElement = document.createElement(elementName); // "class", "interface", etc.
            parentElement.appendChild(typeElement);
    
            // Ajouter le nom de la classe
            Element nameElement = document.createElement("name");
            nameElement.appendChild(document.createTextNode(type.getName()));
            typeElement.appendChild(nameElement);
    
            // Ajouter les champs
            if (!type.getFields().isEmpty()) {
                Element fieldsElement = document.createElement("fields");
                typeElement.appendChild(fieldsElement);
    
                for (FieldInfo field : type.getFields()) {
                    Element fieldElement = document.createElement("field");
                    fieldsElement.appendChild(fieldElement);
    
                    Element fieldNameElement = document.createElement("name");
                    fieldNameElement.appendChild(document.createTextNode(field.getName()));
                    fieldElement.appendChild(fieldNameElement);
    
                    Element fieldTypeElement = document.createElement("type");
                    fieldTypeElement.appendChild(document.createTextNode(field.getType()));
                    fieldElement.appendChild(fieldTypeElement);
    
                    Element modifierElement = document.createElement("modifier");
                    modifierElement.appendChild(document.createTextNode(field.getVisibility()));
                    fieldElement.appendChild(modifierElement);
                }
            }
    
            // Ajouter les méthodes
            if (!type.getMethods().isEmpty()) {
                Element methodsElement = document.createElement("methods");
                typeElement.appendChild(methodsElement);
    
                for (MethodInfo method : type.getMethods()) {
                    Element methodElement = document.createElement("method");
                    methodsElement.appendChild(methodElement);
    
                    Element methodNameElement = document.createElement("name");
                    methodNameElement.appendChild(document.createTextNode(method.getName()));
                    methodElement.appendChild(methodNameElement);
    
                    Element returnTypeElement = document.createElement("returnType");
                    returnTypeElement.appendChild(document.createTextNode(method.getReturnType()));
                    methodElement.appendChild(returnTypeElement);
    
                    Element modifierElement = document.createElement("modifier");
                    modifierElement.appendChild(document.createTextNode(method.getVisibility()));
                    methodElement.appendChild(modifierElement);
    
                    // Ajouter les paramètres
                    if (!method.getParameters().isEmpty()) {
                        Element parametersElement = document.createElement("parameters");
                        methodElement.appendChild(parametersElement);
    
                        for (ParameterInfo param : method.getParameters()) {
                            Element paramElement = document.createElement("parameter");
                            parametersElement.appendChild(paramElement);
    
                            Element paramNameElement = document.createElement("name");
                            paramNameElement.appendChild(document.createTextNode(param.getName()));
                            paramElement.appendChild(paramNameElement);
    
                            Element paramTypeElement = document.createElement("type");
                            paramTypeElement.appendChild(document.createTextNode(param.getType()));
                            paramElement.appendChild(paramTypeElement);
                        }
                    }
                }
            }
    
            // Ajouter les relations
            if (type instanceof ClassInfo) {
                ClassInfo classInfo = (ClassInfo) type;
                if (!classInfo.getRelations().isEmpty()) {
                    Element relationsElement = document.createElement("relations");
                    typeElement.appendChild(relationsElement);
    
                    for (RelationInfo relation : classInfo.getRelations()) {
                        Element relationElement = document.createElement("relation");
                        relationsElement.appendChild(relationElement);
    
                        Element relationTypeElement = document.createElement("type");
                        relationTypeElement.appendChild(document.createTextNode(relation.getType()));
                        relationElement.appendChild(relationTypeElement);
    
                        Element targetElement = document.createElement("target");
                        targetElement.appendChild(document.createTextNode(relation.getTarget()));
                        relationElement.appendChild(targetElement);
                    }
                }
            }
        }
    }
}