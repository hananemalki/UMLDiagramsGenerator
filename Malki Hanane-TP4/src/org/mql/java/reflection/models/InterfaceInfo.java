package org.mql.java.reflection.models;


public class InterfaceInfo extends TypeInfo {
    public InterfaceInfo(Class<?> clazz) {
        super(clazz);
    }
    public InterfaceInfo() {
        //TODO Auto-generated constructor stub
    }
    @Override
    public String toString() {
        return getName();  
    }
    
}

