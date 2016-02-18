enablix.studioApp.factory('QIdUtil', 
	[
	 	'RESTService', 'ContentTemplateService',
	 	function(RESTService, ContentTemplateService) {
	 		
	 		var getParentQId = function(_qId) {
	 			var parentQId = _qId;
	 			var lastIndx = _qId.lastIndexOf(".");
	 			if (lastIndx > 0) {
	 				parentQId = _qId.substr(0, lastIndx);
	 			}
	 			return parentQId;
	 		};
				
	 		return {
	 			getParentQId : getParentQId
	 		};
	 	}
	 ]);