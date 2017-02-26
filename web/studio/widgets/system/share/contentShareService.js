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
	
			var shareContentToSlack = function(containerQId, contentIdentity, 
					channelsSelectd,slackCustomContent,
					 _onSuccess, _onError) {
				var channelIds = [];
				for(var i=0; i < channelsSelectd.length;i++){
					channelIds.push(channelsSelectd[i].id);
				}
				var shareSlackData = {"containerQId" : containerQId,
									  "contentIdentity": contentIdentity,
									  "channelsSelectd": channelIds,
									  "slackCustomContent" : slackCustomContent
							  		};
				RESTService.postForData("sendMessageToSlack", shareSlackData, null, null, _onSuccess, _onError);
			};
			return {
				shareContent : shareContent,
				shareContentToSlack:shareContentToSlack
			};
		
		}
	]);
