package org.mql.java.reflection.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

public class ParameterInfo {
    private String name;
    private String type;
    private List<AnnotationInfo> annotations;
    
    public ParameterInfo(Parameter parameter) {
        this.name = parameter.getName();
        this.type = parameter.getType().getSimpleName();
        this.annotations = new Vector<>();
        for(Annotation ann : parameter.getDeclaredAnnotations()) {
            this.annotations.add(new AnnotationInfo(ann.annotationType()));
        }
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<AnnotationInfo> getAnnotations() { return annotations; }

	@Override
	public String toString() {
		return "ParameterInfo [name=" + name + ", type=" + type + ", annotations=" + annotations + "]";
	}
    
    
}