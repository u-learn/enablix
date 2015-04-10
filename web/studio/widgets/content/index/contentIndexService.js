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
				
					// add container listing node parent
					var indxItem = {
						"id" : cntnr.id,
						"qualifiedId" : cntnr.qualifiedId,
						"label" : cntnr.label,
						"elementIdentity" : _elementIdentity,
						"children" : [],
						"containerDef": cntnr,
						"type": "container"
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

				var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerDef.qualifiedId); 
				
				var indxDataItem = {
					"id" : dataItem.identity,
					"qualifiedId" : _containerDef.qualifiedId,
					"label" : dataItem[labelAttrId],
					"elementIdentity" : dataItem.identity,
					"children" : [],
					"containerDef": _containerDef,
					"type": "instance"
				};
				
				_indxDataParent.children.push(indxDataItem);
				
				// recursively build the child elements
				buildContentIndexFromContainer(indxDataItem.children, _containerDef.container, dataItem.identity);

			} 

			var getContentIndexData = function(_templateId, _onSuccess, _onError) {
				templateId = _templateId;
				var params = {"templateId": _templateId};
			    RESTService.getForData("fetchRootContainers", params, contentIndexTransformer, _onSuccess, _onError);
			};
			
			return {
				getContentIndexData: getContentIndexData,
				addInstanceDataChild: addInstanceDataChild 
			};
	 	}
	]);