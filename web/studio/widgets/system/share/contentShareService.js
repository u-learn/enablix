enablix.studioApp.factory('ContentShareService', 
		[		 'RESTService', 'Notification',
	 	function (RESTService,   Notification) {
		
			var shareContent = function(_containerQId, _contentIdentity, _sharedWithEmailId,emailContent, _onSuccess, _onError) {
				
				var shareData = {"containerQId" : _containerQId, 
							  "contentIdentity" : _contentIdentity,
							  "emailId" : _sharedWithEmailId,
							  "emailCustomContent": emailContent };
				
				RESTService.postForData("sharecontent", null, shareData, null, _onSuccess, _onError);
				
			};
	
			var shareContentToSlack = function(containerQId, contentName,contentIdentity, portalURL ,
					channelID,
					 _onSuccess, _onError) {
				
				var shareSlackData = {"containerQId" : containerQId,
									  "contentName": contentName,
									  "contentIdentity": contentIdentity,
									  "portalURL": portalURL,
									  "channelID": channelID
							  		};
				RESTService.postForData("sendMessageToSlack", shareSlackData, null, null, _onSuccess, _onError);
			};
			return {
				shareContent : shareContent,
				shareContentToSlack:shareContentToSlack
			};
		
		}
	]);
