enablix.studioApp.controller('EmailController', ['$scope', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'StateUpdateService',
    function($scope, $stateParams, RESTService, UserService, $rootScope, StateUpdateService) {
        var userData=JSON.parse(window.localStorage.getItem("userData")); 
        $scope.newEmailData={};
		UserService.getEmailConfig(userData.tenantId,function(emailData) {
	        if(emailData)
	         {
	        	$scope.emailData=emailData;
	        	$scope.newEmailData=$scope.emailData;
	         }else
	         {
	        	 StateUpdateService.goToAddEmailConfig();
	         }	 
	    });
		$scope.getSmtp=function(emailId)
		{
			if(emailId)
				var splited=emailId.split("@");
			    var domain="@"+splited[1];
			UserService.getSmtpConfig(domain,function(smtpData){
				console.log(smtpData);
			});
		};
		$scope.saveEmailData=function()
		{
			UserService.saveEmailData($scope.newEmailData);
		}
		$scope.emailRecordEdit=function()
		{
			StateUpdateService.goToEditEmailConfig();	
		}
		$scope.deleteEmailRecord =function(emailData)
		{
			UserService.deleteEmailData(emailData);
		}
		$scope.cancelOperation =function(flag)
		{	
			if(flag)
				StateUpdateService.goToEmailConfig();						
		};
    }
]);