var genereateRequestConfig = function(_resourceKey, _params) {
	
	var paramsJson = {};
	var url = enablix.serviceURL[_resourceKey];

	angular.forEach(_params, function(value, key) {
	
		if (url.search(":" + key) != -1) {
			url = url.replace(":" + key, value);
			
		} else {
			paramsJson[key] = value;
		}
	});
	
	var returnJson = {
		"url" : url,
		"paramsJson" : paramsJson
	};
	
	return returnJson;
};

enablix.studioApp.factory('RESTService', [
		'$http', '$rootScope', '$window', 'InfoModalWindow', 'StateUpdateService', 'ResourceVersionHolder',
		function($http, $rootScope, $window, InfoModalWindow, StateUpdateService, ResourceVersionHolder) {	
			
			var postForFile = function(_resourceKey, _params, files, _data, _success, _error, _headers) {
				
				var fd = new FormData();
	            
				//Take the first selected file
	            fd.append("file", files[0]);
				
	            angular.forEach(_data, function(key, value) {
	            	fd.append(key, value);
	            });
	            
	            var requestConfig = genereateRequestConfig(_resourceKey, _params);
					
				 $http.post(requestConfig.url, fd, {
		                headers: {'Content-Type': undefined },
		                transformRequest: angular.identity
		                
		            }).success( function(data) {
		            	_success(data);
		            	
					}).error( function(data) {
		            	_error(data);
		            });
			}
			
			
			var addResourceVersionHeaders = function(_headers) {
				
				var callHeaders = {};
				if (!isNullOrUndefined(_headers)) {
					callHeaders = _headers;
				}
				
				var versionHeaders = ResourceVersionHolder.getResourceVersionHeaders();
				
				if (!isNullOrUndefined(versionHeaders)) {
					angular.forEach(versionHeaders, function(value, key) {
						callHeaders[key] = value;
					});
				}
				
				return callHeaders;
			};
			
			var getForData = function(_resourceKey, _params, transformer, _success, _error, _headers) {
				
				var requestConfig = genereateRequestConfig(_resourceKey, _params);
				
				var callHeaders = addResourceVersionHeaders(_headers);
				
				return $http({
							method : 'GET',
							url : requestConfig.url,
							params : requestConfig.paramsJson,
							headers : callHeaders
							
						}).success(function(data) {
							
							if (transformer != undefined) {
								_success(transformer(data));
							} else {
								_success(data);
							}
							
						}).error(function(data, status) {
							
							if (isVersionMismatchError(data, status)) {
								return;
							}
							
							checkAuthenticationErrorAndExecute(data, status, _error);
						});
			};
			
			var isVersionMismatchError = function(data, status) {
				
				if (status == 418) {
					console.log("Version mis-match error");
					
					var modalInstance = InfoModalWindow.showInfoWindow(
							"Application Update", 
							"Newer version of the application is available. Application will be reloaded.");
					
					modalInstance.result.then(function() {
						window.location.reload();
					});
					
					return true;
				}
				
				return false;
			}
			
			var checkAuthenticationErrorAndExecute = function(data, status, _error) {
				
				if (status && status == 401) {
					
					// authentication error
					if (!$rootScope.loginProcess) {
						$rootScope.authenticated = false;
						StateUpdateService.goToLogin(window.location.href);
					} else {
						if (_error) { 
							_error(data, status);
						}
					}
					
				} else {
					if (_error) {
						_error(data, status);
					}
				}
			};

			var postForData = function(_resourceKey, _params, _data,
					transformer, _success, _error, _contentType, _headers) {
				
				if (_contentType != null && _contentType != undefined && _contentType != '') {
					$http.defaults.headers.post['Content-Type'] = _contentType;
					
				} else {
					$http.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
				}
				
				var callHeaders = addResourceVersionHeaders(_headers);
				
				var requestConfig = genereateRequestConfig(_resourceKey, _params);
				
				$http({
					method : 'POST',
					url : requestConfig.url,
					params : requestConfig.paramsJson,
					data : _data,
					headers : callHeaders
					
				}).success(function(data) {
					
					if (transformer != undefined) {
						_success(transformer(data));
					} else {
						_success(data);
					}
					
				}).error(function(data, status) {
					
					if (isVersionMismatchError(data, status)) {
						return;
					}
					
					checkAuthenticationErrorAndExecute(data, status, _error);	
				});

			};
						
			// rest interface
			return {
				getForData : getForData,
				postForData : postForData,
				postForFile : postForFile
			};
		}]);

