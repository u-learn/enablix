enablix.studioApp.controller('PortalCntnrBodyCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'Notification',
    function($scope,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   Notification) {

		$scope.containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.subContainerQId);
		
		if ($stateParams.containerQId === $stateParams.subContainerQId) {
			
			ContentDataService.getContentRecordData(enablix.templateId, $stateParams.containerQId, 
					$stateParams.elementIdentity, 
					function(recordData) {
						$scope.bodyData = recordData;
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
			
		} else {
			
			ContentDataService.getContentData(enablix.templateId, $stateParams.subContainerQId, 
					$stateParams.elementIdentity,
					function(data) {
						if ($stateParams.type == 'single' && data && data.length > 0) {
							$scope.bodyData = data[0];
						} else {
							$scope.bodyData = data;
						}
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
		}
		
		if ($stateParams.type == 'single') {
			
			// set up headers for the view
			$scope.singleHeaders = [];
			
			angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
				
				var header = {
					"key" : containerAttr.id,
					"label" : containerAttr.label,
					"dataType" : containerAttr.type 
				};
				
				$scope.singleHeaders.push(header);
				
			});
			
		} else {
			// set up headers for the expanded and collapsed view
		}
		
		
	}                                          
]);