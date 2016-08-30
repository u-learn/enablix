enablix.studioApp.factory('ActivityAuditService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 				
	 		var auditContentAccess = function(_contentQId, _contentIdentity, _onSuccess, _onError) {
	 			
	 			var _data = {
	 					"containerQId" : _contentQId,
	 					"instanceIdentity" : _contentIdentity
	 			};
	 			
	 			RESTService.postForData("auditContentAccess", {}, _data, null, _onSuccess, _onError, null);
	 		};
	 		
	 		return {
	 			auditContentAccess: auditContentAccess
	 		};
	 	}
	 ]);