enablix.studioApp.controller('PortalNavLinkCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService) {
		
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
			
			if (ContentTemplateService.isRootContainer(enablix.template, _containerQId)) {
				
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
				
			} else {
				
				// find the root container
				var rootNavItem = navItem;
				while (!isNullOrUndefined(rootNavItem.previous)) {
					rootNavItem = rootNavItem.previous;
				}
				
				StateUpdateService.goToPortalSubItem(
						rootNavItem.qualifiedId, rootNavItem.identity, _containerQId, _contentIdentity);
			}
		}
		
	}]);
