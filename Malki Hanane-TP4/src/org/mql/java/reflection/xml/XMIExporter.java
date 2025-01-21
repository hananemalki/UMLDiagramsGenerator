package org.mql.java.reflection.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.RelationInfo;
import org.mql.java.reflection.models.TypeInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

public class XMIExporter {
    private Document document;

    public void exportToXMI(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes, String outputPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            // Création de l'élément racine
            Element xmiElement = document.createElement("XMI");
            xmiElement.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
            xmiElement.setAttribute("xmlns:uml", "http://www.omg.org/spec/UML/20090901");
            document.appendChild(xmiElement);

            // Création du modèle UML
            Element modelElement = document.createElement("uml:Model");
            modelElement.setAttribute("xmi:id", "model1");
            modelElement.setAttribute("name", "MyProject");
            xmiElement.appendChild(modelElement);

            // Parcourir les packages et ajouter leurs éléments
            for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                Element packageElement = document.createElement("packagedElement");
                packageElement.setAttribute("xmi:type", "uml:Package");
                packageElement.setAttribute("xmi:id", "package_" + packageEntry.getKey());
                packageElement.setAttribute("name", packageEntry.getKey());
                modelElement.appendChild(packageElement);

                Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();

                // Ajouter les classes
                processTypeList(typesMap.get("Classes"), "uml:Class", packageElement);
            }

            // Sauvegarder le fichier XMI
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(outputPath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTypeList(List<TypeInfo> types, String elementType, Element parentElement) {
        if (types == null || types.isEmpty()) return;

        for (TypeInfo type : types) {
            Element typeElement = document.createElement("packagedElement");
            typeElement.setAttribute("xmi:type", elementType);
            typeElement.setAttribute("xmi:id", "class_" + type.getName());
            typeElement.setAttribute("name", type.getName());
            parentElement.appendChild(typeElement);

            // Ajouter les relations pour les ClassInfo
            if (type instanceof ClassInfo) {
                ClassInfo classInfo = (ClassInfo) type;
                for (RelationInfo relation : classInfo.getRelations()) {
                    Element relationElement = document.createElement("ownedAttribute");
                    relationElement.setAttribute("xmi:id", "rel_" + relation.getSource() + "_" + relation.getTarget());
                    relationElement.setAttribute("name", relation.getType());
                    typeElement.appendChild(relationElement);
                }
            }
        }
    }
}
