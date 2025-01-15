package org.mql.java.reflection.models;


public class InterfaceInfo extends TypeInfo {
    public InterfaceInfo(Class<?> clazz) {
        super(clazz);
    }
    @Override
    public String toString() {
        return getName();  
    }
    
}

