enablix.studioApp.controller('slackDtlsController', 
		['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService','$stateParams','Notification',
			function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService,$stateParams,Notification) {
			
			var userData=JSON.parse(window.localStorage.getItem("userData"));
			
			var code = $stateParams.code;
			var teamName = $stateParams.teamName;
			//Add a state check
			var initSlackDtls= function(){
				if( code != null ){
					getSlackAccessToken();
				}
				else if( teamName != null ){
					$scope.name=teamName;
				}
			}

			function getSlackAccessToken(){
				var params = {
						"code" : code,
						"userID" : userData.userId
				};
				RESTService.postForData('getSlackAccessToken', params,null ,null, function(data) {
					$scope.name=data.teamName;
				}, function() {    		
					Notification.error({message: "Error in Configuring Slack", delay: enablix.errorMsgShowTime});
				},null,null);
			}
			$scope.unauthorizeSlack = function(){
				var params = {
					"userID" : userData.userId
				};
			
				RESTService.postForData('unauthSlackAcc', params,null ,null, function(data) {
					StateUpdateService.goToSlackAuth("slackDtls");
				}, function() {    		
					Notification.error({message: "Error in Configuring Slack", delay: enablix.errorMsgShowTime});
				},null,null);
			}
			initSlackDtls();
		}
		]);