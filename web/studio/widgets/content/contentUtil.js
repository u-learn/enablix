enablix.studioApp.factory('ContentUtil', 
	[
	 	'RESTService', 'ContentTemplateService',
	 	function(RESTService, ContentTemplateService) {
	 		
			var resolveContainerInstanceLabel = function(_containerDef, _instanceData) {
				
				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(
										enablix.template, _containerDef.qualifiedId);

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
	 		
	 		return {
	 			resolveContainerInstanceLabel: resolveContainerInstanceLabel
	 		};
	 	}
	 ]);