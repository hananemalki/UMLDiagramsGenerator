package org.mql.java.reflection.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;
import java.lang.reflect.Modifier;


public class FieldInfo {
    private String name;
    private String type;
    private int modifiers;
    private List<AnnotationInfo> annotations;
    
    public FieldInfo(Field field) {
        this.name = field.getName();
        this.type = field.getType().getSimpleName();
        this.modifiers = field.getModifiers();
        this.annotations = new Vector<>();
        for(Annotation ann : field.getDeclaredAnnotations()) {
            this.annotations.add(new AnnotationInfo(ann.annotationType()));
        }
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getModifiers() { return modifiers; }
    public void setModifiers(int modifiers) { this.modifiers = modifiers; }
    public List<AnnotationInfo> getAnnotations() { return annotations; }

    public String getVisibility() {
        if (Modifier.isPublic(modifiers)) return "public";
        if (Modifier.isProtected(modifiers)) return "protected";
        if (Modifier.isPrivate(modifiers)) return "private";
        return "package"; 
    }

	@Override
	public String toString() {
		return "FieldInfo [name=" + name + ", type=" + type + ", modifiers=" + modifiers + ", annotations="
				+ annotations + "]";
	}
    
    
}
