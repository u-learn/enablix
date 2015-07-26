enablix.studioApp.factory('PeerContentService', 
	[	'RESTService', 'Notification',
	 	function(RESTService, Notification) {
		
			var getPeers = function(_containerQId, _contentIdentity, _onSuccess) {
				
				var params = {"containerQId" : _containerQId, 
							  "contentIdentity" : _contentIdentity};
				
				var recoUrlKey = "contentPeers";
				
				return RESTService.getForData("contentPeers", params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading peers", delay: enablix.errorMsgShowTime});
					});
				
			};
	
			return {
				getPeers : getPeers
			};
		
		}
	]);