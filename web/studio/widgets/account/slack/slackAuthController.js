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
				
				var url = enablix.serviceURL["getSlackCode"];
				
				RESTService.getForData('getSlackAppDtls', null, null, function(data) {
					if( data != null && data!="" ){
						
						var hostAddr = $location.host();
						
						var subdomain = "www";
						var dotIndx = hostAddr.indexOf('.');
						if (dotIndx > 0) {
							subdomain = hostAddr.substr(0, dotIndx);
						}
						
						var redirectURI = "redirect_uri=" + data.redirectDomain + "/app.html";
						window.location.href = (url + data.clientID + "&" + redirectURI + "&state=" + subdomain);
					}
				}, function() {    		
					Notification.error({message: "Oops! There was an error on Slack.", delay: enablix.errorMsgShowTime});
				},null);
			}
			if(source==null)
				checkIfAlreadyAuth();
		}
		]);
