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
					Notification.primary({message: "Password updated successfully", delay: enablix.errorMsgShowTime});
				StateUpdateService.goToPortalHome();
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
			getAllRoles: getAllRoles
			
		};
	}
]);	