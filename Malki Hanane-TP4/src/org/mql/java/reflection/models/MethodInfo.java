package org.mql.java.reflection.models;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;
public class MethodInfo {
    private String name;
    private String returnType;
    private List<ParameterInfo> parameters;
    private int modifiers;
    private List<AnnotationInfo> annotations;
    
    public MethodInfo(Method method) {
        this.name = method.getName();
        this.returnType = method.getReturnType().getSimpleName();
        this.modifiers = method.getModifiers();
        
        this.parameters = new Vector<>();
        Parameter[] params = method.getParameters();
        for(Parameter param : params) {
            this.parameters.add(new ParameterInfo(param));
        }
        
        this.annotations = new Vector<>();
        for(Annotation ann : method.getDeclaredAnnotations()) {
            this.annotations.add(new AnnotationInfo(ann.annotationType()));
        }
    }
    
    public MethodInfo() {
        this.parameters = new Vector<>();
        this.annotations = new Vector<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public List<ParameterInfo> getParameters() { return parameters; }
    public int getModifiers() { return modifiers; }
    public void setModifiers(int modifiers) { this.modifiers = modifiers; }
    public List<AnnotationInfo> getAnnotations() { return annotations; }

    public String getVisibility() {
        if (Modifier.isPublic(modifiers)) return "public";
        if (Modifier.isProtected(modifiers)) return "protected";
        if (Modifier.isPrivate(modifiers)) return "private";
        return "package"; 
    }

    public void addParameter(ParameterInfo parameter) {
        this.parameters.add(parameter);
    }

    public void addAnnotation(AnnotationInfo annotation) {
        this.annotations.add(annotation);
    }

	@Override
	public String toString() {
		return "MethodInfo [name=" + name + ", returnType=" + returnType + ", parameters=" + parameters + ", modifiers="
				+ modifiers + ", annotations=" + annotations + "]";
	}
    
    
}
