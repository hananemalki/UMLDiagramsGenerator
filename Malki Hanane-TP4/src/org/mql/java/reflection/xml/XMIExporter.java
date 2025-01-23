package org.mql.java.reflection.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.mql.java.reflection.models.*;
import java.io.File;
import java.util.*;

public class XMIExporter {
    private Document document;
    private Map<String, Element> elementCache = new HashMap<>();

    public void exportToXMI(Map<String, Map<String, List<TypeInfo>>> packagesAndTypes, String outputPath) {
        try {
            initializeDocument();
            Element modelElement = createModelElement();
            
            // Première passe : créer tous les éléments
            for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                Element packageElement = createPackageElement(packageEntry.getKey());
                modelElement.appendChild(packageElement);
                
                Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
                processAllTypes(typesMap, packageElement);
            }
            
            // Deuxième passe : ajouter toutes les relations
            for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                Map<String, List<TypeInfo>> typesMap = packageEntry.getValue();
                processAllRelations(typesMap);
            }

            saveDocument(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
        
        Element xmiElement = document.createElement("xmi:XMI");
        xmiElement.setAttribute("xmlns:xmi", "http://www.omg.org/spec/XMI/20131001");
        xmiElement.setAttribute("xmlns:uml", "http://www.eclipse.org/uml2/5.0.0/UML");
        xmiElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        document.appendChild(xmiElement);
    }

    private Element createModelElement() {
        Element modelElement = document.createElement("uml:Model");
        modelElement.setAttribute("xmi:id", "model");
        modelElement.setAttribute("name", "ProjectModel");
        document.getDocumentElement().appendChild(modelElement);
        return modelElement;
    }

    private Element createPackageElement(String packageName) {
        Element packageElement = document.createElement("packagedElement");
        packageElement.setAttribute("xmi:type", "uml:Package");
        packageElement.setAttribute("xmi:id", "package_" + packageName.replace('.', '_'));
        packageElement.setAttribute("name", packageName);
        elementCache.put(packageName, packageElement);
        return packageElement;
    }

    private void processAllTypes(Map<String, List<TypeInfo>> typesMap, Element packageElement) {
        processTypeList(typesMap.get("Classes"), "uml:Class", packageElement);
        processTypeList(typesMap.get("Interfaces"), "uml:Interface", packageElement);
        processTypeList(typesMap.get("Enums"), "uml:Enumeration", packageElement);
    }

    private void processTypeList(List<TypeInfo> types, String elementType, Element parentElement) {
        if (types == null) return;

        for (TypeInfo type : types) {
            Element typeElement = document.createElement("packagedElement");
            typeElement.setAttribute("xmi:type", elementType);
            String typeId = "type_" + type.getName().replace('.', '_');
            typeElement.setAttribute("xmi:id", typeId);
            typeElement.setAttribute("name", type.getName());
            
            elementCache.put(type.getName(), typeElement);
            parentElement.appendChild(typeElement);

            if (type instanceof ClassInfo) {
                processClassMembers((ClassInfo) type, typeElement);
            }
        }
    }

    private void processClassMembers(ClassInfo classInfo, Element classElement) {
        // Attributs
        for (FieldInfo field : classInfo.getFields()) {
            Element attributeElement = document.createElement("ownedAttribute");
            attributeElement.setAttribute("xmi:id", "attr_" + classInfo.getName() + "_" + field.getName());
            attributeElement.setAttribute("name", field.getName());

            Element typeElement = document.createElement("type");
            typeElement.setAttribute("xmi:type", "uml:Class");
            typeElement.setAttribute("href", "pathto:" + field.getType()); // Or use appropriate type reference
            attributeElement.appendChild(typeElement);

            attributeElement.setAttribute("visibility", field.getVisibility());
            classElement.appendChild(attributeElement);
        }

        // Méthodes
        for (MethodInfo method : classInfo.getMethods()) {
            Element operationElement = document.createElement("ownedOperation");
            operationElement.setAttribute("xmi:id", "op_" + classInfo.getName() + "_" + method.getName());
            operationElement.setAttribute("name", method.getName());
            operationElement.setAttribute("visibility", method.getVisibility());
            classElement.appendChild(operationElement);
        }
    }

    private void processAllRelations(Map<String, List<TypeInfo>> typesMap) {
        if (typesMap.get("Classes") != null) {
            for (TypeInfo type : typesMap.get("Classes")) {
                if (type instanceof ClassInfo) {
                    processClassRelations((ClassInfo) type);
                }
            }
        }
    }

    private void processClassRelations(ClassInfo classInfo) {
        Element classElement = elementCache.get(classInfo.getName());
        
        for (RelationInfo relation : classInfo.getRelations()) {
            switch (relation.getType()) {
                case "EXTENDS":
                    createGeneralization(classInfo, relation);
                    break;
                case "IMPLEMENTS":
                    createRealization(classInfo, relation);
                    break;
                case "ASSOCIATION":
                    createAssociation(classInfo, relation);
                    break;
                case "DEPENDENCY":
                    createDependency(classInfo, relation);
                    break;
            }
        }
    }

    private void createGeneralization(ClassInfo classInfo, RelationInfo relation) {
        Element generalization = document.createElement("generalization");
        generalization.setAttribute("xmi:id", "gen_" + classInfo.getName() + "_" + relation.getTarget());
        generalization.setAttribute("general", elementCache.get(relation.getTarget()).getAttribute("xmi:id"));
        elementCache.get(classInfo.getName()).appendChild(generalization);
    }

    private void createRealization(ClassInfo classInfo, RelationInfo relation) {
        Element realization = document.createElement("interfaceRealization");
        realization.setAttribute("xmi:id", "real_" + classInfo.getName() + "_" + relation.getTarget());
        realization.setAttribute("client", elementCache.get(classInfo.getName()).getAttribute("xmi:id"));
        realization.setAttribute("supplier", elementCache.get(relation.getTarget()).getAttribute("xmi:id"));
        elementCache.get(classInfo.getName()).appendChild(realization);
    }

    private void createAssociation(ClassInfo classInfo, RelationInfo relation) {
        Element association = document.createElement("packagedElement");
        association.setAttribute("xmi:type", "uml:Association");
        association.setAttribute("xmi:id", "assoc_" + classInfo.getName() + "_" + relation.getTarget());
        
        Element sourceEnd = document.createElement("ownedEnd");
        sourceEnd.setAttribute("xmi:id", "end_" + classInfo.getName() + "_" + relation.getTarget());
        sourceEnd.setAttribute("type", elementCache.get(relation.getTarget()).getAttribute("xmi:id"));
        
        Element targetEnd = document.createElement("ownedEnd");
        targetEnd.setAttribute("xmi:id", "end_" + relation.getTarget() + "_" + classInfo.getName());
        targetEnd.setAttribute("type", elementCache.get(classInfo.getName()).getAttribute("xmi:id"));
        
        association.appendChild(sourceEnd);
        association.appendChild(targetEnd);
        
        document.getDocumentElement().appendChild(association);
    }

    private void createDependency(ClassInfo classInfo, RelationInfo relation) {
        Element dependency = document.createElement("packagedElement");
        dependency.setAttribute("xmi:type", "uml:Dependency");
        dependency.setAttribute("xmi:id", "dep_" + classInfo.getName() + "_" + relation.getTarget());
        dependency.setAttribute("client", elementCache.get(classInfo.getName()).getAttribute("xmi:id"));
        dependency.setAttribute("supplier", elementCache.get(relation.getTarget()).getAttribute("xmi:id"));
        
        document.getDocumentElement().appendChild(dependency);
    }

    private void saveDocument(String outputPath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(outputPath));
        transformer.transform(source, result);
    }
}