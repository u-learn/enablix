enablix.studioApp.factory('ContentApprovalService', 
	[
	 		    'RESTService', 'Notification', 'DataSearchService', 'ActionNotesWindow', 'ConfirmationModalWindow', 'AuthorizationService', '$location',
	 	function(RESTService,   Notification,   DataSearchService,   ActionNotesWindow,   ConfirmationModalWindow,   AuthorizationService,   $location) {

	 		var DOMAIN_TYPE = "com.enablix.content.approval.model.ContentApproval";
	 		
	 		var ACTION_SAVE_DRAFT = "SAVE_DRAFT";
	 		var ACTION_REJECT = "REJECT";
	 		var ACTION_APPROVE = "APPROVE";
	 		var ACTION_EDIT = "EDIT";
	 		var ACTION_VIEW_DETAILS = "VIEW_DETAILS";
	 		var ACTION_WITHDRAW = "WITHDRAW";
	 		var ACTION_PUBLISH = "PUBLISH";
	 		var ACTION_DISCARD = "DISCARD";
	 		
	 		var STATE_DRAFT = "DRAFT";
	 		var STATE_PUBLISHED = "PUBLISHED";
	 		var STATE_PENDING_APPROVAL = "PENDING_APPROVAL";
	 		var STATE_WITHDRAWN = "WITHDRAWN";
	 		var STATE_APPROVED = "APPROVED";
	 		var STATE_REJECTED = "REJECTED";
	 		
	 		var PERM_SUBMIT_CONTENT_FROM_PORTAL = "VIEW_STUDIO";
	 		
	 		var FILTER_METADATA = {
	 				"contentIdentity" : {
	 					"field" : "objectRef.data.identity",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				},
	 				"contentQId" : {
	 					"field" : "objectRef.contentQId",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				},
	 				"requestState" : {
	 					"field" : "currentState.stateName",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				},
	 				"requestStateNot" : {
	 					"field" : "currentState.stateName",
	 					"operator" : "NOT_EQ",
	 					"dataType" : "STRING"
	 				},
	 				"requestStateNotIn" : {
	 					"field" : "currentState.stateName",
	 					"operator" : "NOT_IN",
	 					"dataType" : "STRING"
	 				},
	 				"refObjectNE" : {
	 					"field" : "objectRef.identity",
	 					"operator" : "NOT_EQ",
	 					"dataType" : "STRING"
	 				},
	 				"createdBy" : {
	 					"field" : "createdBy",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				}
	 		};
	 		    	
	 		var stateActionMap = null;
	 		
	 		var init = function() {
	 			
	 			if (stateActionMap != null) {
	 				return stateActionMap;
	 			}
	 			
	 			return RESTService.getForData("getContentWFStateActionMap", {}, null, function(data) {
			 				stateActionMap = data;
			 			}, function(error) {
			 				Notification.error({message: "Error init data for content workflow.", delay: enablix.errorMsgShowTime});
			 			});
	 		};
	 		
	 		
	 		var submitContent = function(_contentQId, _parentRecordIdentity, _notes, _data, _onSuccess, _onError, _isSaveDraft) {
	 			
	    		var addRequest = isNullOrUndefined(_data.identity);
	    		var serviceUrlKey = _isSaveDraft ? "saveContentDraft" : "submitContentSuggestion";
	    		
	 			var contentDetail = {
	 					"contentQId": _contentQId,
	 					"parentIdentity": _parentRecordIdentity,
	 					"notes": _notes,
	 					"data": _data,
	 					"addRequest": addRequest
	 			};
	 			
	 			var headers = getUrlParameters($location);
	 			RESTService.postForData(serviceUrlKey, {}, contentDetail, null, _onSuccess, _onError, null, headers);
	 			
	 		};
	 		
	 		var publishContent = function(_refObjectIdentity, _contentQId, _parentRecordIdentity, _notes, _data, _onSuccess, _onError) {
	 			
	 			var contentDetail = {
	 					"identity": _refObjectIdentity,
	 					"contentQId": _contentQId,
	 					"parentIdentity": _parentRecordIdentity,
	 					"notes": _notes,
	 					"data": _data
	 			};
	 			
	 			
	 			RESTService.postForData("publishContentSuggestion", {}, contentDetail, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		var approveContent = function(_contentRequestIdentity, _notes, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity,
	 					"notes": _notes
	 			};
	 			
	 			var headers = getUrlParameters($location);
	 			RESTService.postForData("approveContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						
	 						Notification.primary("Content approved!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						
	 						Notification.error({message: "Error approving content : " + error.message, delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 						
	 					}, null), headers;
	 			
	 		};
	 		
	 		var discardContent = function(_contentRequestIdentity, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity
	 			};
	 			
	 			var headers = getUrlParameters($location);
	 			RESTService.postForData("discardContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						
	 						Notification.primary("Content deleted!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						
	 						Notification.error({message: "Error deleting content : " + error.message, delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 						
	 					}, null), headers;
	 			
	 		};
	 		
	 		var rejectContent = function(_contentRequestIdentity, _notes, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity,
	 					"notes": _notes
	 			};
	 			
	 			var headers = getUrlParameters($location);
	 			RESTService.postForData("rejectContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						Notification.primary("Content rejected!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						Notification.error({message: "Error rejecting content : " + error.message, delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 					}, null, headers);
	 			
	 		};
	 		
	 		var withdrawContent = function(_contentRequestIdentity, _notes, _onSuccess, _onError) {
	 			
	 			var actionInput = {
	 					"identity": _contentRequestIdentity,
	 					"notes": _notes
	 			};
	 			
	 			var headers = getUrlParameters($location);
	 			RESTService.postForData("withdrawContentSuggestion", {}, actionInput, null, 
	 					function(data) {
	 						Notification.primary("Content request withdrawn!");
	 						if (_onSuccess) {
	 							_onSuccess(data);
	 						}
	 						
	 					}, function(error) {
	 						Notification.error({message: "Error withdrawing content : " + error.message, delay: enablix.errorMsgShowTime});
	 						if (_onError) {
	 							_onError(error);
	 						}
	 					}, null, headers);
	 			
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
	 		
	 		var getContent = function(_refObjectIdentity, _onSuccess, _onError, _auditAsViewAction) {
	 			
	 			var params = {
 					"refObjectIdentity": _refObjectIdentity
	 			};
	 			
	 			var headers = null;
	 			
	 			if (_auditAsViewAction) {
	 			
	 				var urlParams = getUrlParameters($location);
	 				if (isNullOrUndefined(urlParams.atChannel)) {
	 					urlParams.atChannel = 'WEB';
	 				}
	 				
	 				headers = urlParams;
	 			}

	 			RESTService.getForData("getContentSuggestion", params, null, _onSuccess, _onError, headers);
	 			
	 		};
	 		
	 		var isActionAllowed = function(actionName, record) {
	 			
	 			if ((actionName == ACTION_WITHDRAW || actionName == ACTION_DISCARD)
						&& AuthorizationService.getCurrentUser().userId != record.createdBy) {
					return false;
				}
	 			
	 			if (!isNullOrUndefined(record)) {
				
	 				var nextActions = stateActionMap[record.currentState.stateName];
	 				
					if (nextActions) {
						
						for (var i = 0; i < nextActions.length; i++) {
						
							var nextAction =  nextActions[i];
							if (nextAction.actionName == actionName) {
								
								if (actionName == ACTION_EDIT 
										&& AuthorizationService.getCurrentUser().userId == record.createdBy) {
									return true;
								}
								
								if (!AuthorizationService.userHasAllPermissions(nextAction.requiredPermissions)) {
									return false;
								}
									
								return true;
							}
						}
					}
	 			}
	 			
				return false;
			};
			
			
			var getContentRequests = function(_filters, _pagination, _onSuccess, _onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError);
			};
			
			
			var getPendingRequestsForContent = function(_contentIdentity, _onSuccess, _onError) {
				
				var filters = {
						"contentIdentity": _contentIdentity,
						"requestState" : STATE_PENDING_APPROVAL
				};
				
				getContentRequests(filters, null, _onSuccess, _onError)
			};
			
			var getOtherPendingRequestsForContent = function(_contentIdentity, _refObjectIdentity, _onSuccess, _onError) {
				
				var filters = {
						"contentIdentity": _contentIdentity,
						"requestState" : STATE_PENDING_APPROVAL,
						"refObjectNE" : _refObjectIdentity
				};
				
				getContentRequests(filters, null, _onSuccess, _onError)
			};
			
			var initApproveAction = function(_approvalRecord, _onActionCompletion) {
				
				var contentIdentity = _approvalRecord.objectRef.data.identity;
				
				if (!isNullOrUndefined(contentIdentity)) {
					
					getOtherPendingRequestsForContent(contentIdentity, _approvalRecord.objectRef.identity, 
						function(pendingDataPage) {
							
							if (pendingDataPage.content.length > 0) {
								
								var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
										"There are other existing edit requests for this content which will be rejected. Do you want to continue?", 
										"Proceed", "Cancel");
								
								confirmModal.result.then(function(confirmed) {
									if (confirmed) {
										proceedWithApproval(_approvalRecord, _onActionCompletion, pendingDataPage.content);
									}
								});
								
							} else {
								proceedWithApproval(_approvalRecord, _onActionCompletion, []);
							}
							
						}, function(errorData) {
							Notification.error({message: "Error process action.", delay: enablix.errorMsgShowTime});
						});
					
				} else {
					proceedWithApproval(_approvalRecord, _onActionCompletion);
				}
				
			};
			
			var proceedWithApproval = function(_approvalRecord, _onActionCompletion, _pendingRecordToBeRejected) {
				
				var modalInstance = ActionNotesWindow.showWindow("Approve Content", "Approval notes");
				
				modalInstance.result.then(function(notes) {
					
					angular.forEach(_pendingRecordToBeRejected, function(record) {
						rejectContent(record.objectRef.identity, "", function(data) {
							// do nothing
						});
					});
					
					approveContent(_approvalRecord.objectRef.identity, notes, _onActionCompletion);
				});
			};																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
			
			var initRejectAction = function(_approvalRecord, _onActionCompletion) {
				var modalInstance = ActionNotesWindow.showWindow("Reject Content", "Rejection notes");
				modalInstance.result.then(function(notes) {
					rejectContent(_approvalRecord.objectRef.identity, notes, _onActionCompletion);
				});
			};
			
			var initWithdrawAction = function(_approvalRecord, _onActionCompletion) {
				var modalInstance = ActionNotesWindow.showWindow("Withdraw Content", "Withdrawal notes");
				modalInstance.result.then(function(notes) {
					withdrawContent(_approvalRecord.objectRef.identity, notes, _onActionCompletion);
				});
			};
			
			var initDiscardAction = function(_recordObjectRefIdentity, _onActionCompletion) {
				var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
						"Do you want to continue with discard action?", 
						"Proceed", "Cancel");
				confirmModal.result.then(function(notes) {
					discardContent(_recordObjectRefIdentity, _onActionCompletion);
				});
			};
			
			
			var isApprovalWFRequired = function() {
				return !AuthorizationService.userHasPermission(PERM_SUBMIT_CONTENT_FROM_PORTAL);
			};
			
	 		return {
	 			init: init,
	 			submitContent: submitContent,
	 			publishContent: publishContent,
	 			approveContent: approveContent,
	 			discardContent: discardContent,
	 			rejectContent: rejectContent,
	 			withdrawContent: withdrawContent,
	 			editContent: editContent,
	 			getContent: getContent,
	 			isActionAllowed: isActionAllowed,
	 			getContentRequests: getContentRequests,
	 			getPendingRequestsForContent: getPendingRequestsForContent,
	 			getOtherPendingRequestsForContent: getOtherPendingRequestsForContent, 
	 			initApproveAction: initApproveAction,
	 			initRejectAction: initRejectAction,
	 			initWithdrawAction: initWithdrawAction,
	 			initDiscardAction: initDiscardAction,
	 			isApprovalWFRequired: isApprovalWFRequired,
	 			
	 			actionReject: function() { return ACTION_REJECT; },
	 			actionApprove: function() { return ACTION_APPROVE; },
	 			actionEdit: function() { return ACTION_EDIT; },
	 			actionWithdraw: function() { return ACTION_WITHDRAW; },
	 			actionSaveDraft: function() { return ACTION_SAVE_DRAFT; },
	 			actionViewDetails: function() { return ACTION_VIEW_DETAILS; },
	 			actionPublish: function() { return ACTION_PUBLISH; },
	 			actionDiscard: function() { return ACTION_DISCARD; },
	 			
	 			stateWithdrawn: function() { return STATE_WITHDRAWN; },
	 			stateRejected: function() { return STATE_REJECTED; },
	 			stateApproved: function() { return STATE_APPROVED; },
	 			stateDraft: function() { return STATE_DRAFT; },
	 			statePublished: function() { return STATE_PUBLISHED; }
	 			
	 		};
	 	}
	 ]);