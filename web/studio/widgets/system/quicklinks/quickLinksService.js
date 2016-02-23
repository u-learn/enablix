enablix.studioApp.factory('QuickLinksService', 
	[			'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
		
			var getQuickLinks = function(_onSuccess) {
				var params = {};
				return RESTService.getForData("quickLinks", params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading quick links", delay: enablix.errorMsgShowTime});
					});
			};
			
			var saveQuickLinkCategory = function(_quickLinkSection, _onSuccess, _onError) {
				
				var quickLinkCategory = {
					name: _quickLinkSection.sectionName,
					identity: _quickLinkSection.sectionIdentity
				};
				
				RESTService.postForData("saveQuickLinkCategory", null, quickLinkCategory, null, _onSuccess, _onError);
				
			};
	
			return {
				getQuickLinks : getQuickLinks,
				saveQuickLinkCategory: saveQuickLinkCategory
			};

		
		}
	]);