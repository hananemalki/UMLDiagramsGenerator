package org.mql.java.reflection.models;

import java.util.List;
import java.util.Vector;

public class ClassInfo extends TypeInfo {
    private String superClass;
    
    private List<String> implementedInterfaces; 
    private List<RelationInfo> relations; 
    
//    public ClassInfo(Class<?> clazz) {
//        super(clazz);
//        if(clazz.getSuperclass() != null) {
//            this.superClass = clazz.getSuperclass().getSimpleName();
//        }
//    }
    public ClassInfo(Class<?> clazz) {
        super(clazz);
        this.superClass = clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : null;
        this.implementedInterfaces = new Vector<>();
        this.relations = new Vector<>();

        for (Class<?> iface : clazz.getInterfaces()) {
            implementedInterfaces.add(iface.getName());
        }
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
	    if (!relations.contains(relation)) {
	        relations.add(relation);
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