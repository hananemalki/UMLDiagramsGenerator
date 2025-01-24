package org.mql.java.reflection.models;

import java.util.List;
import java.util.Vector;

public class EnumInfo extends TypeInfo {
    private List<String> enumConstants;
    
    public EnumInfo(Class<?> clazz) {
        super(clazz);
        enumConstants = new Vector<>();
        Object[] constants = clazz.getEnumConstants();
        if (constants != null) {
            for(Object constant : constants) {
                enumConstants.add(constant.toString());
            }
        }
    }
    
    public EnumInfo() {
    }

    public List<String> getEnumConstants() { return enumConstants; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" [");
        
        for(int i = 0; i < enumConstants.size(); i++) {
            sb.append(enumConstants.get(i));
            if(i < enumConstants.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
	
    
}
