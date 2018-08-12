var enablix = enablix || {};
enablix.appSelectorApp = angular.module("appSelector", []);

enablix.appSelectorApp.config([		
			'$httpProvider',
	function($httpProvider) {
		
		// The custom “X-Requested-With” is a conventional header sent by browser clients, 
		// and it used to be the default in Angular but they took it out in 1.3.0. 
		// Spring Security responds to it by not sending a “WWW-Authenticate” header in a 
		// 401 response, and thus the browser will not pop up an authentication dialog 
		// (which is desirable in our app since we want to control the authentication).
		
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	}
]);

enablix.appSelectorApp.controller('AppSelectController', 
			['$http', '$window', '$location',
	function( $http,   $window,   $location) {
		
		$http({
			method : 'GET',
			url : '/user'
			
		}).success(function(data) {
			
			console.log(data);
			var portal = data.principal ? data.principal.portal : "v1";

			var url = $window.location.href;
			console.log(url);
			$window.location.href = portal === "v2" ? url.replace('app.html', 'app2.html') : url.replace('app.html', 'app0.html');
			
		}).error(function(data, status) {
			
			var previousUrl = $window.location.href;
			
			$window.location.href = $location.protocol() + "://" + $location.host() + ":" + $location.port() 
				+ "/login.html#/login#" +"redirect#" + encodeURIComponent(previousUrl);
		});
	}
]);
