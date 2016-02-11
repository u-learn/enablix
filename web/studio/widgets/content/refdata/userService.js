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
		
		var addUserData= function(userData)
		{
			userData.isPasswordSet=false;
			userData.identity=userData.userId;
			var sessionUser=JSON.parse(window.localStorage.getItem("userData"));
			userData.tenantId=sessionUser.tenantId;
				RESTService.postForData('systemuser', null, userData, null,function(data) {	    	
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
				RESTService.postForData('systemuser', null, sessionUser, null,function(data) {	    	
					Notification.primary({message: "Save successfully", delay: enablix.errorMsgShowTime});
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
		
		return {
			
			getAllUsers: getAllUsers,
			addUserData: addUserData,
			updatepassword :  updatepassword,
			checkUserName : checkUserName,
			deleteUser : deleteUser
			
		};
	}
]);	