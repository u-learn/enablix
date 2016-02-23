enablix.studioApp.controller('PortalCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$state', '$stateParams', 'ContentDataService', 'StudioSetupService', 'ContentUtil', 'StateUpdateService',
    function($scope,   ContentTemplateService,   $state,   $stateParams,   ContentDataService,   StudioSetupService,   ContentUtil,   StateUpdateService) {

		$scope.$stateParams = $stateParams;
		
		var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
		var pagination = {
				pageNum: $stateParams.page
		};
		
		$scope.listHeaders = ContentUtil.getContentListHeaders(containerDef);

		var fetchData = function() {
		ContentDataService.getContentData(enablix.templateId, $stateParams.containerQId, null, 
				function(dataPage) {
					$scope.listData = dataPage.content;
					$scope.pageData = dataPage;
					angular.forEach($scope.listData, function(item) {
						ContentUtil.decorateData(containerDef, item);
					});
				}, 
				function(data) {
					//alert('Error retrieving list data');
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				}, pagination);
		
		}
		
		fetchData();
		
		$scope.navToContentDetail = function(contentRecordIdentity) {
			StateUpdateService.goToPortalContainerDetail($stateParams.containerQId, contentRecordIdentity);
		}
		
		$scope.setPage = function(pageNum) {
			pagination.pageNum = pageNum;
			fetchData();
		};
	}                                          
]);