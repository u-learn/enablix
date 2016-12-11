enablix.studioApp.factory('CacheService', 
	[	        'RESTService',
	 	function(RESTService) {
		
			var cache = {};
		
			var get = function(_cacheKey) {
				return cache[_cacheKey];
			};
			
			var put = function(_cacheKey, _cacheData) {
				cache[_cacheKey] = _cacheData;
			};
			
			var remove = function(_cacheKey) {
				cache[_cacheKey] = undefined;
			};
			
			return {
				get: get,
				put: put,
				remove: remove
			};
	 	}
	]);