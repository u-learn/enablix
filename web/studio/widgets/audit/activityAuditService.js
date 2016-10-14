enablix.studioApp.factory('ActivityAuditService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 				
	 		var auditContentAccess = function(_contentQId, _contentIdentity, _contentTitle, _onSuccess, _onError) {
	 			
	 			var _data = {
	 					"containerQId" : _contentQId,
	 					"instanceIdentity" : _contentIdentity,
	 					"itemTitle" : _contentTitle
	 			};
	 			
	 			RESTService.postForData("auditContentAccess", {}, _data, null, _onSuccess, _onError, null);
	 		};
	 		
	 		return {
	 			auditContentAccess: auditContentAccess
	 		};
	 	}
	 ]);