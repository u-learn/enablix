enablix.studioApp.factory('RecommendationService', 
	[	'RESTService', 'Notification',
	 	function(RESTService, Notification) {
		
			var getRecommendations = function(_containerQId, _contentIdentity, _onSuccess) {
				
				var params = {"containerQId" : _containerQId, 
							  "contentIdentity" : _contentIdentity};
				
				var recoUrlKey = "contentSpecificRecommendation";
				
				if (isNullOrUndefined(_containerQId) && isNullOrUndefined(_contentIdentity)) {
					recoUrlKey = "generalRecommendation";
					
				} else if (!isNullOrUndefined(_containerQId) && isNullOrUndefined(_contentIdentity)) {
					recoUrlKey = "containerSpecificRecommendation"
				}
				
				return RESTService.getForData(recoUrlKey, params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading recommendations", delay: enablix.errorMsgShowTime});
					});
				
			};
			
			var fetchRecommendationList = function(_onSuccess) {
				
				return RESTService.getForData("fetchRecommendationList", null, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading recommendation list", delay: enablix.errorMsgShowTime});
					});
				
			};
			
			var addRecommendation = function(_containerQId, _contentIdentity, _onSuccess, _onError) {
				
				var reco = {
						recommendationScope : { 
							templateId : enablix.templateId,
						},
						recommendedData : {
							data : {
								templateId : enablix.templateId,
								containerQId : _containerQId,
								instanceIdentity : _contentIdentity
							}
						}
					};
				
					
				RESTService.postForData("saveRecommendation", null, reco, null, _onSuccess, _onError);
			};
			
			var deleteRecommendation = function(_recoIdentity, _onSuccess, _onError) {
				RESTService.postForData("deleteRecommendation", null, _recoIdentity, null, _onSuccess, _onError);
			};
	
			return {
				getRecommendations : getRecommendations,
				addRecommendation : addRecommendation,
				deleteRecommendation : deleteRecommendation,
				fetchRecommendationList : fetchRecommendationList
			};
		
		}
	]);