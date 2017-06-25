enablix.studioApp.factory('NavigationTracker', 
	[
	 			'$rootScope',
	 	function($rootScope) {
	 		
	 		var navHistory = [];
	 		var recordingOn = true;
	 		
			$rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
				
				if (recordingOn) {
					navHistory.push({route: from, routeParams: fromParams});
				} else {
					recordingOn = true;
				}
			});
			
	 		var getPreviousState = function(removeFromHistory) {
	 			
	 			if (removeFromHistory) {
	 				return navHistory.pop();
	 			} else {
	 				var navLength = navHistory.length;
	 				return navLength > 0 ? navHistory[navHistory.length - 1] : null;
	 			}
	 			
	 		};
	 		
	 		var stopRecording = function() {
	 			recordingOn = false;
	 		};
	 		
	 		return {
	 			getPreviousState: getPreviousState,
	 			stopRecording: stopRecording
	 		};
	 	}
	 ]);


enablix.studioApp.factory('StateChangeCache', 
	[
	 			'CacheService', '$transitions',
	 	function(CacheService,   $transitions) {

	 		var STATE_CHANGE_CACHE_PREFIX = "SCCD_";
	 				
	 		var counter = 0;
	 		
			var valueSetFlags = {};
			var clearValueFlags = {};
			var currentKeys = {};
			
			function nextCacheKey(_key) {
				var k = STATE_CHANGE_CACHE_PREFIX + _key + counter++;
				currentKeys[_key] = k;
				return k;
			}
			
			function currentCacheKey(_key) {
				return currentKeys[_key];
			}
			
	 		var put = function(_key, _value) {
	 			var cKey = nextCacheKey(_key);
	 			CacheService.put(cKey, _value);
	 			valueSetFlags[cKey] = true;
	 		};
	 		
	 		var get = function(_key) {
	 			return CacheService.get(currentCacheKey(_key));
	 		};
	 		
	 		var clear = function() {
	 			angular.forEach(clearValueFlags, function(value, key) {
	 				if (value) {
	 					CacheService.remove(key);
	 					delete clearValueFlags[key];
	 				}
	 			});
	 		};
	 		
	 		var setForClearance = function() {
	 			angular.forEach(valueSetFlags, function(value, key) {
	 				if (value) {
	 					delete valueSetFlags[key];
	 					clearValueFlags[key] = true;
	 				}
	 			});
	 		}
	 		
	 		$transitions.onEnter({}, function(transition, state) {
				//console.log("onEnter: "); console.log(valueSetFlags); console.log(clearValueFlags);
				clear();
			});
			
			$transitions.onFinish({}, function(transition, state) {
				//console.log("onFinish: "); console.log(valueSetFlags); console.log(clearValueFlags);
				setForClearance();
			});
	 		
	 		return {
	 			put : put,
	 			get : get
	 		};
	 	}
	 ]);


enablix.studioApp.factory('ActivityTrackerContext', 
	[
	 			'StateChangeCache',
	 	function(StateChangeCache) {
	 		
	 		var ACTVY_CTX_KEY = "ACTVY_CTX";
	 		
	 		var setContextParams = function(_ctxParams) {
	 			StateChangeCache.put(ACTVY_CTX_KEY, _ctxParams);
	 		};
	 		
	 		var getContextParams = function() {
	 			return StateChangeCache.get(ACTVY_CTX_KEY);
	 		};
	 		
	 		return {
	 			setContextParams : setContextParams,
	 			getContextParams : getContextParams
	 		};
	 	}
	 ]);


enablix.studioApp.factory('NextStateUrlParams', 
	[
	 			'StateChangeCache', '$rootScope', '$location', '$transitions',
	 	function(StateChangeCache,   $rootScope,   $location,   $transitions) {
	 		
	 		var NS_URL_PARAMS_KEY = "NS_URL_PARAMS";
	 		
			$transitions.onSuccess({}, function(transition, state) {
				
				var urlParams = StateChangeCache.get(NS_URL_PARAMS_KEY);
				
				if (urlParams) {
					
					var currentParams = $location.search();
					angular.forEach(urlParams, function(value, key) {
						currentParams[key] = value;
					});
					
					$location.search(currentParams);
				}
			});
	 		
	 		var set = function(_urlParams) {
	 			StateChangeCache.put(NS_URL_PARAMS_KEY, _urlParams);
	 		};
	 		
	 		return {
	 			set : set
	 		};
	 	}
	 ]);
