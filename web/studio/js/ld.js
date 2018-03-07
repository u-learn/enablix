var enablix = enablix || {};
enablix.siteApp = angular.module("site", ['ui.router', 'ngMessages']);

enablix.siteApp.config(
[		'$stateProvider', '$urlRouterProvider', '$httpProvider',
function($stateProvider,   $urlRouterProvider,   $httpProvider) {
	
	$stateProvider
		.state('thankyou', {
			url: '/thankyou',
			templateUrl: 'views/site/thankyou.html'
		})
		.state('signup', {
			url: '/signup',
			templateUrl: 'views/site/signup.html'
		});
	
}]);

enablix.siteApp.directive('ebxValidationError', 
	function () {
		return {
			require: 'ngModel',
			link: function (scope, elm, attrs, ctl) {
				scope.$watch(attrs['ebxValidationError'], function (errorMsg) {
					elm[0].setCustomValidity(errorMsg);
					ctl.$setValidity('ebxValidationError', errorMsg ? false : true);
				});
			}
		};
});


enablix.siteApp.controller('InviteRequestController', 
		['$scope', '$state', '$http', '$window', '$location',
function( $scope,   $state,   $http,   $window,   $location) {
	
	$scope.contactUsForm = {
			email: ""
		};
	
	$scope.error = null;
			
	
	$scope.submitContactUs = function() {

		if ($scope.modalForm.$valid 
				&& $scope.contactUsForm.email
				&& $scope.contactUsForm.email.trim() != "") {
			
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
	
	$scope.initSignup = function() {
		$window.location.href = $location.protocol() + "://" + $location.host() 
				+ ":" + $location.port() + "/register.html";
	}
	
}
]);

enablix.siteApp.controller('SignupController', 
		['$scope', '$state', '$http', '$window', '$location',
function( $scope,   $state,   $http,   $window,   $location) {
	
	$scope.signupForm = { };
	
	$scope.error = null;
	$scope.processing = false;
			
	
	$scope.signup = function() {

		$scope.error = null;
		
		if ($scope.modalForm.$valid) {
			
			$scope.processing = true;
			var callHeaders = {'Content-Type' : 'application/json; charset=utf-8'};
			
			$http({
				method : 'POST',
				url : "/signup",
				data : $scope.signupForm,
				headers : callHeaders
				
			}).success(function(data) {
				
				$window.location.href = $location.protocol() + "://" + $location.host() 
					+ ":" + $location.port() + "/app.html#/portal/home?ns=1";
				
			}).error(function(err, status) {
				
				$scope.processing = false;
				
				if (err.error && err.error.errorCode == "validation_error") {
				
					$scope.error = "";
					
					angular.forEach(err.errors, function(sysError) {
						if (sysError.errorCode == "user_exist") {
							$scope.error += "User account already exists for the given email.\n";
						} else {
							$scope.error += (sysError.message + "\n");
						}
					});
					
				} else {
					$scope.error = "Unable to signup. Please try again later.";
				}
			});
			
		}
	}
	
}
]);

