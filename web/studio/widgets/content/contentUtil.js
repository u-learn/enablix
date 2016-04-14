enablix.studioApp.factory('ContentUtil', 
	[
	 	'RESTService', 'ContentTemplateService',
	 	function(RESTService, ContentTemplateService) {
	 		
			var resolveContainerInstanceLabel = function(_containerDef, _instanceData) {
				
				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(
										enablix.template, _containerDef.qualifiedId);
				
				return findLabelValue(labelAttrId, _containerDef, _instanceData);
			};
			
			var findLabelValue = function(labelAttrId, _containerDef, _instanceData) {
				
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
			}
	 		
			var resolveContainerInstancePortalLabel = function(_containerDef, _instanceData) {
				
				var portalLabelAttrId = ContentTemplateService.getPortalHeadingContentItem(_containerDef.qualifiedId);
				
				if (isNullOrUndefined(portalLabelAttrId)) {
					portalLabelAttrId = ContentTemplateService.getContainerLabelAttrId(
											enablix.template, _containerDef.qualifiedId);
				}
				
				return findLabelValue(portalLabelAttrId, _containerDef, _instanceData);
			};
			
			var decorateData = function(_containerDef, _dataRecord, _processHyperlink) {
				
				for (var i = 0; i < _containerDef.contentItem.length; i++) {
					
					var item = _containerDef.contentItem[i];
					
					if (item.type == 'DOC') {
						
						var docInstance = _dataRecord[item.id];
						if (docInstance && docInstance.identity) {
							_dataRecord.downloadDocIdentity = docInstance.identity;
						}
						
					} else if (item.type == 'BOUNDED') {
						
						var boundedItem = _dataRecord[item.id];
						
						if (_processHyperlink && !isNullOrUndefined(boundedItem) 
								&& boundedItem.length
								&& item.bounded.refList && item.bounded.refList.datastore
								&& item.bounded.refList.datastore.hyperlink) {
						
							for (var k = 0; k < boundedItem.length; k++) {
								var itemVal = boundedItem[k];
								itemVal.hrefData = {
									"containerQId": item.bounded.refList.datastore.storeId,
									"contentIdentity": itemVal.id
								};
							}
						}
					}
				}
			}
			
			var getContentListHeaders = function(_containerDef) {
				
				var listHeaders = [];
				
				var hiddenContentItemIds = [];
				
				angular.forEach(ContentTemplateService.getContainerListViewHiddenItems(_containerDef.qualifiedId), 
						function(hiddenContentItem) {
							hiddenContentItemIds.push(hiddenContentItem.id);
						});
				
				angular.forEach(_containerDef.contentItem, function(containerAttr) {
					
					if (!hiddenContentItemIds.contains(containerAttr.id)) {
						
						var header = {
							"key" : containerAttr.id,
							"desc" : containerAttr.label,
							"dataType" : containerAttr.type 
						};
						
						listHeaders.push(header);
					}
				});
				
				return listHeaders;
			};
			
	 		return {
	 			resolveContainerInstanceLabel: resolveContainerInstanceLabel,
	 			resolveContainerInstancePortalLabel: resolveContainerInstancePortalLabel,
	 			decorateData: decorateData,
	 			getContentListHeaders: getContentListHeaders
	 		};
	 		
	 		
	 	}
	 ]);