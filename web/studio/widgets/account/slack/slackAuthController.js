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
				var hostAddr = $location.host();
				var redirectURI = "redirect_uri=" + $location.protocol() + "://" + hostAddr 
									+ (hostAddr === 'localhost' ? ":" + $location.port() : "")
									+ "/app.html%23/account/slackdtls";
				
				var url = enablix.serviceURL["getSlackCode"];
				
				RESTService.getForData('getSlackAppDtls', null, null, function(data) {
					if( data != null && data!="" ){
						window.location.href = url + data.clientID + "&" +redirectURI;
					}
				}, function() {    		
					Notification.error({message: "Oops! There was an error on Slack.", delay: enablix.errorMsgShowTime});
				},null);
			}
			if(source==null)
				checkIfAlreadyAuth();
		}
		]);
