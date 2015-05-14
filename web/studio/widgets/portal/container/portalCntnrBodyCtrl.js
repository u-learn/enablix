enablix.studioApp.controller('PortalCntnrBodyCtrl', 
		   ['$scope', '$rootScope', '$stateParams', 'ContentDataService', 'StudioSetupService', 'Notification',
    function($scope,   $rootScope,   $stateParams,   ContentDataService,   StudioSetupService,   Notification) {

		if (!$scope.portalIndex.currentNode) {
			angular.forEach($scope.portalIndexData, function(indexItem) {
				if (indexItem.qualifiedId === $stateParams.subContainerQId) {
					indexItem.selected = 'selected';
					$scope.portalIndex.currentNode = indexItem;
				}
			});
		}
		
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
						$scope.bodyData = data;
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
		}
	}                                          
]);