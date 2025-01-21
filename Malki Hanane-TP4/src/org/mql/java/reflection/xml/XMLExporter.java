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

            // Créer l'élément racine
            Element rootElement = document.createElement("project");
            document.appendChild(rootElement);

            // Parcourir tous les packages
            for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                Element packageElement = document.createElement("package");
                packageElement.setAttribute("name", packageEntry.getKey());
                rootElement.appendChild(packageElement);

                Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();

                // Ajouter les classes
                processTypeList(typesMap.get("Classes"), "class", packageElement);

                // Ajouter les interfaces
                processTypeList(typesMap.get("Interfaces"), "interface", packageElement);

                // Ajouter les énumérations
                processTypeList(typesMap.get("Enums"), "enum", packageElement);

                // Ajouter les annotations
                processTypeList(typesMap.get("Annotations"), "annotation", packageElement);
            }

            // Sauvegarder le document XML
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

        Element typesElement = document.createElement(elementName + "es");
        parentElement.appendChild(typesElement);

        for (TypeInfo type : types) {
            Element typeElement = document.createElement(elementName);
            typeElement.setAttribute("name", type.getName());
            typesElement.appendChild(typeElement);

            // Ajouter les champs
            if (!type.getFields().isEmpty()) {
                Element fieldsElement = document.createElement("fields");
                typeElement.appendChild(fieldsElement);

                for (FieldInfo field : type.getFields()) {
                    Element fieldElement = document.createElement("field");
                    fieldElement.setAttribute("name", field.getName());
                    fieldElement.setAttribute("type", field.getType());
                    fieldsElement.appendChild(fieldElement);
                }
            }

            // Ajouter les méthodes
            if (!type.getMethods().isEmpty()) {
                Element methodsElement = document.createElement("methods");
                typeElement.appendChild(methodsElement);

                for (MethodInfo method : type.getMethods()) {
                    Element methodElement = document.createElement("method");
                    methodElement.setAttribute("name", method.getName());
                    methodElement.setAttribute("returnType", method.getReturnType());
                    methodsElement.appendChild(methodElement);

                    // Ajouter les paramètres de la méthode
                    if (!method.getParameters().isEmpty()) {
                        Element parametersElement = document.createElement("parameters");
                        methodElement.appendChild(parametersElement);

                        for (ParameterInfo param : method.getParameters()) {
                            Element paramElement = document.createElement("parameter");
                            paramElement.setAttribute("name", param.getName());
                            paramElement.setAttribute("type", param.getType());
                            parametersElement.appendChild(paramElement);
                        }
                    }
                }
            }

            // Ajouter les relations pour les ClassInfo
            if (type instanceof ClassInfo) {
                ClassInfo classInfo = (ClassInfo) type;
                if (!classInfo.getRelations().isEmpty()) {
                    Element relationsElement = document.createElement("relations");
                    typeElement.appendChild(relationsElement);

                    for (RelationInfo relation : classInfo.getRelations()) {
                        Element relationElement = document.createElement("relation");
                        relationElement.setAttribute("type", relation.getType());
                        relationElement.setAttribute("source", relation.getSource());
                        relationElement.setAttribute("target", relation.getTarget());
                        relationsElement.appendChild(relationElement);
                    }
                }
            }
        }
    }
}