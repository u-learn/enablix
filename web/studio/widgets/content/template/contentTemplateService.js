enablix.studioApp.factory('ContentTemplateService', 
	[			'RESTService', 'Notification', 'AuthorizationService', 'CacheService',
	 	function(RESTService,   Notification,   AuthorizationService,   CacheService) {
		
			var CACHE_KEY_REF_DATA_CONTAINERS = "content.template.refdata.containers";
			var CACHE_KEY_BUS_CAT_CONTAINERS_PREFIX = "content.template.business.category.";
			var CACHE_KEY_CONTENT_CONN_CONTEXT_CONTAINER = "content.template.conn.context.containers";
			var CACHE_KEY_CONTAINER_DEF_PREFIX = "content.template.containerdef.";
		
			var loadTemplate = function() {
				
				return RESTService.getForData("fetchDefaultContentTemplate", null, null, 
					function(data) {
					
						if (data == "") {
							// No template, user should be logout?
							AuthorizationService.logoutUser();
						} else {
							enablix.template = data;
							enablix.templateId = data.id;
						}
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
			
			var getContainerLabel = function(_containerQId) {
				var containerDef = getContainerDefinition(enablix.template, _containerQId);
				return containerDef ? containerDef.label : "";
			}
			
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
				
				var cacheKey = CACHE_KEY_CONTAINER_DEF_PREFIX + _elementQId;
				
				var elemDataDef = CacheService.get(cacheKey);
				
				if (isNullOrUndefined(elemDataDef)) {
					
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
				
					CacheService.put(cacheKey, elemDataDef);
				}
				
				return elemDataDef;
			};
			
			var getContentItem = function(_containerDef, _contentItemId) {

				for (var i = 0; i < _containerDef.contentItem.length; i++) {
					var item = _containerDef.contentItem[i];
					if (item.id == _contentItemId) {
						return item;
					}
				}
				
				return null;
			};
			
			var getConcreteContainerDefinition = function(_template, _containerQId) {
				
				var containerDef = getContainerDefinition(enablix.template, _containerQId);
				
				if (!isNullOrUndefined(containerDef) && isLinkedContainer(containerDef)) {
					containerDef = getContainerDefinition(enablix.template, containerDef.linkContainerQId);
				}
				
				return containerDef;
			}
			
			var isLinkedContainer = function(_containerDef) {
				return !isNullOrUndefined(_containerDef.linkContainerQId);
			}
			
			var getContainerLabelAttrId = function(_template, _containerQId) {
				
				var cntnrUIDef = getUIDefinition(_template, _containerQId);
				
				var labelFieldId = null;
				
				if (!angular.isUndefined(cntnrUIDef)) {
					
					var labelQId = cntnrUIDef.container.labelQualifiedId;
					
					if (isNullOrUndefined(labelQId)) {
						// check for portal heading config
						if (cntnrUIDef.container.portalConfig && cntnrUIDef.container.portalConfig.headingContentItem) {
							labelQId = cntnrUIDef.container.portalConfig.headingContentItem.id;
						}
					}

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
			};
			
			var getBoundedRefListContainer = function(_contentItemDef) {
				
				if (isBoundedRefListItem(_contentItemDef)) {
					var refContainerId = _contentItemDef.bounded.refList.datastore.storeId;
					return getContainerDefinition(enablix.template, refContainerId);
				}
				
				return null;
			};
			
			var checkAndGetBoundedRefListContainerQId = function(_contentItemDef) {
				if (isBoundedRefListItem(_contentItemDef)) {
					return _contentItemDef.bounded.refList.datastore.storeId;
				}
				return null;
			};
			
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
					
					if (!isNullOrUndefined(_contentItemDef.qualifiedId)) {
						
						var params = { 
								"templateId": _templateId,
								"contentQId": _contentItemDef.qualifiedId
							};
						
						RESTService.getForData("fetchBoundedRefList", params, _transform, _onSuccess, _onError);
						
					} else {
						RESTService.postForData("fetchBoundedDefRefList", null, _contentItemDef.bounded, _transform, _onSuccess, _onError, null);
					}
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
			
			var getRootContainer = function(_containerId) {
				
				var rootContainers = enablix.template.dataDefinition.container;
				
				for (var i = 0; i < rootContainers.length; i++) {
				
					var currCntnr = rootContainers[i];
					if (currCntnr.qualifiedId == _containerId) {
						return currCntnr;
					}
				}
				
				return null;
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
			
			var getParentEnclosureDefinition = function(_childId) {
				var enclList = getPortalTopNavEnclosures();
				for (var i = 0; i < enclList.length; i++) {
					var encl = enclList[i];
					for (var j = 0; j < encl.childContainer.length; j++) {
						var childContainer = encl.childContainer[j];
						if (childContainer.id === _childId) {
							return encl;
						}
					}
				}
				return null;
			}
			
			var getRootContainerIdForContainer = function(_containerQId) {
				var cntnrQIdArr = _containerQId.split("\.");
				return cntnrQIdArr[0];
			};
			
			var getInheritableItems = function(_containerQId, _parentContainerQId) {
				
				var inheritableItems = [];
				
				var containerDef = getContainerDefinition(enablix.template, _containerQId);
				var parentContainerDef = getContainerDefinition(enablix.template, _parentContainerQId);
				
				if (isNullOrUndefined(containerDef) || isNullOrUndefined(parentContainerDef)) {
					return inheritableItems;
				}
				
				angular.forEach(containerDef.contentItem, function(itemDef) {
					
					if (isBoundedRefListItem(itemDef)) {
					
						if (itemDef.bounded.refList.datastore.storeId == parentContainerDef.id) {
							
							inheritableItems.push({
									contentItemId: itemDef.id,
									parentContentItemId: itemDef.bounded.refList.datastore.dataId
								});
							
						} else {
							
							for (var i = 0; i < parentContainerDef.contentItem.length; i++) {
							
								var parentItemDef = parentContainerDef.contentItem[i];
								
								if (isBoundedRefListItem(parentItemDef)
										&& matchBoundedRefListItems(itemDef, parentItemDef)) {
								
									inheritableItems.push({
											contentItemId: itemDef.id,
											parentContentItemId: parentItemDef.id
										});
									
									break;
								} 
							}
						}
					}
				});
				
				return inheritableItems;
			};
			
			var isBoundedRefListItem = function(_itemDef) {
				return _itemDef.type == "BOUNDED" && _itemDef.bounded.refList;
			}
			
			function matchBoundedRefListItems(_item1, _item2) {
				return angular.equals(_item1.bounded.refList.datastore, _item2.bounded.refList.datastore);
			}
			
			var getRefDataContainers = function() {
				
				var refDataContainers = CacheService.get(CACHE_KEY_REF_DATA_CONTAINERS);
				
				if (!isNullOrUndefined(refDataContainers)) {
					return refDataContainers;
				}
				
				refDataContainers = [];
				
				var containerList = enablix.template.dataDefinition.container;
				angular.forEach(containerList, function(containerDef) {
					if (containerDef.refData) {
						refDataContainers.push(containerDef);
					}
				});
				
				CacheService.put(CACHE_KEY_REF_DATA_CONTAINERS, refDataContainers);
				
				return refDataContainers;
			};
			
			var getContainersByBusinessCategory = function(_businessCategory) {
				
				var cacheKey = CACHE_KEY_BUS_CAT_CONTAINERS_PREFIX + _businessCategory;
				var businessCategoryContainers = CacheService.get(cacheKey);
				
				if (!isNullOrUndefined(businessCategoryContainers)) {
					return businessCategoryContainers;
				}
				
				businessCategoryContainers = [];
				
				findContainersByBusinessCategory(enablix.template.dataDefinition.container, 
						_businessCategory, businessCategoryContainers);
				
				CacheService.put(cacheKey, businessCategoryContainers);
				
				return businessCategoryContainers;
			};
			
			function findContainersByBusinessCategory(_containerList, _businessCategory, _containerHolderArr) {
				angular.forEach(_containerList, function(containerDef) {
					if (containerDef.businessCategory && !isLinkedContainer(containerDef)
							&& containerDef.businessCategory == _businessCategory) {
						_containerHolderArr.push(containerDef);
					}
					findContainersByBusinessCategory(containerDef.container, _businessCategory, _containerHolderArr);
				});
			}
			
			var isLinkedContainer = function(_containerDef) {
				return !isNullOrUndefined(_containerDef.linkContainerQId);
			};
			
			var getContentConnectionContextContainers = function() {
				
				var contextContainers = CacheService.get(CACHE_KEY_CONTENT_CONN_CONTEXT_CONTAINER);
				
				if (!isNullOrUndefined(contextContainers)) {
					return contextContainers;
				}
				
				contextContainers = [];
				
				if (!isNullOrUndefined(enablix.template.contentTypeMappingConfig)) {
					
					angular.forEach(enablix.template.contentTypeMappingConfig.contextConfig.containerDataContext, 
							function(contextCnt) {
								var containerDef = getConcreteContainerDefinition(enablix.template, contextCnt.qualifiedId);
								if (!isNullOrUndefined(containerDef)) {
									contextContainers.push(containerDef);
								}
							});
					
				} 
				
				CacheService.put(CACHE_KEY_CONTENT_CONN_CONTEXT_CONTAINER, contextContainers);
				
				return contextContainers;
			};
			
			var getUserContainerQId = function() {
				
				var userContainerQId = "user";
				
				if (enablix.template.dataDefinition.userProfileRef && 
						enablix.template.dataDefinition.userProfileRef.containerQId) {
					userContainerQId = enablix.template.dataDefinition.userProfileRef.containerQId;
				}
				
				return userContainerQId;
			};
			
			var getStudioContainers = function(_studioName) {
				return getStudioConfig(_studioName).containerList;
			};
			
			var getStudioConfig = function(_studioName) {
				
				// current we have not introduced the configuration of different studios
				// hence all containers are shown under 'content' studio
				var studioContainers = getRootContainers();
				
				return {
					containerList: studioContainers,
					navigableIndex: true // are the index tree nodes expandable
				};
			};
			
			var hasContentStackConfigItem = function(_containerDef) {
				
				if (!isNullOrUndefined(_containerDef)) {
					
					for (var i = 0; i < _containerDef.contentItem.length; i++) {
					
						var itemDef = _containerDef.contentItem[i];
						if (itemDef.type == 'CONTENT_STACK') {
							return true;
						}
					}
				}
				
				return false;
			};
			
			/**
			 * filterFn: function(_containerDef) {
			 * 		// check condition and return true or false
			 * }
			 */
			var getFilteredListOfContainers = function(_filterFn) {
				
				var containerList = [];
				
				walkContainers(function(containerDef) {
					if (_filterFn(containerDef)) {
						containerList.push(containerDef);
					}
				});
				
				return containerList;
			};
			
			var walkContainers = function(_callbackFn) {
				walkContainerList(enablix.template.dataDefinition.container, _callbackFn);
			};
			
			var walkContainerList = function(_containerList, _callbackFn) {
				angular.forEach(_containerList, function(containerDef) {
					_callbackFn(containerDef);
					walkContainerList(containerDef.container, _callbackFn);
				});
			};
			
			var hasChildContainer = function(_containerDef, _childQId) {
				
				if (!isNullOrUndefined(_containerDef)) {
					
					var childCntnrs = _containerDef.container;
					
					if (!isNullOrUndefined(childCntnrs)) {
						for (var i = 0; i < childCntnrs.length; i++) {
							if (_childQId == childCntnrs[i].qualifiedId) {
								return true;
							}
						}
					}
				}
				
				return false;
			}
			
			return {
				getTemplate : getTemplate,
				getDefaultTemplate : getDefaultTemplate,
				getContainerLabelAttrId : getContainerLabelAttrId,
				getContainerLabel: getContainerLabel,
				getUIDefinition: getUIDefinition,
				getContainerDefinition: getContainerDefinition,
				getConcreteContainerDefinition: getConcreteContainerDefinition,
				getContentItem: getContentItem,
				getBoundedRefListContainer: getBoundedRefListContainer,
				isRootContainer: isRootContainer,
				isLinkedContainer: isLinkedContainer,
				getBoundedValueList: getBoundedValueList,
				loadTemplate: loadTemplate,
				getContainerEnclosures: getContainerEnclosures,
				getContainerListViewHiddenItems: getContainerListViewHiddenItems,
				getRootContainers : getRootContainers,
				getRootContainer : getRootContainer,
				getPortalTopNavItemContainers: getPortalTopNavItemContainers,
				getPortalTopNavEnclosures : getPortalTopNavEnclosures,
				getPortalCondensedViewItems: getPortalCondensedViewItems,
				getPortalHeadingContentItem: getPortalHeadingContentItem,
				getPortalEnclosureDefinition: getPortalEnclosureDefinition,
				getParentEnclosureDefinition: getParentEnclosureDefinition,
				getRootContainerIdForContainer: getRootContainerIdForContainer,
				getInheritableItems: getInheritableItems,
				getRefDataContainers: getRefDataContainers,
				getContainersByBusinessCategory: getContainersByBusinessCategory,
				getContentConnectionContextContainers: getContentConnectionContextContainers,
				getUserContainerQId: getUserContainerQId,
				isBoundedRefListItem: isBoundedRefListItem,
				checkAndGetBoundedRefListContainerQId: checkAndGetBoundedRefListContainerQId,
				getStudioContainers: getStudioContainers,
				getStudioConfig: getStudioConfig,
				hasContentStackConfigItem: hasContentStackConfigItem,
				getFilteredListOfContainers: getFilteredListOfContainers,
				hasChildContainer: hasChildContainer,
				walkContainers: walkContainers
			};
		
		}
	]);