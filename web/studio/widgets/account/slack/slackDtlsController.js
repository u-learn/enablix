enablix.studioApp.controller('slackDtlsController', 
		['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService','$stateParams','Notification',
			function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService,$stateParams,Notification) {
			
			var code = $stateParams.code;
			var teamName = $stateParams.teamName;
			var error = $stateParams.error;
			var slackUserID = $stateParams.slackUserID;
			//Add a state check
			var initSlackDtls= function(){
				if( code != null ){
					getSlackAccessToken();
				}
				else if( teamName != null ){
					$scope.name=teamName;
					$scope.slackUserID=slackUserID;
				}
			}

			function getSlackAccessToken(){
				var params = {
						"code" : code
				};
				RESTService.postForData('getSlackAccessToken', params,null ,null, function(data) {
					$scope.name=data.teamName;
					$scope.slackUserID=data.slackUserID;
				}, function() {    		
					Notification.error({message: "Error in Configuring Slack", delay: enablix.errorMsgShowTime});
				},null,null);
			}
			$scope.unauthorizeSlack = function(){		
				RESTService.postForData('unauthSlackAcc', null,null ,null, function(data) {
					if(data) {
						StateUpdateService.goToSlackAuth("slackDtls");
					} else {
						Notification.error({message: "Oops! There was an error unauthorizing account on Slack.", delay: enablix.errorMsgShowTime});
					}
				}, function() {    		
					Notification.error({message: "Oops! There was an error unauthorizing account on Slack.", delay: enablix.errorMsgShowTime});
				},null,null);
			}
			if(error==null || error==undefined) {
				initSlackDtls();
			}
			else {
				StateUpdateService.goToSlackAuth("slackDtls");
			}
				
		}
		]);
