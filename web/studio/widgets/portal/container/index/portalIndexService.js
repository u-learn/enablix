enablix.studioApp.factory('PortalContainerIndexService', 
	[
	 	'RESTService', 'ContentIndexService',
	 	function(RESTService, ContentIndexService) {
	 		
	 		var getIndexList = function(_containerQId) {
	 			return ContentIndexService.getPortalIndexForContainer(_containerQId);
	 		}
	 		
	 		return {
	 			getIndexList: getIndexList
	 		};
	 	}
	 ]);