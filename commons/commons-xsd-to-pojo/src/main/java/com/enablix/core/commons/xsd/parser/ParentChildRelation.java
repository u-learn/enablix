package com.enablix.core.commons.xsd.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.core.commons.xsdtopojo.BaseContentType;

public class ParentChildRelation {
	
	private static ThreadLocal<ParentChildRelation> BUILDERS = 
			new ThreadLocal<ParentChildRelation>() {
				protected ParentChildRelation initialValue() {
					return new ParentChildRelation();
				}
			};

	public static ParentChildRelation get() {
		return BUILDERS.get();
	}
	
	public static void clear() {
		BUILDERS.remove();
	}
	
	private Map<Object, Object> childParentMap;
			
	private Map<BaseContentType, List<BaseContentType>> relationships;
	
	private boolean stale;
	
	private ParentChildRelation() {
		this.relationships = new HashMap<>();
		this.childParentMap = new HashMap<>();
		this.stale = false;
	}
	
	private List<BaseContentType> addParentAndGetChilds(BaseContentType parent) {
		List<BaseContentType> childs = relationships.get(parent);
		if (childs == null) {
			childs = new ArrayList<BaseContentType>();
			relationships.put(parent, childs);
		}
		return childs;
	}
	
	public void addRelation(Object parent, Object child) {
		stale = true;
		childParentMap.put(child, parent);
	}
	
	private void buildChildrenList(Object parent, Object child) {
		
		if (parent instanceof BaseContentType
				&& child instanceof BaseContentType) {
			addRelation((BaseContentType) parent, (BaseContentType) child);
		
		} else if (parent instanceof BaseContentType) {
			addParentAndGetChilds((BaseContentType) parent);
		
		} else if (child instanceof BaseContentType) {
			BaseContentType bcParent = getBaseContentParent(child);
			if (bcParent != null) {
				addRelation(bcParent, (BaseContentType) child);
			}
		}
		
	}
	
	private void buildChildrenList() {
		relationships.clear();
		for (Map.Entry<Object, Object> entry : childParentMap.entrySet()) {
			buildChildrenList(entry.getValue(), entry.getKey());
		}
		stale = false;
	}
	
	private BaseContentType getBaseContentParent(Object child) {
		
		BaseContentType returnValue = null;
		Object parent = childParentMap.get(child);
		
		if (parent == null) {
			// do nothing. will break here
		} else if (parent instanceof BaseContentType) {
			returnValue = (BaseContentType) parent;
		
		} else {
			returnValue = getBaseContentParent(parent);
		}
		
		return returnValue;
	}
	
	protected void addRelation(BaseContentType parent, BaseContentType child) {
		List<BaseContentType> childs = addParentAndGetChilds(parent);
		childs.add(child);
	}

	public Map<BaseContentType, List<BaseContentType>> getRelationships() {
		if (stale) {
			buildChildrenList();
		}
		return relationships;
	}
	
	public List<BaseContentType> getChildren(BaseContentType parent) {
		return getRelationships().get(parent);
	}
	
}
