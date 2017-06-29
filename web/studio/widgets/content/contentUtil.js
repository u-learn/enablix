enablix.studioApp.factory('ContentUtil', 
	[
	 			'RESTService', 'ContentTemplateService', 'DocPreviewService', 'StateUpdateService', 'ContentTemplateService',
	 	function(RESTService,   ContentTemplateService,   DocPreviewService,   StateUpdateService,   ContentTemplateService) {
	 		
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
				
				if (_dataRecord && _dataRecord.__associations && _dataRecord.__associations.parent) {
					_dataRecord.parentIdentity = _dataRecord.__associations.parent.recordIdentity;
				}
				
				for (var i = 0; i < _containerDef.contentItem.length; i++) {
					
					var item = _containerDef.contentItem[i];
					
					if (item.type == 'DOC') {
						
						var docInstance = _dataRecord[item.id];
						if (docInstance && docInstance.identity) {
							_dataRecord.downloadDocIdentity = docInstance.identity;
							_dataRecord.docMetadata = docInstance;
							
							var previewHandler = DocPreviewService.getPreviewHandler(docInstance);
							if (previewHandler != null) {
								_dataRecord.docThumbnailUrl = previewHandler.smallThumbnailUrl(docInstance);
							}
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
						
					} else if (item.type == 'RICH_TEXT') {
						
						if (isNullOrUndefined(_dataRecord[item.id + "_rt"])) {
							_dataRecord[item.id + "_rt"] = _dataRecord[item.id];
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
					
					if (!hiddenContentItemIds.contains(containerAttr.id)
							&& containerAttr.type != "CONTENT_STACK") {
						
						var header = {
							"key" : containerAttr.id,
							"desc" : containerAttr.label,
							"dataType" : containerAttr.type,
							"typeDef" : containerAttr
						};
						
						switch (containerAttr.type) {
							case "DOC_TYPE":
								header.sortProperty = containerAttr.id + ".name";
								break;
								
							case "BOUNDED":
								header.sortProperty = containerAttr.id + ".label";
								break;
								
							case "TEXT":
							case "DATE_TIME":
							case "NUMERIC":
								header.sortProperty = containerAttr.id;
								break;
						}
						
						if (containerAttr.type != "BOUNDED") {
							header.sortProperty = containerAttr.id;
						}
						
						listHeaders.push(header);
					}
				});
				
				return listHeaders;
			};
			
			var getContentLabelValue = function(_containerDef, _dataRecord) {
				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerDef.qualifiedId);
				return _dataRecord[labelAttrId];
			};
			
			var resolveAndAddTitle = function(_containerDef, _dataList) {
				
				if (ContentTemplateService.isLinkedContainer(_containerDef)) {
					_containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerDef.linkContainerQId)
				}
				
				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerDef.qualifiedId);
				angular.forEach(_dataList, function(dataRec) {
					dataRec._title = dataRec[labelAttrId];
				});
			};
			
			var groupContentRecordsByQId = function(_contentRecords, _addToMap) {
				
				// create a map of contentQId (containerQId) -> content group
				// the content group will group all records with same containerQId
				var contentGroupMap = _addToMap;
				
				angular.forEach(_contentRecords, function(contentDataRecord) {
					
					var contentQId = contentDataRecord.containerQId;
					var contentGroup = contentGroupMap[contentQId];
				
					if (isNullOrUndefined(contentGroup)) {
						
						var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, contentQId);
						
						contentGroup = {
							qualifiedId: contentQId,
							label: containerDef.label,
							containerDef: containerDef,
							records: []
						};
						
						contentGroupMap[contentQId] = contentGroup;
					}
					
					contentGroup.records.push(contentDataRecord.record);
				});
				
				return contentGroupMap;
			}
			
			var getContentDetailHeaders = function(_containerDef, _excludeTitleAttr, _excludeRefListAttr) {
				
				var headers = [];
				var containerLabelAttrId = null;
				
				if (_excludeTitleAttr) {
					// do not populate when we have to show label so that it does not match attr id
					// when matching to ignore including label attr in list of displayable attributes
					containerLabelAttrId = ContentTemplateService.getContainerLabelAttrId(
												enablix.template, _containerDef.qualifiedId);
				}
				
				angular.forEach(_containerDef.contentItem, function(containerAttr) {
					
					if (containerAttr.id != containerLabelAttrId
							&& containerAttr.type != 'CONTENT_STACK'
							&& (!_excludeRefListAttr || containerAttr.type !== 'BOUNDED')) {
						
						var header = {
							"id": containerAttr.qualifiedId,
							"key" : containerAttr.id,
							"label" : containerAttr.label,
							"dataType" : containerAttr.type,
							"typeDef" : containerAttr
						};
						
						headers.push(header);
					}
					
				});
				
				return headers;
			}
			
			var hasData2 = function(_data) {
				return !isNullOrUndefined(_data) && (_data.length != 0); //!isArrayAndEmpty(_data) && !isStringAndEmpty(_data);
			}
			
			var hasData = function(_data) {
				return !isNullOrUndefined(_data) && !isArrayAndEmpty(_data) && !isStringAndEmpty(_data);
			}
			
			var isContentFieldRenderable = function(_contentDef, _data) {
				return hasData(_data);
			}
			
			var navToTileBasedSubContainerList = function(
	 				_containerQId, _subContainerQId, _parentIdentity, _subContainerDef, _searchTerm) {
	 			
	 			var urlParams = {};
	 			
	 			if (!_subContainerDef) {
	 				_subContainerDef = ContentTemplateService.getConcreteContainerDefinition(enablix.template, _subContainerQId);
	 			}
	 			
				var refContentItem = ContentTemplateService.getContentItemByRefDatastore(_subContainerDef, _containerQId);
				if (refContentItem) {
					urlParams["sf_" + refContentItem.id] = _parentIdentity;
				}
				
				StateUpdateService.goToPortalContainerList(_subContainerQId, "tile", urlParams, _searchTerm);
				
	 		}
			
	 		return {
	 			resolveContainerInstanceLabel: resolveContainerInstanceLabel,
	 			resolveContainerInstancePortalLabel: resolveContainerInstancePortalLabel,
	 			decorateData: decorateData,
	 			getContentListHeaders: getContentListHeaders,
	 			getContentLabelValue: getContentLabelValue,
	 			resolveAndAddTitle: resolveAndAddTitle,
	 			groupContentRecordsByQId: groupContentRecordsByQId,
	 			getContentDetailHeaders: getContentDetailHeaders,
	 			isContentFieldRenderable: isContentFieldRenderable,
	 			navToTileBasedSubContainerList: navToTileBasedSubContainerList
	 		};
	 		
	 		
	 	}
	 ]);