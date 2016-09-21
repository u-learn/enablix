enablix.studioApp.factory('DocService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 			
	 		var getDocMetadata = function(_docIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 				"docIdentity": _docIdentity
	 			};
	 			
	 			RESTService.getForData("getDocMetadata", params, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		return {
	 			getDocMetadata : getDocMetadata
	 		};
	 	}
	 ]);