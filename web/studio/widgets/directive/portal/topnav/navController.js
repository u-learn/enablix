enablix.studioApp.controller('PortalTopNavCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil) {
		
		$scope.topNavList = [];
		$scope.$stateParams = $stateParams;
		
		$scope.navToPortalTopNavItem = function(sublistItem) {
			StateUpdateService.goToPortalContainerBody(sublistItem.qualifiedId, sublistItem.identity, 
					'single', sublistItem.qualifiedId);
		};
		
		var itemContainerList = ContentTemplateService.getPortalTopNavItemContainers()
		angular.forEach(itemContainerList, function(itemCntnr) {
			
			var container = ContentTemplateService.getContainerDefinition(enablix.template, itemCntnr.qualifiedId);
			
			if (!container || container.refData) {
				return;
			}
			
			var navItem = {
				"id" : container.id,
				"qualifiedId" : container.qualifiedId,
				"label" : container.label,
				"children" : [],
				"containerDef" : container,
				"type": "item-container"
			};
			
			$scope.topNavList.push(navItem);
			
			// add container data instance node
			ContentDataService.getContentData(enablix.templateId, navItem.qualifiedId, null, function(data) {
				
				angular.forEach(data, function(dataItem) {
					addNavSublistItem(navItem, container, dataItem);
				});
				
			}, function(data) {
				Notification.error({message: "Error retrieving content data", delay: enablix.errorMsgShowTime});
			});
			
		});
		
		var addNavSublistItem = function(navItem, container, subItemData) {
			
			var label = ContentUtil.resolveContainerInstanceLabel(container, subItemData);
			
			var sublistItem = {
				"identity" : subItemData.identity,
				"qualifiedId" : container.qualifiedId,
				"label" : label
			};
			
			navItem.children.push(sublistItem);
			
		};
		
		var enclosureList = ContentTemplateService.getPortalTopNavEnclosures();
		angular.forEach(enclosureList, function(enclosure) {
			
			var navItem = {
					"id" : enclosure.id,
					"qualifiedId" : enclosure.id,
					"label" : enclosure.label,
					"children" : [],
					"type": "enclosure",
					"enclosureDef": enclosure
				};
			
			$scope.topNavList.push(navItem);
			
		});
		
	}]);
