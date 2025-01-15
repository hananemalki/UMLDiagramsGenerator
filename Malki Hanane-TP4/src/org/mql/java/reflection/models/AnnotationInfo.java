package org.mql.java.reflection.models;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class AnnotationInfo extends TypeInfo {
    private List<AnnotationMethodInfo> annotationMethods;
    
    public AnnotationInfo(Class<?> clazz) {
        super(clazz);
        annotationMethods = new Vector<>();
        for(Method method : clazz.getDeclaredMethods()) {
            annotationMethods.add(new AnnotationMethodInfo(method));
        }
    }
    
    public List<AnnotationMethodInfo> getAnnotationMethods() { return annotationMethods; }

//	@Override
//	public String toString() {
//		return "AnnotationInfo [annotationMethods=" + annotationMethods + "]";
//	}
    @Override
    public String toString() {
        return String.format("%s [methods: %d]", getName(), annotationMethods.size());
    }
}