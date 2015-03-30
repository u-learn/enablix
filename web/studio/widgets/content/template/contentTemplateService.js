enablix.studioApp.factory('ContentTemplateService', 
	[	'RESTService',
	 	function(RESTService) {
	
			var getTemplate = function(_templateId, _onSuccess, _onError) {
				var params = { "templateId": _templateId };
				RESTService.getForData("fetchContentTemplate", params, null, _onSuccess, _onError);
			};
			
			var getUIDefinition = function(_template, _elementQId) {
				
				var elemUIDef = undefined;
				
				var uiDefinitions = _template.uiDefinition.contentUIDef;
				
				// find the container UI definition
				for (var i = 0; i < uiDefinitions.length; i++) {

					var uiDef = uiDefinitions[i]; 
					
					if (uiDef.qualifiedId == _elementQId) {
						elemUIDef = uiDef;
						break;
					}
				}
				
				return elemUIDef;
			}
			
			var getContainerLabelAttrId = function(_template, _containerQId) {
				
				var cntnrUIDef = getUIDefinition(_template, _containerQId);
				
				var labelFieldId = 'name';
				
				if (!angular.isUndefined(cntnrUIDef)) {
					
					var labelQId = cntnrUIDef.container.labelQualifiedId;

					if (!angular.isUndefined(labelQId)) {
					
						if (labelQId.indexOf(_containerQId) == 0) { // starts with container QId, strip it
							labelFieldId = labelQId.substring(_containerQId.length + 1, labelQId.length);
							
						} else {
							labelFieldId = labelQId;
						}
					}
				}
				
				return labelFieldId;
			};
		
			return {
				getTemplate : getTemplate,
				getContainerLabelAttrId : getContainerLabelAttrId,
				getUIDefinition: getUIDefinition
			};
		
		}
	]);