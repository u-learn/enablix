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
				
	 		return {
	 			resolveContainerInstanceLabel: resolveContainerInstanceLabel,
	 			resolveContainerInstancePortalLabel: resolveContainerInstancePortalLabel
	 		};
	 	}
	 ]);