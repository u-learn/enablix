enablix.studioApp.factory('ContentIndexService', 
	[	'RESTService', 'ContentDataService', 'ContentTemplateService', 'ContentUtil', 'Notification',
	 	function(RESTService, ContentDataService, ContentTemplateService, ContentUtil, Notification) {
		
			var templateId = "";
			
			var contentIndexTransformer = function(containerList, ignoreInstanceLoad, prntContainerQId) {
				
				var indexData = [];
				
				ignoreInstanceLoad = true;
				
				var parentCntnrQId = prntContainerQId || '~root~';
				buildContentIndexFromContainer(indexData, containerList, null, parentCntnrQId, null, ignoreInstanceLoad);
				
				return indexData;
			};
			
			var buildContentIndexFromContainer = function(_childrenList, 
							_containerList, _elementIdentity, _parentCntnrQId, _parentNode, ignoreInstanceLoad) {
			
				var enclosures = ContentTemplateService.getContainerEnclosures(_parentCntnrQId);
				
				var enclosureNodes = new Array();
				var cntnrIdToEnclosureMap = new Array();
				
				// identify and build enclosure by grouping the container which belong to an enclosure
				angular.forEach(enclosures, function(enclosure) {
					
					var enclosureNd = {
							"id" : enclosure.id,
							"qualifiedId" : enclosure.id,
							"label" : enclosure.label,
							"elementIdentity" : null,
							"parentIdentity" : null,
							"children" : [],
							"containerDef": null,
							"type": "enclosure",
							"uiClass": "eb-indx-container",
							"parentNode": _parentNode,
							"expandable": true
						};
					
					angular.forEach(enclosure.childContainer, function(childCntnr) {
						cntnrIdToEnclosureMap[childCntnr.id] = enclosureNd;
					});
					
					enclosureNodes.push(enclosureNd);
				});
				
				angular.forEach(_containerList, function(cntnr, index) {

					var cntnrLabel = cntnr.label;
					var cntnrQId = cntnr.qualifiedId;
					
					if (!isNullOrUndefined(cntnr.linkContainerQId)) {
						cntnr = ContentTemplateService.getRootContainer(cntnr.linkContainerQId);
						if (isNullOrUndefined(cntnr)) {
							return;
						}
						
						if (isNullOrUndefined(cntnrLabel)) {
							cntnrLabel = cntnr.label
						}
					}
					
					// ignore refData containers
					if (cntnr.refData) {
						return;
					}
				
					var cntnrType = cntnr.single ? "container-instance" : "container";
					var elemIdentity = cntnr.single ? null : _elementIdentity;
					
					// add container listing node parent
					var indxItem = {
						"id" : cntnr.qualifiedId,
						"qualifiedId" : cntnrQId,
						"label" : cntnrLabel,
						"elementIdentity" : elemIdentity,
						"parentIdentity" : _elementIdentity,
						"children" : [],
						"containerDef": cntnr,
						"type": cntnrType,
						"uiClass": "eb-indx-container"
					};
					
					setIndexNodeExpandable(indxItem);
				
					var enclosureOfChildCntnr = cntnrIdToEnclosureMap[cntnr.id];
					
					if (!isNullOrUndefined(enclosureOfChildCntnr)) {
						// this container is part of enclosure
						indxItem.parentNode = enclosureOfChildCntnr;
						enclosureOfChildCntnr.children.push(indxItem);
						
					} else {
						indxItem.parentNode = _parentNode;
						_childrenList.push(indxItem);
					}
					
					if (!ignoreInstanceLoad || indxItem.type == "container-instance") {
						loadIndexChildren(indxItem, !ignoreInstanceLoad);
					}
					
					/*var childLabelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, cntnr.qualifiedId);
					
					if ((!isNullOrUndefined(childLabelAttrId) || cntnr.single) && !ignoreInstanceLoad) {
						// add container data instance node
						ContentDataService.getContentData(templateId, indxItem.qualifiedId, _elementIdentity, function(data) {
							
							addInstanceDataChildren(indxItem, cntnr, data);
							
						}, function(data) {
							Notification.error({message: "Error retrieving content data for template [" 
									+ templateId + ", " + indxItem.qualifiedId + "]", delay: enablix.errorMsgShowTime});
							//alert("Error retrieving content data for template [" + templateId + ", " + indxItem.qualifiedId + "]");
						});
					}*/
				});
				
				angular.forEach(enclosureNodes, function(encNode) {
					if (encNode.children && encNode.children.length > 0) {
						_childrenList.push(encNode);
					}
				});
			};
			
			var setIndexNodeExpandable = function(_indxNode) {
				return _indxNode.expandable = _indxNode.type == "container" 
					|| (_indxNode.type == "instance" && _indxNode.containerDef.container 
							&& _indxNode.containerDef.container.length > 0);
			};
			
			var addInstanceDataChildren = function(_indxDataParent, _containerDef, _dataItems, _ignoreInstanceLoad) {
				_indxDataParent.children = [];
				angular.forEach(_dataItems, function(dataItem) {
					addInstanceDataChild(_indxDataParent, _containerDef, dataItem, _ignoreInstanceLoad);
				});
				_indxDataParent.dataLoaded = true;
			};
			
			var addInstanceDataChild = function(_indxDataParent, _containerDef, dataItem, _ignoreInstanceLoad) {

				var childLabelAttrId = ContentTemplateService.getContainerLabelAttrId(
											enablix.template, _containerDef.qualifiedId);
				
				var indxDataItem = null;
				
				if (_containerDef.single) {
					_indxDataParent.elementIdentity = dataItem.identity;
					indxDataItem = _indxDataParent;
					
				} else {
					
					if (!isNullOrUndefined(childLabelAttrId)) {
					
						var nodeLabel = ContentUtil.resolveContainerInstanceLabel(_containerDef, dataItem); 
						
						var indxDataItem = {
							"id" : dataItem.identity,
							"qualifiedId" : _containerDef.qualifiedId,
							"label" : nodeLabel,
							"elementIdentity" : dataItem.identity,
							"children" : [],
							"containerDef": _containerDef,
							"type": "instance",
							"uiClass": "eb-indx-instance",
							"parentNode": _indxDataParent
						};
						
						setIndexNodeExpandable(indxDataItem);
						
						_indxDataParent.children.push(indxDataItem);
					}
				}
				
				if (!isNullOrUndefined(indxDataItem)) {
					// recursively build the child elements
					buildContentIndexFromContainer(indxDataItem.children, _containerDef.container, 
							dataItem.identity, _containerDef.qualifiedId, indxDataItem, _ignoreInstanceLoad);
				}

				return indxDataItem;
			} 
			
			var getContentIndexData = function(_templateId, _studioName, _onSuccess, _onError) {
				
				templateId = _templateId;

				var studioConfig = ContentTemplateService.getStudioConfig(_studioName);
				var rootContainers = studioConfig.containerList;
				
				rootContainers = contentIndexTransformer(rootContainers, !studioConfig.navigableIndex);
				
				_onSuccess(rootContainers);
			};
			
			var updateNodeData = function(_treeNode, _data) {
				if (_treeNode.containerDef && !_treeNode.containerDef.single
						&& _treeNode.elementIdentity == _data.identity) {
					_treeNode.label = ContentUtil.resolveContainerInstanceLabel(_treeNode.containerDef, _data);
				}
			};
			
			var deleteInstanceChildNode = function(_parentNode, childIdentity) {
				
				if (!isNullOrUndefined(_parentNode)) {
					
					var childrenList = _parentNode.children || [];
					
					for (var i = 0; i < childrenList.length; i++) {
						
						var child = childrenList[i];
						
						if (child.elementIdentity == childIdentity) {
							
							if (child.containerDef.single) {
								child.elementIdentity = null;
							} else {
								childrenList.splice(i, 1);
							}
							
							break;
						}
					}
				}
			};
			
			var getPortalIndexForContainer = function(_containerQId, _elementIdentity) {
				
				var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);
				
				var indexList = []
				
				var abtIndexItem = {
						"id" : _elementIdentity,
						"qualifiedId" : _containerQId,
						"label" : "About",
						"elementIdentity" : _elementIdentity,
						"children" : [],
						"containerDef": containerDef,
						"type": "instance"
					};
				
				indexList.push(abtIndexItem);
				
				var indxCntnrList = contentIndexTransformer(containerDef.container, true, _containerQId);
				angular.forEach(indxCntnrList, function(indxItem) {
					indexList.push(indxItem);
				});
				
				return indexList;
			}
			
			var refreshNodeChildren = function(_indxNode, _childrenList, _loadChildren) {
				loadIndexChildren(_indxNode, _loadChildren, _childrenList);
			};
			
			var loadIndexChildren = function(_indxNode, _loadChildren, _childrenList) {
				
				var cntnr = _indxNode.containerDef;
				
				var childLabelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, cntnr.qualifiedId);
				
				if ((!isNullOrUndefined(childLabelAttrId) || cntnr.single) && _indxNode.type != "instance") {
					
					_indxNode.dataLoading = true;
					
					if (isNullOrUndefined(_childrenList)) {
						
						var parentIdentity = isNullOrUndefined(_indxNode.parentIdentity) ? _indxNode.elementIdentity : _indxNode.parentIdentity;
						
						// add container data instance node
						ContentDataService.getContentData(templateId, _indxNode.qualifiedId, parentIdentity, function(data) {
							
							addInstanceDataChildren(_indxNode, cntnr, data, !_loadChildren);
							_indxNode.dataLoading = false;
							
						}, function(data) {
							Notification.error({message: "Error retrieving content data for template [" 
									+ templateId + ", " + _indxNode.label + "]", delay: enablix.errorMsgShowTime});
							
							_indxNode.dataLoading = false;
						});
						
					} else {
						addInstanceDataChildren(_indxNode, cntnr, _childrenList, !_loadChildren);
						_indxNode.dataLoading = false;
					}
					
				}
				
				_indxNode.dataLoaded = true;
				
			};
			
			return {
				getContentIndexData: getContentIndexData,
				addInstanceDataChild: addInstanceDataChild,
				addInstanceDataChildren: addInstanceDataChildren,
				updateNodeData: updateNodeData,
				deleteInstanceChildNode: deleteInstanceChildNode,
				getPortalIndexForContainer: getPortalIndexForContainer,
				loadIndexChildren: loadIndexChildren,
				refreshNodeChildren: refreshNodeChildren
			};
	 	}
	]);