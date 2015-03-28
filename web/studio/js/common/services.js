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
		'$http', '$window',
		function($http, $window) {	
			
			var postForFile = function(_resourceKey, _params, files,_success, _error) {
				
				 var fd = new FormData();
		            //Take the first selected file
		            fd.append("file", files[0]);
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
			
			var getForData = function(_resourceKey, _params, transformer, _success, _error) {
				
				var requestConfig = genereateRequestConfig(_resourceKey, _params);
				
				$http({
					method : 'GET',
					url : requestConfig.url,
					params : requestConfig.paramsJson
					
				}).success(function(data) {
					
					if (transformer != undefined) {
						_success(transformer(data));
					} else {
						_success(data);
					}
					
				}).error(function(data) {
					_error(data);	
				});
			};

			var postForData = function(_resourceKey, _params, _data,
					transformer, _success, _error,_contentType) {
				
				if (_contentType != null && _contentType != undefined && _contentType != '') {
					$http.defaults.headers.post['Content-Type'] = _contentType;
					
				} else {
					$http.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
				}
				
				var requestConfig = genereateRequestConfig(_resourceKey, _params);
				
				$http({
					method : 'POST',
					url : requestConfig.url,
					params : requestConfig.paramsJson,
					data : _data
					
				}).success(function(data) {
					
					if (transformer != undefined) {
						_success(transformer(data));
					} else {
						_success(data);
					}
					
				}).error(function(data) {
					_error(data);	
				});

			};
						
			// rest interface
			return {
				getForData : getForData,
				postForData : postForData,
				postForFile : postForFile
			};
		}]);

