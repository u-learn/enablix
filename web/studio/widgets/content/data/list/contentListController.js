enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$state', '$stateParams', 'ContentDataService', 'ContentTemplateService',
	function( $scope,   $state,   $stateParams,   ContentDataService,   ContentTemplateService) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		$scope.listHeaders = [];
		$scope.pageHeading = $scope.containerDef.label;
		
		$scope.navToAddContent = function() {
			$state.go('studio.add', {
				'containerQId': containerQId,
				'parentIdentity': parentIdentity
			});
		}
		
		angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
			
			var dataType = "string";
			
			switch (containerAttr.type) {
				case "text":
					dataType = "string";
					break;
					
				case "numeric":
					dataType = "number";
					break;
					
				case "dateTime":
					dataType = "date";
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
		
	}
]);