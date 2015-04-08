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

			var getContainerDefinition = function(_template, _elementQId) {
				
				var elemDataDef = undefined;
				var elemQIdArr = _elementQId.split("\.");
				
				var containers = _template.dataDefinition.container;

				for (var k = 0; k < elemQIdArr.length; k++) {
					
					var nextCntnrId = elemQIdArr[k];
					var matchFound = false;
					
					// find the container Data definition
					for (var i = 0; i < containers.length; i++) {
	
						var currCntnr = containers[i];
						
						if (currCntnr.id == nextCntnrId) {
							matchFound = true;
							elemDataDef = currCntnr;
							containers = currCntnr.container;
							break;
						}
						
						if (!matchFound) {
							elemDataDef = undefined;
						}
						
					}
				}
				
				return elemDataDef;
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
		
			var isRootContainer = function(_template, _containerQId) {
				
				var rootContainers = _template.dataDefinition.container;
				
				for (var i = 0; i < rootContainers.length; i++) {
				
					var currCntnr = rootContainers[i];
					if (currCntnr.qualifiedId == _containerQId) {
						return true;
					}
				}
				
				return false;
			}
			
			return {
				getTemplate : getTemplate,
				getContainerLabelAttrId : getContainerLabelAttrId,
				getUIDefinition: getUIDefinition,
				getContainerDefinition: getContainerDefinition,
				isRootContainer: isRootContainer
			};
		
		}
	]);