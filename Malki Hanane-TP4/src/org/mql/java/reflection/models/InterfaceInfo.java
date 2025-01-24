package org.mql.java.reflection.models;


public class InterfaceInfo extends TypeInfo {
    public InterfaceInfo(Class<?> clazz) {
        super(clazz);
    }
    public InterfaceInfo() {
    }
    @Override
    public String toString() {
        return getName();  
    }
    
}

