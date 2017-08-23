enablix.studioApp.factory('UserService', 
	[	        'RESTService', 'Notification', 'StateUpdateService', 'DataSearchService',
	 	function(RESTService,   Notification,   StateUpdateService,   DataSearchService) {

		var USER_PROFILE_DOMAIN = "com.enablix.core.domain.security.authorization.UserProfile";
		
		var filterMetadata = {
			"userRoleIn" : {
				"field" : "systemProfile.roles.$id",
				"operator" : "IN",
				"dataType" : "STRING"
			}	
		};
		
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
		
		var addUserData= function(user, userProfile)
		{
			 userProfile.email =  userProfile.email.toLowerCase();
			if (isNullOrUndefined(user.isPasswordSet)) {
				user.isPasswordSet=false;
			}
			user.identity = userProfile.email;
			userProfile.identity = userProfile.email;
			user.userId = userProfile.email ;
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			user.tenantId=sessionUser.tenantId;
			var userVO = { user: user, userProfile: userProfile};	
			
			RESTService.postForData('systemuser', null, userVO, null,function(data) {	    	
					Notification.primary({message: "User Saved successfully", delay: enablix.errorMsgShowTime});
					StateUpdateService.goToListUser();				
	    	}, function() {    		
	    		Notification.error({message: "Error saving user ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToListUser();
	    	});
			
		};
		
		var editUserData= function(user, userProfile)
		{
			userProfile.email =  userProfile.email.toLowerCase();
			if (isNullOrUndefined(user.isPasswordSet)) {
				user.isPasswordSet=false;
			}
			user.identity = userProfile.email;
			userProfile.identity = userProfile.email;
			user.userId = userProfile.email ;
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			user.tenantId=sessionUser.tenantId;
			var userVO = { user: user, userProfile: userProfile};	
			
			RESTService.postForData('systemuseredit', null, userVO, null,function(data) {	    	
					Notification.primary({message: "User Saved successfully", delay: enablix.errorMsgShowTime});
					StateUpdateService.goToListUser();				
	    	}, function() {    		
	    		Notification.error({message: "Error saving user ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToListUser();
	    	});
			
		};
		
		var updatepassword= function( id, _password)
		{		

			var userData = { id : id, password :_password, isPasswordSet: true };
			RESTService.postForData('systemuserchangepwd', null, userData, null,function(data) {	    	
					Notification.primary({message: "Password saved successfully", delay: enablix.errorMsgShowTime});
					//sendMail(sessionUser,sessionUser.userId,"passwordconfirmation", false);
					StateUpdateService.goToPortalHome();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    	});
			
		};
		
		var sendMail = function(templateObject, emailid, scenario, notifyUser){
			
			var emailData = {scenario:scenario,emailid:emailid,templateObject:templateObject};
			
			RESTService.postForData('sendmail',null,emailData, null,function(sent) {
				
					if(notifyUser) { 
						if(sent) {
							Notification.primary({message: "Mail sent successfully", delay: enablix.errorMsgShowTime});
						} else {
							Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
						}
					}
					
				}, function(errorObj) {    		
						if(notifyUser) {
							Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
						}
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
	    		//Notification.error({message: "Error while checking userName", delay: enablix.errorMsgShowTime});
	    	});
		};
		var deleteUser= function(identity,_success)
		{
			RESTService.postForData('deletesystemuser', null, identity, null,function(data) {
				 if(data) {
					Notification.primary({message: "User deleted successfully", delay: enablix.errorMsgShowTime});
					_success(data);
				 } else {
					Notification.error({message: "Something went wrong while deleting user", delay: enablix.errorMsgShowTime});
					_success(data);
				 };	
				//StateUpdateService.goToListUser();
	    	}, function() {    		
	    		Notification.error({message: "Error ", delay: enablix.errorMsgShowTime});
	    		StateUpdateService.goToListUser();
	    	});
			
		};
		
		var getEmailConfig= function(tenantId,_success,_error)
		{
			RESTService.getForData('getemailconfiguration', null, null, function(data) {	    	
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
		
		var saveEmailData= function(emailData)
		{			
			
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			emailData.tenantId=sessionUser.tenantId;
			RESTService.postForData('addemailconfiguration', null, emailData, null,function(data) {	    	
					Notification.primary({message: "Saved successfully", delay: enablix.errorMsgShowTime});
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
		
		var searchTenantUsers = function(_dataFilters, _pagination, _onSuccess, _onError) {

			DataSearchService.getSearchResult(USER_PROFILE_DOMAIN, _dataFilters, _pagination, 
						filterMetadata, _onSuccess, _onError)
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
			getAllRoles: getAllRoles,
			sendMail:sendMail,
			editUserData:editUserData,
			searchTenantUsers: searchTenantUsers 
		};
	}
]);	