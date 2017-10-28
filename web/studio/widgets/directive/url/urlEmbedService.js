enablix.studioApp.factory('UrlEmbedService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 			
	 		var getEmbedInfo = function(_url, _onSuccess, _onError) {
	 			
	 			var params = {
	 				"url": _url
	 			};
	 			
	 			RESTService.getForData("getEmbedInfo", params, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		return {
	 			getEmbedInfo : getEmbedInfo
	 		};
	 	}
	 ]);