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
	 		
	 		var editContent = function(_refObjectIdentity, _contentQId, _parentRecordIdentity, _notes, _data, _onSuccess, _onError) {
	 			
	 			var contentDetail = {
	 					"identity": _refObjectIdentity,
	 					"contentQId": _contentQId,
	 					"parentIdentity": _parentRecordIdentity,
	 					"notes": _notes,
	 					"data": _data
	 			};
	 			
	 			RESTService.postForData("editContentSuggestion", {}, contentDetail, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		var getContent = function(_refObjectIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"refObjectIdentity": _refObjectIdentity
	 			};

	 			RESTService.getForData("getContentSuggestion", params, null, _onSuccess, _onError);
	 			
	 		};
	 		
	 		var isActionAllowed = function(actionName, record) {
	 			
	 			if (!isNullOrUndefined(record)) {
				
	 				var nextActions = record.currentState.nextActions;
					if (nextActions) {
						for (var i = 0; i < nextActions.length; i++) {
							var nextAction =  nextActions[i];
							if (nextAction.actionName == actionName) {
								return true;
							}
						}
					}
	 			}
				return false;
			};
	 		
	 		return {
	 			submitContent: submitContent,
	 			approveContent: approveContent,
	 			rejectContent: rejectContent,
	 			editContent: editContent,
	 			getContent: getContent,
	 			isActionAllowed: isActionAllowed
	 		};
	 	}
	 ]);