enablix.studioApp.factory('UserService', 
	[	        'RESTService', 'Notification','StateUpdateService',
	 	function(RESTService, Notification,StateUpdateService) {
		
		var getAllUsers= function(_success){
			RESTService.getForData('systemuser', null, null, function(data) {	    	
				_success(data);	    	
	    	}, function() {    		
	    		Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
	    	});
		};
		
		var getUserByIdentity = function(userIdentity, _success) {
			
			var params = {userIdentity: userIdentity};
			
			RESTService.getForData('fetchUser', params, null, function(data) {	    	
				_success(data);	    	
			}, function() {    		
				Notification.error({message: "Error fetching user data", delay: enablix.errorMsgShowTime});
			});
		};
		
		var addUserData= function(userData, roles)
		{
			if (isNullOrUndefined(userData.isPasswordSet)) {
				userData.isPasswordSet=false;
			}
			userData.identity=userData.userId;
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			userData.tenantId=sessionUser.tenantId;
			var userVO = { user: userData, roles: roles };
				RESTService.postForData('systemuser', null, userVO, null,function(data) {	    	
					Notification.primary({message: "Save successfully", delay: enablix.errorMsgShowTime});
				StateUpdateService.goToListUser();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToListUser();
	    	});
			
		};
		
		var updatepassword= function(_password)
		{		
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			sessionUser.password=_password;
			sessionUser.isPasswordSet=true;
			var userVO = { user: sessionUser };
				RESTService.postForData('systemuser', null, userVO, null,function(data) {	    	
					Notification.primary({message: "Password save successfully", delay: enablix.errorMsgShowTime});
				StateUpdateService.goToStudio();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    	});
			
		};
		var checkUserName=function(userName,_success)
		{			
				RESTService.getForData('checkusername', {"userName": userName}, null, function(data) {	    	
					_success(data);
				/*if(data)
				{
					Notification.error({message: "User Name already exits", delay: enablix.errorMsgShowTime});
				}else
				{
					Notification.primary({message: "User Name is valid", delay: enablix.errorMsgShowTime});
				}*/	
	    	}, function() {    		
	    		Notification.error({message: "Error while checking userName", delay: enablix.errorMsgShowTime});
	    	});
		};
		var deleteUser= function(userData,_success)
		{
			RESTService.postForData('deletesystemuser', null, userData, null,function(data) {
				 if(data){
					Notification.primary({message: "User deleted successfully", delay: enablix.errorMsgShowTime});
					_success(data);
				 } else {
					Notification.error({message: "Something went wrong while deleting user", delay: enablix.errorMsgShowTime});
					_success(data);
				 };	
				StateUpdateService.goToListUser();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToListUser();
	    	});
			
		};
		
		var getEmailConfig= function(tenantId,_success,_error)
		{
			var requestParam={"tenantId" : tenantId};
			RESTService.getForData('getemailconfiguration', requestParam, null, function(data) {	    	
				_success(data);	    	
    	}, function(errorObj) {    		
    		_error(errorObj)
    	});
		};
			
		var getSmtpConfig = function(domainName,_success,_error)
		{
			var requestParam={"domainName" : domainName};
			RESTService.getForData('getsmtpconfig', requestParam, null, function(data) {	    	
				_success(data);	    	
    	}, function(errorObj) {    		
    		_error(errorObj)
    	});
		};
		//var saveEmailData = function ()
		var saveEmailData= function(emailData)
		{			
			
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			emailData.tenantId=sessionUser.tenantId;
			emailData.identity=sessionUser.tenantId;
			RESTService.postForData('addemailconfiguration', null, emailData, null,function(data) {	    	
					Notification.primary({message: "Save successfully", delay: enablix.errorMsgShowTime});
				StateUpdateService.goToEmailConfig();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToEmailConfig();
	    	});
			
		};
		
		var deleteEmailData = function(emailData)
		{		
			RESTService.postForData('deleteemailconfiguration', null, emailData, null,function(data) {	    	
					Notification.primary({message: "Deleted successfully", delay: enablix.errorMsgShowTime});
				StateUpdateService.goToAddEmailConfig();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToEmailConfig();
	    	});
		}
		
		var getAllRoles = function(_success) {
			
			RESTService.getForData('fetchAllRoles', null, null, function(data) {	    	
				_success(data);	    	
			}, function() {    		
				Notification.error({message: "Error fetching roles", delay: enablix.errorMsgShowTime});
			});
		};
		
		return {
			
			getAllUsers: getAllUsers,
			getUserByIdentity: getUserByIdentity,
			addUserData: addUserData,
			updatepassword :  updatepassword,
			checkUserName : checkUserName,
			deleteUser : deleteUser,
			getEmailConfig  : getEmailConfig,
			getSmtpConfig : getSmtpConfig,
			saveEmailData : saveEmailData,
			deleteEmailData : deleteEmailData,
			getAllRoles: getAllRoles
			
		};
	}
]);	