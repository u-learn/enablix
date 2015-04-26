enablix.studioApp.factory('RefdataIndexService', 
	[	        'RESTService', 'ContentTemplateService', 'StateUpdateService',
	 	function(RESTService,   ContentTemplateService,   StateUpdateService) {
		
			var currContainerQId = "";
		
			var buildIndexData = function(_containerList) {
				
				var indexData = [];
				
				angular.forEach(_containerList, function(cntnr) {
					
					if (cntnr.refData) {
					
						var indxItem = {
							id: cntnr.id,
							label: cntnr.label,
							containerDef: cntnr
						};
						
						indexData.push(indxItem);
					}
				});
				
				return indexData;
			};
		
			var getIndexData = function() {
				var indexData = buildIndexData(enablix.template.dataDefinition.container);
				return indexData;
			};
			
			var setCurrentContainerQId = function(_containerQId) {
				currContainerQId = _containerQId;
			};
			
			var getCurrentContainerQId = function() {
				return currContainerQId;
			};
			
			return {
				getIndexData: getIndexData,
				setCurrentContainerQId: setCurrentContainerQId,
				getCurrentContainerQId: getCurrentContainerQId
			};
	 	}
	]);