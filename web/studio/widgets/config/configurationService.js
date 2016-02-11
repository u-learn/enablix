enablix.studioApp.factory('ConfigurationService', 
	[			'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
		
			var getConfigByKey = function(_configKey, _onSuccess) {
				
				var params = {configKey: _configKey};
				
				RESTService.getForData("fetchConfigByKey", params, null, _onSuccess, function() {
					Notification.error({message: "Error loading configuration data", delay: enablix.errorMsgShowTime});
				});
				
			};
			
			var saveConfiguration = function(_configData, _onSuccess, _onError) {
				
				RESTService.postForData("saveConfig", null, _configData, null, _onSuccess, _onError);
				
			};
			
			return {
				getConfigByKey: getConfigByKey,
				saveConfiguration: saveConfiguration
			};
		
		}
	]);