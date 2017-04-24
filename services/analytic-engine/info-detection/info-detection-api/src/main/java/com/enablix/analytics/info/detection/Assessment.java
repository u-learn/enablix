package com.enablix.analytics.info.detection;

import java.util.List;

public class Assessment {

	private List<TypeOpinion> typeOpinions;

	public List<TypeOpinion> getTypeOpinions() {
		return typeOpinions;
	}

	public void addOpinion(Opinion opinion) {
		// TODO:
	}

	public void addOpinions(List<Opinion> opinions) {
		
		if (opinions != null) {
		
			for (Opinion opinion : opinions) {
				addOpinion(opinion);
			}
		}
	}
	
}
