enablix.studioApp.factory('CacheService', 
	 	function() {
		
			var cache = {};
		
			var logCache = function() {
				console.log("cache: "); console.log(cache);
			}
			
			var get = function(_cacheKey) {
				return cache[_cacheKey];
			};
			
			var put = function(_cacheKey, _cacheData) {
				cache[_cacheKey] = _cacheData;
			};
			
			var remove = function(_cacheKey) {
				delete cache[_cacheKey];
			};
			
			return {
				get: get,
				put: put,
				remove: remove
			};
	 	}
);