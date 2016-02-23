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
			
			var getContentQuickLinkAssociation = function(_contentIdentity, _onSuccess) {
				
				var params = {
						contentIdentity: _contentIdentity
				};
				
				return RESTService.getForData("getQuickLinkCategoryAssociation", params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading quick links", delay: enablix.errorMsgShowTime});
					});
			};
			
			var addQuickLink = function(_quickLinkCategoryIdentity, _containerQId, _contentIdentity, _onSuccess, _onError) {
				
				var quickLink = {
					quickLinkCategoryIdentity : _quickLinkCategoryIdentity,
					linkContent : {
						containerQId : _containerQId,
						instanceIdentity : _contentIdentity
					}
				}
				
				RESTService.postForData("addQuickLinks", null, quickLink, null, _onSuccess, _onError);
			};
			
			var deleteQuickLink = function(_quickLinkIdentity, _onSuccess, _onError) {
				RESTService.postForData("deleteQuickLinks", null, _quickLinkIdentity, null, _onSuccess, _onError);
			}
	
			return {
				getQuickLinks : getQuickLinks,
				saveQuickLinkCategory: saveQuickLinkCategory,
				getContentQuickLinkAssociation: getContentQuickLinkAssociation,
				addQuickLink: addQuickLink,
				deleteQuickLink: deleteQuickLink
			};

		
		}
	]);