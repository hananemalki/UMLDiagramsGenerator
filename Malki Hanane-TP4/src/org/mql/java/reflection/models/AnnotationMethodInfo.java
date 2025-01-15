package org.mql.java.reflection.models;

import java.lang.reflect.Method;

public class AnnotationMethodInfo extends MethodInfo {
    private Object defaultValue;
    
    public AnnotationMethodInfo(Method method) {
        super(method);
        this.defaultValue = method.getDefaultValue();
    }
    
    public Object getDefaultValue() { return defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }

	@Override
	public String toString() {
		return "AnnotationMethodInfo [defaultValue=" + defaultValue + "]";
	}
    
}