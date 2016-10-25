enablix.studioApp.factory('ContentApprovalService', 
	[
	 		    'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
	 		    	
	    	var submitContent = function(_contentQId, _parentRecordIdentity, _notes, _data, _onSuccess, _onError) {
	 			
	 			var contentDetail = {
	 					"contentQId": _contentQId,
	 					"parentIdentity": _parentRecordIdentity,
	 					"notes": _notes,
	 					"data": _data
	 			};
	 			
	 			RESTService.postForData("submitContentSuggestion", {}, contentDetail, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		var approveContent = function(_contentRequestIdentity, _notes, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity,
	 					"notes": _notes
	 			};
	 			
	 			RESTService.postForData("approveContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						
	 						Notification.primary("Content approved!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						
	 						Notification.error({message: "Error approving content.", delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 						
	 					}, null);
	 			
	 		};
	 		
	 		var rejectContent = function(_contentRequestIdentity, _notes, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity,
	 					"notes": _notes
	 			};
	 			
	 			RESTService.postForData("rejectContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						Notification.primary("Content rejected!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						Notification.error({message: "Error rejecting content.", delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 					}, null);
	 			
	 		};
	 		
	 		var editContent = function(_contentRequestIdentity, _contentQId, _parentRecordIdentity, _notes, _data, _onSuccess, _onError) {
	 			
	 			var contentDetail = {
	 					"identity": _contentRequestIdentity,
	 					"contentQId": _contentQId,
	 					"parentIdentity": _parentRecordIdentity,
	 					"notes": _notes,
	 					"data": _data
	 			};
	 			
	 			RESTService.postForData("editContentSuggestion", {}, contentDetail, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		return {
	 			submitContent: submitContent,
	 			approveContent: approveContent,
	 			rejectContent: rejectContent,
	 			editContent: editContent
	 		};
	 	}
	 ]);