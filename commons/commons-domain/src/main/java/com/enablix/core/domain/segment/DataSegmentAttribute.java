package com.enablix.core.domain.segment;

public class DataSegmentAttribute {

	public enum Presence {
		REQUIRED, OPTIONAL
	}
	
	public enum Matching {
		STRICT, LENIENT
	}
	
	private String id;
	
	private Object value;
	
	private Presence presence;
	
	private Matching matching;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Presence getPresence() {
		return presence;
	}

	public void setPresence(Presence presence) {
		this.presence = presence;
	}

	public Matching getMatching() {
		return matching;
	}

	public void setMatching(Matching matching) {
		this.matching = matching;
	}
	
}
