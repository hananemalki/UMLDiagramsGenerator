package org.mql.java.reflection.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class ClassInfo extends TypeInfo {
    private String superClass;
    private List<String> implementedInterfaces; 
    private List<RelationInfo> relations; 
    private List<FieldInfo> fields;  
    private List<MethodInfo> methods;
    
    public ClassInfo(Class<?> clazz) {
        super(clazz);
        this.superClass = clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : null;
        this.implementedInterfaces = new Vector<>();
        this.relations = new Vector<>();
        this.fields = new Vector<>(); 
        this.methods = new Vector<>(); 

        for (Class<?> iface : clazz.getInterfaces()) {
            implementedInterfaces.add(iface.getName());
        }

        for (Field field : clazz.getDeclaredFields()) {
            fields.add(new FieldInfo(field));  
        }

        for (Method method : clazz.getDeclaredMethods()) {
            methods.add(new MethodInfo(method));  
        }
    }
    public ClassInfo() {
        this.implementedInterfaces = new Vector<>();
        this.relations = new Vector<>();
        this.fields = new Vector<>(); 
        this.methods = new Vector<>(); 
    }
    public String getSuperClass() { return superClass; }
    public void setSuperClass(String superClass) { this.superClass = superClass; }
    
    public List<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}
	public void setImplementedInterfaces(List<String> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}
	public List<RelationInfo> getRelations() {
		return relations;
	}
	public void setRelations(List<RelationInfo> relations) {
		this.relations = relations;
	}
	public void addRelation(RelationInfo relation) {
        if (relation != null && !relations.contains(relation)) {
            relations.add(relation);
        }
	}

    public List<FieldInfo> getFields() {
        return fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }
    public void addField(FieldInfo fieldInfo) {
        if (!fields.contains(fieldInfo)) {
            fields.add(fieldInfo);
        }
    }

    public void addMethod(MethodInfo methInfo) {
        if (!methods.contains(methInfo)) {
            methods.add(methInfo);
        }
    }

    public void addImplementedInterface(String interfaceName) {
        if (!implementedInterfaces.contains(interfaceName)) {
            implementedInterfaces.add(interfaceName);
        }
    }

	@Override
    public String toString() {
        String superClassName = superClass != null ? superClass : "None";
        return String.format("%s [extends %s]",
            getName(),  
            superClassName);
    }
    
    
}