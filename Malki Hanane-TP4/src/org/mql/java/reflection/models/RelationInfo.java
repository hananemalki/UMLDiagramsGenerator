package org.mql.java.reflection.models;

import java.util.Objects;

public class RelationInfo {
	private String source; 
    private String target; 
    private String type;
	public RelationInfo() {
	}
	public RelationInfo(String source, String target, String type) {
		super();
		this.source = source;
		this.target = target;
		this.type = type;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "RelationInfo [source=" + source + ", target=" + target + ", type=" + type + "]";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    RelationInfo that = (RelationInfo) o;
	    return source.equals(that.source) &&
	           target.equals(that.target) &&
	           type.equals(that.type);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(source, target, type);
	}
	


}
