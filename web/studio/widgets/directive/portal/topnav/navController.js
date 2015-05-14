enablix.studioApp.controller('PortalTopNavCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService) {
		
		$scope.topNavList = [];
		$scope.$stateParams = $stateParams;
		
		$scope.navToPortalTopNavItem = function(sublistItem) {
			StateUpdateService.goToPortalContainerBody(sublistItem.qualifiedId, sublistItem.identity, 
					'single', sublistItem.qualifiedId);
		};
		
		var containerList = ContentTemplateService.getRootContainers();
		angular.forEach(containerList, function(container) {
			
			if (container.refData) {
				return;
			}
			
			var navItem = {
				"id" : container.id,
				"qualifiedId" : container.qualifiedId,
				"label" : container.label,
				"children" : [],
				"containerDef" : container
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
			
			var label = resolveContainerInstanceLabel(container, subItemData);
			
			var sublistItem = {
				"identity" : subItemData.identity,
				"qualifiedId" : container.qualifiedId,
				"label" : label
			};
			
			navItem.children.push(sublistItem);
			
		};
		
		var resolveContainerInstanceLabel = function(_containerDef, _instanceData) {
			
			var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerDef.qualifiedId);

			for (var i = 0; i < _containerDef.contentItem.length; i++) {
				
				var cntItem = _containerDef.contentItem[i];
				
				if (cntItem.id == labelAttrId) {
				
					if (cntItem.type == 'TEXT') {
						return _instanceData[labelAttrId]; 
					
					} else if (cntItem.type == 'BOUNDED') {
						var bndValue = _instanceData[labelAttrId];
						if (bndValue) {
							return bndValue[0].label;
						}
					} else if (cntItem.type == 'DOC') {
						var docValue = _instanceData[labelAttrId];
						if (docValue) {
							return docValue.name;
						}
					}
				}
			}
			
			return "";
		};
		
	}]);
