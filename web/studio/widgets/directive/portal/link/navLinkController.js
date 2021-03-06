enablix.studioApp.controller('PortalNavLinkCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentUtil',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentUtil) {
		
		var navLink = $scope.navLink;
		
		$scope.showDocLink = !($scope.hideDocLink || false);
		
		if (!isNullOrUndefined(navLink)) {
			
			var firstLevelContainerQId = navLink.qualifiedId;
			var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, firstLevelContainerQId);
			
			$scope.navLinkArr = [];
			
			if (!$scope.hideHierarchy && !isNullOrUndefined(containerDef)) {
				var first = {
						label : containerDef.label,
						qualifiedId : firstLevelContainerQId,
						next : navLink
					};
				
				$scope.navLinkArr.push(first);
			}
	
			var previousItem = null;
			var navContentPointer = navLink;
			while (!isNullOrUndefined(navContentPointer)) {
				
				var isLastLink = isNullOrUndefined(navContentPointer.next);
				if (!$scope.hideHierarchy || isLastLink) {

					$scope.navLinkArr.push(navContentPointer);
	
					if (navLink.recordData && isLastLink) {
						var containerDef = ContentTemplateService.getContainerDefinition(
								enablix.template, navContentPointer.qualifiedId);
						if (containerDef) {
							ContentUtil.decorateData(containerDef, navLink.recordData, false, true);
						}
					}
				}
				
				navContentPointer.previous = previousItem;
				
				// move pointer to next
				previousItem = navContentPointer;
				navContentPointer = navContentPointer.next;
			}
		}
		
		$scope.navToContent = function(navItem) {
			
			var _containerQId = navItem.qualifiedId;
			var _contentIdentity = navItem.identity;
			
			StateUpdateService.goToPortalContainerDetail(
					_containerQId, _contentIdentity);
				
		}
		
	}]);
