enablix.studioApp.factory('ResourceVersionHolder', 
		[
		 			'Notification',
		 	function(Notification) {
		 		
		 		var resourceVersions = null;
		 		var resourceVersionHeaders = {};
		 				
		 		var getResourceVersionHeaders = function() {
		 			return resourceVersionHeaders;
		 		};
		 		
		 		var updateResourceVersionHeaders = function(_resourceVersions) {
		 			
		 			resourceVersions = _resourceVersions;
		 			
		 			resourceVersionHeaders = {};
					angular.forEach(resourceVersions, function(resVersion) {
						resourceVersionHeaders["version." + resVersion.resourceName] = resVersion.resourceVersion;
					});
		 		};
		 		
		 		return {
		 			getResourceVersionHeaders: getResourceVersionHeaders,
		 			updateResourceVersionHeaders: updateResourceVersionHeaders
		 		};
		 	}
		 ]);



enablix.studioApp.factory('ResourceVersionService', 
	[
	 			'Notification', 'RESTService', 'ResourceVersionHolder',
	 	function(Notification,   RESTService,   ResourceVersionHolder) {
	 		
	 		var loadResourceVersions = function() {
	 			
				return RESTService.getForData("loadResourceVersions", null, null, function(data) {
					ResourceVersionHolder.updateResourceVersionHeaders(data);
				}, function(errorData) {
					Notification.error({message: "Error getting resource versions", delay: enablix.errorMsgShowTime});
				});
	 		};
	 		
	 		return {
	 			loadResourceVersions: loadResourceVersions
	 		};
	 	}
	 ]);

