enablix.studioApp.controller('PortalCntnrSubItemCtrl', 
		   ['$scope', '$state', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'Notification', 'ContentUtil',
    function($scope,   $state,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   Notification,   ContentUtil) {

		$scope.containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.subContainerQId);
		
		$scope.$stateParams = $stateParams;
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
										_containerDef, _dataRecord);
			
			ContentUtil.decorateData(_containerDef, _dataRecord, true);
		}
		
		ContentDataService.getContentRecordData(enablix.templateId, $stateParams.subContainerQId, 
				$stateParams.subItemIdentity, 'PORTAL',
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