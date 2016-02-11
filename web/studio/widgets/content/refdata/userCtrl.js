enablix.studioApp.controller('UserController', 
			['$scope', '$state', 'RESTService','UserService', '$rootScope', 'StateUpdateService',
	function( $scope,   $state,   RESTService,  UserService, $rootScope,   StateUpdateService) {
		 
		 function init(){
		 UserService.getAllUsers(function(data){	
			 $scope.userData=data;				 
		 });
		 };
		 $scope.addUser=function(){
			 
			 StateUpdateService.goAddUser();
		 }
		 
		 $scope.userRecordEdit=function(identity){
			 
			 StateUpdateService.goEditUser(identity);
			 	
		 }	 
		 $scope.deleteUserRecord=function(identity)
		 {
			 var user=$scope.userData.filter(function(obj){ 
				 if(obj.identity===identity) 
					 return obj;
			 });
			 var userTodelete=user[0]; 
			 UserService.deleteUser(userTodelete,function(data){
				 init();
			 });			 
		 }
		 init();
	}
]);			