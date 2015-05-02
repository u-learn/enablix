enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		$scope.containerDef = {};
		$scope.listHeaders = [];
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
			
			var dataType = "string";
			
			switch (containerAttr.type) {
				case "TEXT":
					dataType = "string";
					break;
					
				case "NUMERIC":
					dataType = "number";
					break;
					
				case "DATE_TIME":
					dataType = "date";
					break;
					
				case "DOC":
					dataType = "doc";
					break;
					
				case "BOUNDED":
					dataType = "select";
					break;
			}
			
			var header = {
				"key" : containerAttr.id,
				"desc" : containerAttr.label,
				"dataType" : dataType 
			};
			
			$scope.listHeaders.push(header);
			
			ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
					function(data) {
						$scope.listData = data;
					}, 
					function(data) {
						//alert('Error retrieving list data');
						Notification.error({message: "Error retrieving list data", delay: null});
					});
		});
		
		$scope.pageHeading = $scope.containerDef.label;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(containerQId, elementIdentity);
		};
		
	}
]);