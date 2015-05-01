enablix.studioApp.factory('ContentIndexService', 
	[	'RESTService', 'ContentDataService', 'ContentTemplateService',
	 	function(RESTService, ContentDataService, ContentTemplateService) {
		
			var templateId = "";
			
			var contentIndexTransformer = function(data) {
				
				var indexData = [];

				buildContentIndexFromContainer(indexData, data, null);
				
				return indexData;
			};
			
			var buildContentIndexFromContainer = function(_indxDataParent, _containerList, _elementIdentity) {
			
				angular.forEach(_containerList, function(cntnr) {

					// ignore refData containers
					if (cntnr.refData) {
						return;
					}
				
					var cntnrType = cntnr.single ? "container-instance" : "container";
					var elemIdentity = cntnr.single ? null : _elementIdentity;
					
					// add container listing node parent
					var indxItem = {
						"id" : cntnr.id,
						"qualifiedId" : cntnr.qualifiedId,
						"label" : cntnr.label,
						"elementIdentity" : elemIdentity,
						"parentIdentity" : _elementIdentity,
						"children" : [],
						"containerDef": cntnr,
						"type": cntnrType,
						"uiClass": "eb-indx-container"
					};
				
					_indxDataParent.push(indxItem);
					
					// add container data instance node
					ContentDataService.getContentData(templateId, indxItem.qualifiedId, _elementIdentity, function(data) {
						
						angular.forEach(data, function(dataItem) {
							addInstanceDataChild(indxItem, cntnr, dataItem);
						});
						
					}, function(data) {
						alert("Error retrieving content data for template [" + templateId + ", " + indxItem.qualifiedId + "]");
					});
					
				});
			};
			
			var addInstanceDataChild = function(_indxDataParent, _containerDef, dataItem) {

				var indxDataItem = null;
				
				if (_containerDef.single) {
					_indxDataParent.elementIdentity = dataItem.identity;
					_indxDataParent.type = "instance";
					indxDataItem = _indxDataParent;
					
				} else {
					
					var nodeLabel = resolveContainerInstanceLabel(_containerDef, dataItem); 
					
					var indxDataItem = {
						"id" : dataItem.identity,
						"qualifiedId" : _containerDef.qualifiedId,
						"label" : nodeLabel,
						"elementIdentity" : dataItem.identity,
						"children" : [],
						"containerDef": _containerDef,
						"type": "instance",
						"uiClass": "eb-indx-instance"
					};
					
					_indxDataParent.children.push(indxDataItem);
				}
				
				// recursively build the child elements
				buildContentIndexFromContainer(indxDataItem.children, _containerDef.container, dataItem.identity);

				return indxDataItem;
			} 
			
			var resolveContainerInstanceLabel = function(_containerDef, _instanceData) {
				
				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerDef.qualifiedId);

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

			var getContentIndexData = function(_templateId, _onSuccess, _onError) {
				templateId = _templateId;
				var params = {"templateId": _templateId};
			    RESTService.getForData("fetchRootContainers", params, contentIndexTransformer, _onSuccess, _onError);
			};
			
			var updateNodeData = function(_treeNode, _data) {
				if (_treeNode.containerDef && !_treeNode.containerDef.single) {
					_treeNode.label = resolveContainerInstanceLabel(_treeNode.containerDef, _data);
				}
			};
			
			return {
				getContentIndexData: getContentIndexData,
				addInstanceDataChild: addInstanceDataChild,
				updateNodeData: updateNodeData
			};
	 	}
	]);