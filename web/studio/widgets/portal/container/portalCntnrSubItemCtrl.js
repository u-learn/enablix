enablix.studioApp.controller('PortalCntnrSubItemCtrl', 
		   ['$scope', '$state', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'Notification', 'ContentUtil',
    function($scope,   $state,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   Notification,   ContentUtil) {

		$scope.containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.subContainerQId);
		
		$scope.$stateParams = $stateParams;
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
										_containerDef, _dataRecord);
			
			for (var i = 0; i < _containerDef.contentItem.length; i++) {
				
				var item = _containerDef.contentItem[i];
				
				if (item.type == 'DOC') {
				
					var docInstance = _dataRecord[item.id];
					if (docInstance && docInstance.identity) {
						_dataRecord.downloadDocIdentity = docInstance.identity;
					}
					
					break;
				}
			}
		}
		
		ContentDataService.getContentRecordData(enablix.templateId, $stateParams.subContainerQId, 
				$stateParams.subItemIdentity, 
				function(recordData) {
					decorateData($scope.containerDef, recordData);
					$scope.bodyData = recordData;
				},
				function(errResp) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
			
		// set up headers for the view
		$scope.headers = [];
		
		angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
			
			var header = {
				"key" : containerAttr.id,
				"label" : containerAttr.label,
				"dataType" : containerAttr.type 
			};
			
			$scope.headers.push(header);
			
		});
			
	}                                          
]);