package org.mql.java.reflection.xml;

import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.models.MethodInfo;
import org.mql.java.reflection.models.FieldInfo;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {

    public static Document generateXML(List<ClassInfo> classes, String packageName) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("project");
        doc.appendChild(rootElement);

        createPackageXML(classes, doc, rootElement, packageName);
        
        String filePath = "resources/generatedXML/" + packageName.replace('.', '/') + ".xml";
        saveXMLToFile(doc, filePath);
        return doc;
    }

    private static void createPackageXML(List<ClassInfo> classes, Document doc, Element parentElement, String packageName) {
        Element packageElement = doc.createElement("package");
        parentElement.appendChild(packageElement);

        packageElement.setAttribute("name", packageName);

        for (ClassInfo classInfo : classes) {
            createClassXML(classInfo, doc, packageElement);
        }
    }

    private static void createClassXML(ClassInfo classInfo, Document doc, Element parentElement) {
        Element classElement = doc.createElement("class");
        parentElement.appendChild(classElement);

        Element classNameElement = doc.createElement("name");
        classNameElement.appendChild(doc.createTextNode(classInfo.getName()));
        classElement.appendChild(classNameElement);

        Element fieldsElement = doc.createElement("fields");
        classElement.appendChild(fieldsElement);
        List<FieldInfo> fields = classInfo.getFields();
        for (FieldInfo field : fields) {
            createFieldXML(field, doc, fieldsElement);
        }

        Element methodsElement = doc.createElement("methods");
        classElement.appendChild(methodsElement);
        List<MethodInfo> methods = classInfo.getMethods();
        for (MethodInfo method : methods) {
            createMethodXML(method, doc, methodsElement);
        }
    }

    private static void createFieldXML(FieldInfo field, Document doc, Element parentElement) {
        Element fieldElement = doc.createElement("field");
        parentElement.appendChild(fieldElement);
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(field.getName()));
        fieldElement.appendChild(nameElement);

        Element typeElement = doc.createElement("type");
        typeElement.appendChild(doc.createTextNode(field.getType()));
        fieldElement.appendChild(typeElement);
    }

    private static void createMethodXML(MethodInfo method, Document doc, Element parentElement) {
        Element methodElement = doc.createElement("method");
        parentElement.appendChild(methodElement);

        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(method.getName()));
        methodElement.appendChild(nameElement);

        Element returnTypeElement = doc.createElement("returnType");
        returnTypeElement.appendChild(doc.createTextNode(method.getReturnType()));
        methodElement.appendChild(returnTypeElement);
    }

    public static void saveXMLToFile(Document doc, String filePath) throws TransformerException, IOException {
        File file = new File(filePath);
        
        file.getParentFile().mkdirs();

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(file);
        transformer.transform(new DOMSource(doc), result);

        System.out.println("XML file saved to: " + file.getAbsolutePath());
    }

    public static void printXML(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }
}
