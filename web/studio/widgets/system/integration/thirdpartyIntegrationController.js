enablix.tpIntegrationConfigMetadata = [
	{
		tpKey: "integration.wordpress",
		tpName: "Wordpress Integration",
		params: [
			{
				paramKey: "BASE_URL",
				paramName: "Base Url"
			}
		]
	}
];

var findIntegrationConfigMetadata = function(_tpKey) {
	
	for (var i = 0; i < enablix.tpIntegrationConfigMetadata.length; i++) {
	
		var tempIntTypeMd = enablix.tpIntegrationConfigMetadata[i];
		
		if (tempIntTypeMd.tpKey == _tpKey) {
			return tempIntTypeMd;
		}
	}
	
	return null
};

enablix.studioApp.controller('ThirdPartyIntBaseController', 
			['$scope', '$state', '$stateParams', 'ConfigurationService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   ConfigurationService,   StateUpdateService,   Notification) {
			
	}
]);

enablix.studioApp.controller('ThirdPartyIntegrationController', 
			['$scope', '$state', '$stateParams', 'ConfigurationService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   ConfigurationService,   StateUpdateService,   Notification) {
	
		$scope.configMetadata = enablix.tpIntegrationConfigMetadata;
		$scope.intConfigDataBackup = {};
		$scope.intConfigData = {};
		
		$scope.addOrEditAction = $state.current.name.indexOf('add') > 0 || $state.current.name.indexOf('edit') > 0;
		
		if (!isNullOrUndefined($stateParams.intConfigKey)) {
			
			ConfigurationService.getConfigByKey($stateParams.intConfigKey, function(data) {
				$scope.selectIntTypeMd = findIntegrationConfigMetadata(data.key);
				$scope.intConfigData = data;
				angular.copy($scope.intConfigData, $scope.intConfigDataBackup);
			});
			
		}
		
		$scope.changeIntegrationType = function(selectIntType) {

			$scope.selectIntTypeMd = findIntegrationConfigMetadata(selectIntType);
			
			if ($scope.selectIntTypeMd != null) {
				$scope.intConfigData = {
						key : $scope.selectIntTypeMd.tpKey,
						config: {}
				};
			}
		}
		
		var updateSelectedIntTypeMetadata = function(_intConfigData) {
			$scope.selectIntTypeMd = findIntegrationConfigMetadata(_intConfigData.key);
		};
		
		
		$scope.saveIntegrationConfig = function() {
			
			ConfigurationService.saveConfiguration($scope.intConfigData, 
				function() {
					
					angular.copy($scope.intConfigData, $scope.intConfigDataBackup);
					$scope.updateAddOrEditAction(false);
					
					Notification.primary("Saved successfully!");
					
				}, function() {
					Notification.error({message: "Error saving integration config", delay: enablix.errorMsgShowTime});
				});
		};
		
		$scope.updateAddOrEditAction = function(isAddOrEdit) {
			$scope.addOrEditAction = isAddOrEdit;
		};
		
		$scope.cancelOperation = function() {
			angular.copy($scope.intConfigDataBackup, $scope.intConfigData);
			$scope.updateAddOrEditAction(false);
			updateSelectedIntTypeMetadata($scope.intConfigData);
		};
	}
]);	

enablix.studioApp.controller('ThirdPartyIntegrationListController', 
			['$scope', '$state', '$stateParams', 'ConfigurationService', 'StateUpdateService', 'DataSearchService', 'Notification',
	function( $scope,   $state,   $stateParams,   ConfigurationService,   StateUpdateService,   DataSearchService,   Notification) {
	
		var CONFIG_DOMAIN_TYPE = "com.enablix.core.domain.config.Configuration";
				
		$scope.dataList = [];
		
		$scope.pagination = {
				pageSize: enablix.defaultPageSize,
				pageNum: 0,
				sort: {
					field: "createdAt",
					direction: "DESC"
				}
			};
			
		$scope.tableHeaders =
			 [{
				 desc: "Integration Type",
				 valueFn: function(record) { var intConfigMd = findIntegrationConfigMetadata(record.key); return intConfigMd == null ? "" : intConfigMd.tpName; },
			 },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName",
		    	 sortProperty: "createdByName"
		     }];	
		
		$scope.navToIntConfigDetail = function(_record) {
			StateUpdateService.goToTPIntConfigDetail(_record.key);
		}
		
		$scope.navToEditIntConfig = function(_record) {
			StateUpdateService.goToEditTPIntConfig(_record.key);
		}
		
		$scope.navToAddIntConfig = function() {
			StateUpdateService.goToAddTPIntConfig();
		}
		
		$scope.deleteIntConfig = function(_record) {
			
			ConfigurationService.deleteByIdentity(_record.identity, function() {
				Notification.primary("Removed successfully!");
				StateUpdateService.reload();
			}, function(errorData) {
				Notification.error({message: "Error removing integration configuration", delay: enablix.errorMsgShowTime});
			});
		}
		
		$scope.tableRecordActions = 
			[{
				actionName: "Detail",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.navToIntConfigDetail
			},
			{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.navToEditIntConfig
			},
			{
				actionName: "Remove",
				tooltip: "Delete",
				iconClass: "fa fa-times",
				tableCellClass: "remove",
				actionCallbackFn: $scope.deleteIntConfig
			}];
		
		$scope.filterMetadata = {
				"keyRegex" : {
					"field" : "key",
					"operator" : "REGEX",
					"dataType" : "STRING"
				}
		};
		
		$scope.fetchSearchResult = function() {

			var _dataFilters = {
					keyRegex: "^integration.*"
			};
			
			DataSearchService.getSearchResult(CONFIG_DOMAIN_TYPE, _dataFilters, $scope.pagination, $scope.filterMetadata,
				function(dataPage) {
					
					$scope.dataList = dataPage.content;
					$scope.pageData = dataPage;
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.fetchSearchResult();
	}
]);	
