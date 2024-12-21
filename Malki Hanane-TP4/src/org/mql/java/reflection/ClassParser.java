package org.mql.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;

public class ClassParser {
    private Class<?> cls;
    private StringBuilder builder;
    
    public Class<?> getCls() {
        return cls;
    }
    public String getClassName() {
        return cls.getName();
    }

    public ClassParser(String className) {
        try {
            this.cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className);
        }
    }
    private Class<?>[] getDeclaredClasses() {
        return cls.getDeclaredClasses(); 
    }
    public String getPackages() {
    	builder.append("Package de la classe : \n")
    		   .append(cls.getPackageName()).append(" \n ");
    	Class<?>[] declaredClasses = getDeclaredClasses();
    	if(declaredClasses.length > 0) {
        	builder.append("Package de la classe interne  : \n");
        	for (Class<?> innerClass : declaredClasses) {
                builder.append(innerClass.getName()).append(" : ").append(innerClass.getPackageName()).append("\n");
            }
    	}
    	else {
    		builder.append("Pas de classes internes \n");
    	}
    	return builder.toString();
    }
    
    
    public String getInterfaces() {
    	Class<?>[] interfaces = cls.getInterfaces(); 
    	builder.append(" Interface [ ");
    	for (Class<?> inter : interfaces) {
			builder.append(inter.getName()).append(" ");
		}
    	builder.setLength(builder.length() -2);
    	builder.append(" ] ");
    	return builder.toString();
    }
    
    public String getEnnumerations() {
    	Class<?>[] declaredClasses = getDeclaredClasses();
    	boolean hasEnum =false;
    	for (Class<?> decClass : declaredClasses) {
			if(decClass.isEnum()) {
				hasEnum = true;
                builder.append("Enum: ").append(decClass.getName()).append("\n");
                Object[] enums = decClass.getEnumConstants();
                for (Object e :enums) {
                    builder.append("  - ").append(e.toString()).append("\n");
                }
			}
    	}
		if (!hasEnum) {
            return "Aucune ennumeration .";
        }
	
    	return builder.toString();
    }
    
    public String getAnnotations() {
    	Annotation[] annotations = cls.getAnnotations();
    	for (Annotation annotation : annotations) {
			builder.append("Annotations :\n");
			builder.append(annotation.annotationType().getName()).append("\n");
		}
    	return builder.toString();
    }
    
    
    
    
   
    
   
    



    
}
