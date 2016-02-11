enablix.studioApp.controller('DocStoreConfigController', 
			['$scope', '$state', '$stateParams', 'ConfigurationService', 'DocStoreConfigService', 'RESTService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   ConfigurationService,   DocStoreConfigService,   RESTService,   StateUpdateService,   Notification) {
	
		var DOCUMENT_STORE_CONFIG_KEY_PREFIX = "docstore.";
		
		$scope.breadcrumbList = 
		[
	         { label: "Setup" },
	         { label: "Document Store" }
		];
		
		$scope.docStoreConfigMetadata = [];
		$scope.docStoreTypes = [];
		$scope.selectDocStoreTypeMd = {};
		$scope.docstore = {};
		$scope.docstoreBackup = {};
		
		$scope.docstoreConfigured = false;
		$scope.addOrEditAction = false;
		
		$scope.changeDocStoreType = function(selectDocStoreType) {
			for (var i = 0; i < $scope.docStoreConfigMetadata.length; i++) {
				var tempDocStoreTypeMd = $scope.docStoreConfigMetadata[i];
				if (tempDocStoreTypeMd.storeTypeCode == selectDocStoreType) {
					$scope.selectDocStoreTypeMd = tempDocStoreTypeMd;
					$scope.docstore = {
							STORE_TYPE : tempDocStoreTypeMd.storeTypeCode
					};
					break;
				}
			}
		}
		
		DocStoreConfigService.getDocStoresConfigMetadata(function(data) {
				
				$scope.docStoreConfigMetadata = data;
				
				angular.forEach(data, function(docStoreConfigMd) {
					$scope.docStoreTypes.push({
						label: docStoreConfigMd.storeTypeName,
						id: docStoreConfigMd.storeTypeCode
					});
				});

				if (!isNullOrUndefined(data) && data.length > 0) {
					$scope.selectDocStoreTypeMd = data[0];
					$scope.changeDocStoreType($scope.selectDocStoreTypeMd.storeTypeCode);
				}
				
				DocStoreConfigService.getDefaultDocStoreConfig(function(data) {
					
					if (!isNullOrUndefined(data) && data != "") {
						
						$scope.docstore = data.config;
						$scope.docstoreConfigured = true;
						
						angular.copy(data.config, $scope.docstoreBackup);
						
						updateSelectedDocStoreTypeMetadata(data.config);
						
					} else {
						$scope.updateAddOrEditAction(true);
					}
				});
				
			}, function(data) {
				Notification.error({message: "Error loading document store configuration data", delay: enablix.errorMsgShowTime});
			});
		
		var updateSelectedDocStoreTypeMetadata = function(_docstoreConfig) {
			for (var i = 0; i < $scope.docStoreConfigMetadata.length; i++) {
				
				var tempDocStoreTypeMd = $scope.docStoreConfigMetadata[i];
				
				if (tempDocStoreTypeMd.storeTypeCode == _docstoreConfig.STORE_TYPE) {
					$scope.selectDocStoreTypeMd = tempDocStoreTypeMd;
					break;
				}
			}
		};
		
		
		$scope.saveDocStoreConfig = function() {
			
			var docStoreConfigKey = DOCUMENT_STORE_CONFIG_KEY_PREFIX + $scope.docstore.STORE_TYPE;
			var docStoreConfiguration = {
				key : docStoreConfigKey
			};
			
			docStoreConfiguration.config = $scope.docstore;
			
			DocStoreConfigService.saveDocStoreConfig(docStoreConfiguration, 
				function() {
					
					angular.copy($scope.docstore, $scope.docstoreBackup);
					$scope.updateAddOrEditAction(false);
					$scope.docstoreConfigured = true;
					
					Notification.primary("Saved successfully!");
					
				}, function() {
					Notification.error({message: "Error saving document store configuration", delay: enablix.errorMsgShowTime});
				});
		};
		
		$scope.updateAddOrEditAction = function(isAddOrEdit) {
			$scope.addOrEditAction = isAddOrEdit;
		};
		
		$scope.cancelOperation = function() {
			angular.copy($scope.docstoreBackup, $scope.docstore);
			$scope.updateAddOrEditAction(false);
			updateSelectedDocStoreTypeMetadata($scope.docstore);
		};
	}
]);			
