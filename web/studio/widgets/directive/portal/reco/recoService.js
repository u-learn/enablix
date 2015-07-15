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
	
			return {
				getRecommendations : getRecommendations
			};
		
		}
	]);