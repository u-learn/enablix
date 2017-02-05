enablix.studioApp.controller('ShareToSlackController', [
	'$scope', '$stateParams', '$modalInstance', '$timeout', 'containerQId', 'contentIdentity', 'ContentShareService', 'Notification','RESTService','contentName','portalURL',
	function( $scope,   $stateParams,   $modalInstance,   $timeout,   containerQId,   contentIdentity,   ContentShareService,   Notification,RESTService,contentName,portalURL) {
		
		var userData=JSON.parse(window.localStorage.getItem("userData"));
		$scope.channelLst=[];

		function getChannels(_success){
			var params = {
					userID : userData.userId
			}
			RESTService.getForData('getSlackChannels', params, null, function(data) {
				_success(data);	    	
			}, function() {    		
				Notification.error({message: "Error loading channels ", delay: enablix.errorMsgShowTime});
			});
		};
		
		function populateChannels(data){
			var channelsDtls = data.channels;
			var size = channelsDtls.length;
			for(var i=0; i<size;i++)
			{
				var channelObj= {
						label: channelsDtls[i].name,
						id: channelsDtls[i].id
				};
				$scope.channelLst.push(channelObj);
			}
		}

		$scope.shareContent = function() {
			ContentShareService.shareContentToSlack(userData.userId,containerQId, contentName,contentIdentity, portalURL ,$scope.channelID,function(sent) {
				if(sent) {
					Notification.primary({message: "Content Shared successfully", delay: enablix.errorMsgShowTime});
				} else {
					Notification.error({message: "Error sending content to Slack ", delay: enablix.errorMsgShowTime});
				}
			}, function(fail) {
				Notification.error({message: "Error sending content to Slack ", delay: enablix.errorMsgShowTime});
			});
			$modalInstance.close();
		}

		$scope.close = function() {
			$modalInstance.dismiss('cancel');
		}
		getChannels(populateChannels);
	}]);
