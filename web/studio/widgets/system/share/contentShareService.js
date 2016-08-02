enablix.studioApp.factory('ContentShareService', 
		[		 'RESTService', 'Notification',
	 	function (RESTService,   Notification) {
		
			var shareContent = function(_containerQId, _contentIdentity, _sharedWithEmailId, _onSuccess, _onError) {
				
				var shareData = {"containerQId" : _containerQId, 
							  "contentIdentity" : _contentIdentity,
							  "emailId" : _sharedWithEmailId};
				
				RESTService.postForData("sharecontent", null, shareData, null, _onSuccess, _onError);
				
			};
	
			return {
				shareContent : shareContent
			};
		
		}
	]);