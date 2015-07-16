enablix.studioApp.factory('ContentTemplateService', 
	[	'RESTService', 'Notification',
	 	function(RESTService, Notification) {
		
			var loadTemplate = function() {
				
				return RESTService.getForData("fetchDefaultContentTemplate", null, null, 
					function(data) {
						enablix.template = data;
						enablix.templateId = data.id;
					}, 
					function(resp, status) {
						//alert("Error loading content template");
						Notification.error({message: "Error loading content template", delay: enablix.errorMsgShowTime});
					});
				
			};
	
			var getTemplate = function(_templateId, _onSuccess, _onError) {
				
				if (enablix.template) {
					_onSuccess(enablix.template);
					
				} else {
					var params = { "templateId": _templateId };
					RESTService.getForData("fetchContentTemplate", params, null, _onSuccess, _onError);
				}
				
			};

			var getDefaultTemplate = function(_onSuccess, _onError) {
				
				RESTService.getForData("fetchDefaultContentTemplate", null, null, function(data) {
					enablix.template = data;
					enablix.templateId = data.id;
					_onSuccess(data);
				}, _onError);
				
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
				
				var labelFieldId = null;
				
				if (!angular.isUndefined(cntnrUIDef)) {
					
					var labelQId = cntnrUIDef.container.labelQualifiedId;

					if (!angular.isUndefined(labelQId) && labelQId != null) {
					
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
			
			var getBoundedValueList = function(_templateId, _contentItemDef, _transform, _onSuccess, _onError) {
				
				if (_contentItemDef.bounded.fixedList) {
					
					var itemList = [];
					
					angular.forEach(_contentItemDef.bounded.fixedList.data, function(optItem) {
						itemList.push({id: optItem.id, label: optItem.label});
					});
					
					if (_transform) {
						itemList = _transform(itemList);	
					}
					
					_onSuccess(itemList);
					
				} else if(_contentItemDef.bounded.refList) {
					
					var params = { 
							"templateId": _templateId,
							"contentQId": _contentItemDef.qualifiedId
						};
					RESTService.getForData("fetchBoundedRefList", params, _transform, _onSuccess, _onError);
				}
			}
			
			var getContainerEnclosures = function(_cntnrQId) {
				
				var cntnrUIDef = getUIDefinition(enablix.template, _cntnrQId);
				
				if (!isNullOrUndefined(cntnrUIDef) && !isNullOrUndefined(cntnrUIDef.container) 
						&& !isNullOrUndefined(cntnrUIDef.container.enclosure)) {
					return cntnrUIDef.container.enclosure;
				}
				
				return [];
			}
			
			var getContainerListViewHiddenItems = function(_cntnrQId) {
				
				var cntnrUIDef = getUIDefinition(enablix.template, _cntnrQId);
				
				if (!isNullOrUndefined(cntnrUIDef) && !isNullOrUndefined(cntnrUIDef.container) 
						&& !isNullOrUndefined(cntnrUIDef.container.listViewConfig)
						&& !isNullOrUndefined(cntnrUIDef.container.listViewConfig.hideContentItem)) {
					return cntnrUIDef.container.listViewConfig.hideContentItem;
				}
				
				return [];
			}
			
			var getRootContainers = function() {
				
				if (enablix.template) {
					return enablix.template.dataDefinition.container;
					
				} else {
					Notification.error({message : "App not initialized properly", delay: enablix.errorMsgShowTime});
					return [];
				}
			}
			
			var getPortalTopNavItemContainers = function() {
				if (enablix.template.portalUIDefinition) {
					return enablix.template.portalUIDefinition.topNavigation.itemContainers.itemContainer;
				}
				return [];
			}
			
			var getPortalTopNavEnclosures = function() {
				if (enablix.template.portalUIDefinition && enablix.template.portalUIDefinition.topNavigation.enclosures) {
					return enablix.template.portalUIDefinition.topNavigation.enclosures.enclosure;
				}
				return [];
			}
			
			var getPortalCondensedViewItems = function(_containerQId) {
				
				var items = [];
			
				var cntnrUIDef = getUIDefinition(enablix.template, _containerQId);
					
				if (!isNullOrUndefined(cntnrUIDef) && !isNullOrUndefined(cntnrUIDef.container) 
						&& !isNullOrUndefined(cntnrUIDef.container.portalConfig)
						&& !isNullOrUndefined(cntnrUIDef.container.portalConfig.condensedView)) {
					items = cntnrUIDef.container.portalConfig.condensedView.showContentItem;
				}
				
				return items;
			};
			
			var getPortalHeadingContentItem = function(_containerQId) {
				
				var cntnrUIDef = getUIDefinition(enablix.template, _containerQId);
				
				if (!isNullOrUndefined(cntnrUIDef) && !isNullOrUndefined(cntnrUIDef.container) 
						&& !isNullOrUndefined(cntnrUIDef.container.portalConfig)
						&& !isNullOrUndefined(cntnrUIDef.container.portalConfig.headingContentItem)) {
					return cntnrUIDef.container.portalConfig.headingContentItem.id;
				}
				
				return null;
			}
			
			var getPortalEnclosureDefinition = function(_enclosureId) {
				var enclList = getPortalTopNavEnclosures();
				for (var i = 0; i < enclList.length; i++) {
					var encl = enclList[i];
					if (encl.id == _enclosureId) {
						return encl;
					}
				}
				return null;
			}
			
			var getRootContainerIdForContainer = function(_containerQId) {
				var cntnrQIdArr = _containerQId.split("\.");
				return cntnrQIdArr[0];
			}
			
			return {
				getTemplate : getTemplate,
				getDefaultTemplate : getDefaultTemplate,
				getContainerLabelAttrId : getContainerLabelAttrId,
				getUIDefinition: getUIDefinition,
				getContainerDefinition: getContainerDefinition,
				isRootContainer: isRootContainer,
				getBoundedValueList: getBoundedValueList,
				loadTemplate: loadTemplate,
				getContainerEnclosures: getContainerEnclosures,
				getContainerListViewHiddenItems: getContainerListViewHiddenItems,
				getRootContainers : getRootContainers,
				getPortalTopNavItemContainers: getPortalTopNavItemContainers,
				getPortalTopNavEnclosures : getPortalTopNavEnclosures,
				getPortalCondensedViewItems: getPortalCondensedViewItems,
				getPortalHeadingContentItem: getPortalHeadingContentItem,
				getPortalEnclosureDefinition: getPortalEnclosureDefinition,
				getRootContainerIdForContainer: getRootContainerIdForContainer
			};
		
		}
	]);