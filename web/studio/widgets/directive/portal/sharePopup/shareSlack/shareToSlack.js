enablix.studioApp.controller('ShareToSlackController', [
	'$scope', '$stateParams', '$modalInstance', '$timeout', 'containerQId', 'contentIdentity', 'ContentShareService', 'Notification','RESTService',
	function( $scope,   $stateParams,   $modalInstance,   $timeout,   containerQId,   contentIdentity,   ContentShareService,   Notification,RESTService) {
		$scope.channelsSelectd=[];
		$scope.channelLst=[];
		$scope.slackCustomContent="";
		getChannels(populateChannels);
		$scope.channelBoundedRefItemDef = {
				"id" : "playUserSet",
				"bounded" : {
	                "fixedList" : {
	                    "data" :$scope.channelLst
	                }, 
	                "multivalued" : true,
				},
                "type" : "BOUNDED", 
                "label" : "Select Channel", 
            }; 
		
		function getChannels(_success){
			RESTService.getForDataSync('getSlackChannels', null, null, function(data) {
				_success(data);	    	
			}, function() {    		
				Notification.error({message: "Error loading channels ", delay: enablix.errorMsgShowTime});
			});
		};
		
		function populateChannels(data){
			var channelsDtls = data.channels;
			if(channelsDtls!=null && channelsDtls!=undefined){
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
		}
		function validateChannel() {
			if($scope.channelsSelectd==null || $scope.channelsSelectd==undefined || $scope.channelsSelectd.length==0) {
				return false;
			}
			else {
				return true;	
			}
		}
		$scope.shareContent = function() {
			var valResp = validateChannel();
			if(valResp) {
				ContentShareService.shareContentToSlack(containerQId, contentIdentity, $scope.channelsSelectd,$scope.slackCustomContent,
						function(sent) {
					if(sent) {
						Notification.primary({message: "Done! Shared successfully on Slack.", delay: enablix.errorMsgShowTime});
					} else {
						Notification.error({message: "Oops! There was an error sharing on Slack.", delay: enablix.errorMsgShowTime});
					}
				}, function(fail) {
					Notification.error({message: "Oops! There was an error sharing on Slack.", delay: enablix.errorMsgShowTime});
				});
				$modalInstance.close();
			}
			else{
				Notification.error({message: "Please select a Channel.", delay: 3000});
			}
		}

		$scope.close = function() {
			$modalInstance.dismiss('cancel');
		}
		
	}]);
