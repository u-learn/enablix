enablix.studioApp.factory('QuickLinksService', 
	[	'RESTService', 'Notification',
	 	function(RESTService, Notification) {
		
			var getQuickLinks = function(_onSuccess) {
				var params = {};
				return RESTService.getForData("quickLinks", params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading quick links", delay: enablix.errorMsgShowTime});
					});
				
			};
	
			return {
				getQuickLinks : getQuickLinks
			};
		
		}
	]);