enablix.studioApp.factory('DocStoreConfigService', 
	[			'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
		
			var getDocStoresConfigMetadata = function(_onSuccess, _onError) {
				_onSuccess(enablix.docstore.metadata);
			};
			
			return {
				getDocStoresConfigMetadata: getDocStoresConfigMetadata
			};
		
		}
	]);