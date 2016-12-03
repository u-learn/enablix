enablix.studioApp.factory('ContentOperInitService', 
	[
	 		    'ContentDataService', 'ContentTemplateService', 'QIdUtil',
	 	function(ContentDataService,   ContentTemplateService,   QIdUtil) {
	 		
	 		var initAddContentOper = function(_scope, _containerQId, _parentIdentity) {
	 			
	 			_scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);
	 			var containerLabel = _scope.containerDef.label;
	 			
	 			var linkContainerValue = null;
	 			var linkContentItemId = null;
	 			
	 			if (!isNullOrUndefined(_scope.containerDef.linkContainerQId)) {
	 				
	 				linkContentItemId = _scope.containerDef.linkContentItemId;
	 				linkContainerValue = [{id: _parentIdentity}];
	 				
	 				_scope.containerDef = ContentTemplateService.getContainerDefinition(
	 						enablix.template, _scope.containerDef.linkContainerQId);
	 				
	 				if (isNullOrUndefined(containerLabel)) {
	 					containerLabel = _scope.containerDef.label;
	 				}
	 			}
	 			
	 			_scope.parentRecord = {};
	 			
	 			if (!isNullOrUndefined(_parentIdentity) && _parentIdentity != "") {
	 			
	 				var parentQId = QIdUtil.getParentQId(_containerQId);
	 				
	 				ContentDataService.getContentRecordData(enablix.templateId, parentQId, _parentIdentity, null,
	 					function(data) {
	 				
	 						_scope.parentRecord = data;
	 						
	 						if (!isNullOrUndefined(_scope.parentRecord)) {
	 							
	 							var inheritableItems = ContentTemplateService.getInheritableItems(_scope.containerDef.qualifiedId, parentQId);
	 							
	 							angular.forEach(inheritableItems, function(inheritableItem) {
	 								_scope.containerData[inheritableItem.contentItemId] = 
	 									getParentItemValue(_scope.parentRecord, inheritableItem.parentContentItemId);
	 							});
	 						}
	 					});
	 			}
	 			
	 			_scope.pageHeading = "Add " + containerLabel;
	 			
	 			_scope.containerData = {};
	 			
	 			if (!isNullOrUndefined(linkContainerValue)) {
	 				_scope.containerData[linkContentItemId] = linkContainerValue;
	 			}
	 		};
	 		
	 		function getParentItemValue(_parentRecord, parentContentItemId) {
	 			
	 			var parentVal = _parentRecord[parentContentItemId];
	 			var copiedVal = undefined;
	 			
	 			if (!isNullOrUndefined(parentVal)) {
	 				
	 				copiedVal = [];
	 				
	 				if (Array.isArray(parentVal)) {
		 				
	 					angular.forEach(parentVal, function(val) {
		 					copiedVal.push({id: val.id, label:val.label});
		 				});
		 				
	 				} else {
	 					copiedVal.push({id: parentVal});
	 				}
	 			}
	 			
	 			return copiedVal
	 		};
	 		
	 		var initEditContentOper = function(_scope, _containerQId, _contentIdentity) {
	 			
	 			_scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);
	 			var containerLabel = _scope.containerDef.label;
	 			
	 			if (!isNullOrUndefined(_scope.containerDef.linkContainerQId)) {
	 				
	 				_scope.containerDef = ContentTemplateService.getContainerDefinition(
	 						enablix.template, _scope.containerDef.linkContainerQId);
	 				
	 				if (isNullOrUndefined(containerLabel)) {
	 					containerLabel = _scope.containerDef.label;
	 				}
	 			}
	 			
	 			_scope.pageHeading = "Edit " + containerLabel;
	 			
	 			_scope.containerData = {};
	 			
	 			ContentDataService.getContentRecordData(enablix.templateId, _containerQId, _contentIdentity, null,
	 					function(data) {
	 						_scope.containerData = angular.copy(data);
	 					}, 
	 					function(data) {
	 						//alert('Error retrieving record data');
	 						Notification.error({message: "Error retrieving record data", delay: enablix.errorMsgShowTime});
	 					});
	 		};
	 		
	 		return {
	 			initAddContentOper: initAddContentOper,
	 			initEditContentOper: initEditContentOper
	 		};
	 	}
	 ]);