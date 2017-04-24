package com.enablix.wordpress.model;

import com.afrozaar.wordpress.wpapi.v2.model.Term;
import com.enablix.analytics.info.detection.InfoTag;

public class WPInfoTag implements InfoTag {

	private Term wpTerm;
	
	protected WPInfoTag() {
		// for ORM
	}
	
	public WPInfoTag(Term wpTerm) {
		super();
		this.wpTerm = wpTerm;
	}

	public Term getWpTerm() {
		return wpTerm;
	}

	@Override
	public String tag() {
		return wpTerm.getName();
	}

}
