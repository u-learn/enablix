package com.enablix.app.content.ui.reco;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.reco.Recommendation;

@RestController
@RequestMapping("reco")
public class RecommendationController {

	@Autowired
	private RecommendationManager recoManager;
	
	@RequestMapping(method = RequestMethod.POST, value="/save")
	public void saveRecommendation(@RequestBody Recommendation reco) {
		recoManager.saveRecommendation(reco);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/delete")
	public void deleteRecommendation(@RequestBody String recoIdentity) {
		recoManager.deleteRecommendation(recoIdentity);;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/list")
	public Collection<RecommendationWrapper> getRecommendations() {
		return recoManager.getAllRecommendations();
	}
	
}
