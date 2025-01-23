package org.mql.java.reflection.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class RelationExtractor {
    public static void extractRelations(ClassInfo classInfo, Class<?> clazz) {
        if (classInfo.getSuperClass() != null) {
            classInfo.addRelation(new RelationInfo(clazz.getName(), classInfo.getSuperClass(), "extends"));
        }

        for (String iface : classInfo.getImplementedInterfaces()) {
            classInfo.addRelation(new RelationInfo(clazz.getName(), iface, "implements"));
        }

        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (!fieldType.isPrimitive()) {
                if (isComposition(field)) {
                    classInfo.addRelation(new RelationInfo(clazz.getName(), fieldType.getName(), "composition"));
                } else {
                    classInfo.addRelation(new RelationInfo(clazz.getName(), fieldType.getName(), "aggregation"));
                }
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            for (Class<?> paramType : method.getParameterTypes()) {
                classInfo.addRelation(new RelationInfo(clazz.getName(), paramType.getName(), "uses"));
            }
            if (method.getReturnType() != void.class) {
                classInfo.addRelation(new RelationInfo(clazz.getName(), method.getReturnType().getName(), "returns"));
            }
        }
    }

    // private static boolean isComposition(Field field) {
    //     return field.getType().getSimpleName().equals("Form");
    // }
    private static boolean isComposition(Field field) {
        Class<?> fieldType = field.getType();
        
        return !fieldType.isPrimitive() && 
               !fieldType.getName().startsWith("java.lang") &&
               !fieldType.getName().startsWith("java.util") &&
               (fieldType.getSimpleName().equals("Form") || 
                fieldType.getName().contains("Component") ||
                fieldType.getName().contains("Panel"));
    }
}