enablix.studioApp.factory('UserPreferenceService', 
			['RESTService', 'Notification','StateUpdateService',
 	function( RESTService,   Notification,  StateUpdateService) {
	
		var userPrefs = {};
		
		var loadApplicablePreferences = function() {
			
			return RESTService.getForData("fetchApplicableUserPrefs", null, null, 
				function(data) {
				
					angular.forEach(data, function(userPref) {
						userPrefs[userPref.key] = userPref;
					});
					
				}, 
				function(resp, status) {
					Notification.error({message: "Error loading preferences", delay: enablix.errorMsgShowTime});
				});
			
		};
		
		var getPrefByKey = function(_prefKey) {
			return userPrefs[_prefKey];
		}
	
		var saveOrUpdatePref = function(_url, _prefKey, _prefValue, _onSuccess, _onError) {
			
			var userPref = {
					key: _prefKey,
					config: _prefValue
			};
			
 			RESTService.postForData(_url, null, userPref, null, function(data) {
 				loadApplicablePreferences();
 				_onSuccess(data)
 			}, _onError);
 			
		};
		
		var saveAsUserPref = function(_prefKey, _prefValue, _onSuccess, _onError) {
			saveOrUpdatePref("saveAsUserPref", _prefKey, _prefValue, _onSuccess, _onError);
		};
		
		var saveAsSystemPref = function(_prefKey, _prefValue, _onSuccess, _onError) {
			saveOrUpdatePref("saveAsSystemPref", _prefKey, _prefValue, _onSuccess, _onError);
		};
		
		return {
			loadApplicablePreferences: loadApplicablePreferences,
			getPrefByKey: getPrefByKey,
			saveAsUserPref: saveAsUserPref,
			saveAsSystemPref: saveAsSystemPref
		};
	}
]);	