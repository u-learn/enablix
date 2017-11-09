var enablix = enablix || {};
enablix.siteApp = angular.module("site", ['ui.router', 'ngMessages']);

enablix.siteApp.config(
[		'$stateProvider', '$urlRouterProvider', '$httpProvider',
function($stateProvider,   $urlRouterProvider,   $httpProvider) {
	
	$stateProvider
		.state('thankyou', {
			url: '/thankyou',
			templateUrl: 'views/site/thankyou.html'
		});
	
}]);


enablix.siteApp.controller('InviteRequestController', 
		['$scope', '$state', '$http', '$window', '$location',
function( $scope,   $state,   $http,   $window,   $location) {
	
	$scope.contactUsForm = {
			email: ""
		};
	
	$scope.error = null;
			
	
	$scope.submitContactUs = function() {

		if ($scope.modalForm.$valid) {
			
			var callHeaders = {'Content-Type' : 'application/json; charset=utf-8'};
			
			$http({
				method : 'POST',
				url : "/site/contactusnc",
				data : $scope.contactUsForm,
				headers : callHeaders
				
			}).success(function(data) {
				
				$window.location.href = $location.protocol() + "://" + $location.host() 
					+ ":" + $location.port() + "/thankyou.html";
				
			}).error(function(data, status) {
				$scope.error = "Unable to register your request. Please try later."	
			});
			
		} 
	}
	
}
]);
