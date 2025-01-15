package org.mql.java.reflection.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class TypeInfo {
	
    private String name;
    private String packageName;
    private int modifiers;
    private List<FieldInfo> fields;
    private List<MethodInfo> methods;
    private List<String> interfaces;
    
    public TypeInfo() {
        fields = new Vector<>();
        methods = new Vector<>();
        interfaces = new Vector<>();
    }
    
    public TypeInfo(Class<?> clazz) {
        this();
        this.name = clazz.getSimpleName();
        this.packageName = clazz.getPackage().getName();
        this.modifiers = clazz.getModifiers();
        
        // Extraire les interfaces
        for(Class<?> iface : clazz.getInterfaces()) {
            interfaces.add(iface.getSimpleName());
        }
        
        // Extraire les champs
        for(Field field : clazz.getDeclaredFields()) {
            fields.add(new FieldInfo(field));
        }
        
        // Extraire les méthodes
        for(Method method : clazz.getDeclaredMethods()) {
            methods.add(new MethodInfo(method));
        }
    }
    
    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public int getModifiers() { return modifiers; }
    public void setModifiers(int modifiers) { this.modifiers = modifiers; }
    public List<FieldInfo> getFields() { return fields; }
    public List<MethodInfo> getMethods() { return methods; }
    public List<String> getInterfaces() { return interfaces; }

	
    
    
}
