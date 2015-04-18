enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$state', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService',
	function( $scope,   $state,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		$scope.containerDef = {};
		$scope.listHeaders = [];
		
		ContentTemplateService.getTemplate(enablix.templateId, function(template) {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(template, containerQId);
			
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
			});
			
			ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
					function(data) {
						$scope.listData = data;
					}, 
					function(data) {
						alert('Error retrieving list data');
					});
		});
		
		$scope.pageHeading = $scope.containerDef.label;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(elementIdentity);
		};
		
		
		
	}
]);