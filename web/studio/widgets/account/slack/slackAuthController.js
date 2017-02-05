enablix.studioApp.controller('slackAuthController', 
		['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService','$stateParams',
			function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService,$stateParams) {

			var userData=JSON.parse(window.localStorage.getItem("userData"));
			var source = $stateParams.source;
			var checkIfAlreadyAuth= function(){
				var _data = {
						"userID" : userData.userId
				};
				RESTService.getForData('getSlackStoredAuthAccessToken', _data, null, function(data) {
					if( data != null && data.accessToken!=null && data.teamName!=null ){
						StateUpdateService.goToSlackDtls(data.teamName);
					}
				}, function() {    		
					Notification.error({message: "Error in Configuring Slack", delay: enablix.errorMsgShowTime});
				},null);
			}

			$scope.authorizeSlack = function(){
				var url = enablix.serviceURL["getSlackCode"];
				window.location.href=url ;
			}
			if(source==null)
				checkIfAlreadyAuth();
		}
		]);