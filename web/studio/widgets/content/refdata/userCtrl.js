enablix.studioApp.controller('UserController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService', 'ConfirmationModalWindow',
	function( $scope,   $state,   RESTService,  UserService,   $rootScope,   StateUpdateService, ConfirmationModalWindow) {
		 
		$scope.breadcrumbList = 
		[
	         { label: "Setup" },
	         { label: "User Management" }
		];
				
		 function init() {
			 UserService.getAllUsers(function(data) {	
				 $scope.userData=data;				 
			 });
		 };
		 
		 $scope.addUser = function(){
			 
			 StateUpdateService.goAddUser();
		 }
		 
		 $scope.userRecordView=function(identity) {
			 StateUpdateService.goViewUser(identity);
		 }	 
		 
		 $scope.userRecordEdit=function(identity) {
			 StateUpdateService.goEditUser(identity);
		 }	 
		 
		 $scope.deleteUserRecord=function(identity) {
			 
			 var user = $scope.userData.filter(function(obj){ 
				 if(obj.identity===identity) 
					 return obj;
			 });
			 
			var userTodelete=user[0]; 
			var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
					"Do you want to continue?", 
					"Proceed", "Cancel");

			confirmModal.result.then(function(confirmed) {

				if (confirmed) {
					UserService.deleteUser(userTodelete,function(data){
						init();
					});
				}
			});			 
		 }
		 
		 init();
	}
]);			
