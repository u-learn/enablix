enablix.studioApp.factory('ContentIndexService', 
	[	'RESTService', 'ContentDataService',
	 	function(RESTService, ContentDataService) {
		
			var templateId = "";
			
			var contentIndexTransformer = function(data) {
				
				var indexData = [];

				buildContentIndexFromContainer(indexData, data, null);
				
				return indexData;
			};
			
			var buildContentIndexFromContainer = function(_indxDataParent, _containerList, _parentIdentity) {
				
				angular.forEach(_containerList, function(cntnr) {
				
					var indxItem = {
						"id" : cntnr.id,
						"qualifiedId" : cntnr.qualifiedId,
						"label" : cntnr.label,
						"parentIdentity" : _parentIdentity,
						"children" : []
					};
				
					_indxDataParent.push(indxItem);
					
					ContentDataService.getContentData(templateId, indxItem.qualifiedId, _parentIdentity, function(data) {
						
						angular.forEach(data, function(dataItem) {
							
							var indxDataItem = {
								"id" : dataItem.identity,
								"qualifiedId" : cntnr.qualifiedId,
								"label" : dataItem['name'],
								"parentIdentity" : dataItem.identity,
								"children" : []
							};
							
							indxItem.children.push(indxDataItem);
							
							// recursively build the child elements
							buildContentIndexFromContainer(indxDataItem.children, cntnr.container, dataItem.identity);

						});
						
					}, function(data) {
						alert("Error retrieving content data for template [" + templateId + ", " + indxItem.qualifiedId + "]");
					});
					
				});
			};
			
			var getContentIndexData = function(_templateId, _onSuccess, _onError) {
				templateId = _templateId;
				var params = {"templateId": _templateId};
			    RESTService.getForData("fetchRootContainers", params, contentIndexTransformer, _onSuccess, _onError);
			};
			
			return {
				getContentIndexData: getContentIndexData
			};
	 	}
	]);