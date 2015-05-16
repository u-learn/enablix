enablix.studioApp.factory('PortalContainerIndexService', 
	[
	 	'RESTService', 'ContentIndexService', 'ContentTemplateService',
	 	function(RESTService, ContentIndexService, ContentTemplateService) {
	 		
	 		var getIndexList = function(_containerQId) {
	 			
	 			var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);

	 			var indexList = [];
				
				var abtIndexItem = {
						"id" : containerDef.id,
						"qualifiedId" : _containerQId,
						"label" : "About",
						"children" : [],
						"containerDef": containerDef,
						"type": "single"
					};
				
				indexList.push(abtIndexItem);
				
				angular.forEach(containerDef.container, function(subCntnr) {
					var itemType = subCntnr.single ? "single" : "multi";
					var indxItem = {
							"id" : subCntnr.id,
							"qualifiedId" : subCntnr.qualifiedId,
							"label" : subCntnr.label,
							"children" : [],
							"containerDef": containerDef,
							"type": "single"
						};
					indexList.push(indxItem);
				});
				
				return indexList;
	 		}
	 		
	 		return {
	 			getIndexList: getIndexList
	 		};
	 	}
	 ]);