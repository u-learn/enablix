enablix.studioApp.controller('slackAuthController', 
		['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService','$stateParams','$location',
			function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService,$stateParams,$location) {

			var source = $stateParams.source;
			var checkIfAlreadyAuth= function(){
				
				RESTService.getForData('getSlackStoredAuthAccessToken', null, null, function(data) {
					if( data != null && data.accessToken!=null && data.teamName!=null ){
						StateUpdateService.goToSlackDtls(data.teamName,data.slackUserID);
					}
				}, function() {    		
					Notification.error({message: "Oops! There was an error on Slack.", delay: enablix.errorMsgShowTime});
				},null);
			}

			$scope.authorizeSlack = function(){
				var redirectURI="redirect_uri="+$location.protocol() + "://" + $location.host() 
				+ "/app.html%23/account/slackdtls";
				var url = enablix.serviceURL["getSlackCode"];
				window.location.href=url +"&"+redirectURI;
			}
			if(source==null)
				checkIfAlreadyAuth();
		}
		]);
