enablix.studioApp.controller('ShareController', ['$scope', '$stateParams', '$modalInstance', 'bodyData', 'singleHeaders', 'multiHeaders','UserService','counter',
	function( $scope,   $stateParams,   $modalInstance, bodyData, singleHeaders, multiHeaders,UserService,counter){

$scope.shareContent = function(){
UserService.sendMail({"bodyData": bodyData, "singleHeaders": singleHeaders, "multiHeaders": multiHeaders, "counter": counter},$scope.emailid,"shareContent",true);
}

		$scope.close = function() {
			$modalInstance.close();
		}

}]);