enablix.studioApp.factory('ContentIndexService', 
	[	'RESTService', 'ContentDataService', 'ContentTemplateService', 'ContentUtil', 'Notification',
	 	function(RESTService, ContentDataService, ContentTemplateService, ContentUtil, Notification) {
		
			var templateId = "";
			
			var contentIndexTransformer = function(containerList, ignoreInstanceLoad, prntContainerQId) {
				
				var indexData = [];

				var parentCntnrQId = prntContainerQId || '~root~';
				buildContentIndexFromContainer(indexData, containerList, null, parentCntnrQId, null, ignoreInstanceLoad);
				
				return indexData;
			};
			
			var buildContentIndexFromContainer = function(_childrenList, 
							_containerList, _elementIdentity, _parentCntnrQId, _parentNode, ignoreInstanceLoad) {
			
				var enclosures = ContentTemplateService.getContainerEnclosures(_parentCntnrQId);
				
				var enclosureNodes = new Array();
				var cntnrIdToEnclosureMap = new Array();
				
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
							"parentNode": _parentNode
						};
					
					angular.forEach(enclosure.childContainer, function(childCntnr) {
						cntnrIdToEnclosureMap[childCntnr.id] = enclosureNd;
					});
					
					enclosureNodes.push(enclosureNd);
				});
				
				angular.forEach(_containerList, function(cntnr) {

					// ignore refData containers
					if (cntnr.refData) {
						return;
					}
				
					var cntnrType = cntnr.single ? "container-instance" : "container";
					var elemIdentity = cntnr.single ? null : _elementIdentity;
					
					// add container listing node parent
					var indxItem = {
						"id" : cntnr.qualifiedId,
						"qualifiedId" : cntnr.qualifiedId,
						"label" : cntnr.label,
						"elementIdentity" : elemIdentity,
						"parentIdentity" : _elementIdentity,
						"children" : [],
						"containerDef": cntnr,
						"type": cntnrType,
						"uiClass": "eb-indx-container"
					};
				
					var enclosureOfChildCntnr = cntnrIdToEnclosureMap[cntnr.id];
					
					if (!isNullOrUndefined(enclosureOfChildCntnr)) {
						indxItem.parentNode = enclosureOfChildCntnr;
						enclosureOfChildCntnr.children.push(indxItem);
						
					} else {
						indxItem.parentNode = _parentNode;
						_childrenList.push(indxItem);
					}
					
					var childLabelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, cntnr.qualifiedId);
					
					if ((!isNullOrUndefined(childLabelAttrId) || cntnr.single) && !ignoreInstanceLoad) {
						// add container data instance node
						ContentDataService.getContentData(templateId, indxItem.qualifiedId, _elementIdentity, function(data) {
							
							angular.forEach(data, function(dataItem) {
								addInstanceDataChild(indxItem, cntnr, dataItem);
							});
							
						}, function(data) {
							Notification.error({message: "Error retrieving content data for template [" 
									+ templateId + ", " + indxItem.qualifiedId + "]", delay: enablix.errorMsgShowTime});
							//alert("Error retrieving content data for template [" + templateId + ", " + indxItem.qualifiedId + "]");
						});
					}
				});
				
				angular.forEach(enclosureNodes, function(encNode) {
					_childrenList.push(encNode);
				});
			};
			
			var addInstanceDataChild = function(_indxDataParent, _containerDef, dataItem) {

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
						
						_indxDataParent.children.push(indxDataItem);
					}
				}
				
				if (!isNullOrUndefined(indxDataItem)) {
					// recursively build the child elements
					buildContentIndexFromContainer(indxDataItem.children, _containerDef.container, 
							dataItem.identity, _containerDef.qualifiedId, indxDataItem);
				}

				return indxDataItem;
			} 
			
			var getContentIndexData = function(_templateId, _onSuccess, _onError) {
				templateId = _templateId;
				var params = {"templateId": _templateId};
			    RESTService.getForData("fetchRootContainers", params, contentIndexTransformer, _onSuccess, _onError);
			};
			
			var updateNodeData = function(_treeNode, _data) {
				if (_treeNode.containerDef && !_treeNode.containerDef.single
						&& _treeNode.elementIdentity == _data.identity) {
					_treeNode.label = ContentUtil.resolveContainerInstanceLabel(_treeNode.containerDef, _data);
				}
			};
			
			var deleteInstanceChildNode = function(_parentNode, childIdentity) {
				
				var childrenList = _parentNode.children;
				
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
			
			return {
				getContentIndexData: getContentIndexData,
				addInstanceDataChild: addInstanceDataChild,
				updateNodeData: updateNodeData,
				deleteInstanceChildNode: deleteInstanceChildNode,
				getPortalIndexForContainer: getPortalIndexForContainer
			};
	 	}
	]);