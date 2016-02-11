enablix.studioApp.factory('DocStoreConfigService', 
	[			'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
		
			var getDocStoresConfigMetadata = function(_onSuccess, _onError) {
				_onSuccess(enablix.docstore.metadata);
			};
			
			var saveDocStoreConfig = function(_configData, _onSuccess, _onError) {
				
				RESTService.postForData("saveDocStoreConfig", null, _configData, null, _onSuccess, _onError);
				
			};
			
			var getDefaultDocStoreConfig = function(_onSuccess) {
				RESTService.getForData("defaultDocStoreConfig", null, null, _onSuccess, function() {
					Notification.error({message: "Error loading default document store data", delay: enablix.errorMsgShowTime});
				});
			}
			
			return {
				getDocStoresConfigMetadata: getDocStoresConfigMetadata,
				saveDocStoreConfig: saveDocStoreConfig,
				getDefaultDocStoreConfig: getDefaultDocStoreConfig
			};
		
		}
	]);