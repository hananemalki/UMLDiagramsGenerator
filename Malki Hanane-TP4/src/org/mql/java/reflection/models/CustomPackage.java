package org.mql.java.reflection.models;

import java.util.List;
import java.util.Vector;

public class CustomPackage {
    private String name;
    private List<ClassInfo> classes;
    private List<InterfaceInfo> interfaces;
    private List<EnumInfo> enums;
    private List<AnnotationInfo> annotations;

    public CustomPackage(String name) {
        this.name = name;
        this.classes = new Vector<>();
        this.interfaces = new Vector<>();
        this.enums = new Vector<>();
        this.annotations = new Vector<>();
    }

    public void addClass(ClassInfo classInfo) {
        classes.add(classInfo);
    }

    public void addInterface(InterfaceInfo interfaceInfo) {
        interfaces.add(interfaceInfo);
    }

    public void addEnum(EnumInfo enumInfo) {
        enums.add(enumInfo);
    }

    public void addAnnotation(AnnotationInfo annotationInfo) {
        annotations.add(annotationInfo);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClassInfo> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassInfo> classes) {
		this.classes = classes;
	}

	public List<InterfaceInfo> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceInfo> interfaces) {
		this.interfaces = interfaces;
	}

	public List<EnumInfo> getEnums() {
		return enums;
	}

	public void setEnums(List<EnumInfo> enums) {
		this.enums = enums;
	}

	public List<AnnotationInfo> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationInfo> annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		return "CustomPackage [name=" + name + ", classes=" + classes + ", interfaces=" + interfaces + ", enums="
				+ enums + ", annotations=" + annotations + "]";
	}

    
}
