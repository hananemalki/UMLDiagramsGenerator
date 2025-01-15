package org.mql.java.reflection.models;


public class ClassInfo extends TypeInfo {
    private String superClass;
    
    public ClassInfo(Class<?> clazz) {
        super(clazz);
        if(clazz.getSuperclass() != null) {
            this.superClass = clazz.getSuperclass().getSimpleName();
        }
    }
    
    public String getSuperClass() { return superClass; }
    public void setSuperClass(String superClass) { this.superClass = superClass; }

    @Override
    public String toString() {
        String superClassName = superClass != null ? superClass : "None";
        return String.format("%s [extends %s]",
            getName(),  // Utilise getName() au lieu de clazz.getSimpleName()
            superClassName);
    }
    
    
}