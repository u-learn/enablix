enablix.studioApp.controller('ContentKitAddEditCtrl', 
			['$scope', '$state', '$stateParams', '$filter', 'ContentKitService', 'ContentTemplateService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   $filter,   ContentKitService,   ContentTemplateService,   StateUpdateService,   Notification) {
	
		var contentKitIdentity = $stateParams.contentKitIdentity;
		$scope.editOper = $state.includes("contentkit.edit");
		$scope.addOper = $state.includes("contentkit.add");
		$scope.viewOper = $state.includes("contentkit.detail");
		
		$scope.heading = "Add Content Kit";
		$scope.contentKit = {
			contentList: []
		};
		
		$scope.selectedContent = [];
		$scope.linkedKitRecords = [];
		
		if ($scope.viewOper) {
			$scope.heading = "Content Kit Detail";
		} else if ($scope.editOper) {
			$scope.heading = "Edit Content Kit";
		}
		
		if (!isNullOrUndefined(contentKitIdentity)) {
			
			ContentKitService.getContentKitDetail(contentKitIdentity, 
			
				function(data) {
					
					$scope.contentKit = data.contentKit;
					$scope.linkedKitRecords = data.linkedKits || [];

					angular.forEach($scope.contentKit.contentList, function(selectedContentItem) {
						
						var containerLabel = ContentTemplateService.getContainerLabel(selectedContentItem.containerQId);
						selectedContentItem.__containerLabel = containerLabel;
						
						$scope.selectedContent.push({
							qualifiedId: selectedContentItem.containerQId,
							identity: selectedContentItem.instanceIdentity,
							label: selectedContentItem.title,
							containerLabel: ContentTemplateService.getContainerLabel(selectedContentItem.containerQId)
						});
						
					});
				}, 
				function(resp) {
					Notification.error({message: "Error retrieving content kit data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.contentTableHeaders =
			 [{
				 desc: "Type",
				 valueKey: "__containerLabel",
				 sortProperty: "__containerLabel"
			 },
		     {
		    	 desc: "Title",
		    	 valueKey: "title",
		    	 sortProperty: "title"
		     }];
		
		$scope.deleteContentRecord = function(_record, $event, _index) {
			if (_index >= 0) {
				$scope.contentKit.contentList.splice(_index, 1);
				$scope.selectedContent.splice(_index, 1);
			}
		}
		
		$scope.deleteLinkedContentKit = function(_record, $event, _index) {
			if (_index >= 0) {
				$scope.linkedKitRecords.splice(_index, 1);
			}
		}
		
		if (!$scope.viewOper) {
			
			$scope.contentTableRecordActions = 
				[{
					actionName: "Remove",
					tooltip: "Delete",
					iconClass: "fa fa-times",
					tableCellClass: "remove",
					actionCallbackFn: $scope.deleteContentRecord
				}];
			
			$scope.linkedKitTableRecordActions = 
				[{
					actionName: "Remove",
					tooltip: "Delete",
					iconClass: "fa fa-times",
					tableCellClass: "remove",
					actionCallbackFn: $scope.deleteLinkedContentKit
				}];
		}
			
		
		$scope.updateContentKit = function() {
			
			$scope.contentKit.linkedKits = [];
			angular.forEach($scope.linkedKitRecords, function(linkedKit) {
				$scope.contentKit.linkedKits.push(linkedKit.identity);
			});
			
			ContentKitService.saveContentKit($scope.contentKit, 
				function(result) {
					Notification.primary("Saved successfully!");
					StateUpdateService.goToContentKitDetail(result.payload.identity);
					
				}, function(errorData) {
					Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
				});
		};	
		
		$scope.linkedKitsSelected = function(selectedContentKits) {
			$scope.linkedKitRecords = selectedContentKits;
			$scope.contentKit
		};
		
		$scope.linkedKitsFilter = {};
		$scope.linkedKitsFilterMetadata = {};

		if ($scope.editOper) {
			
			$scope.linkedKitsFilterMetadata = {
				identityNE : {
 					field : "identity",
 					operator : "NOT_EQ",
 					dataType : "STRING"
 				}	
			};
			
			$scope.linkedKitsFilter = {
				identityNE: contentKitIdentity
			};
		}
		
		$scope.linkedKitHeaders = 
			[{
				 desc: "Name",
				 valueKey: "name",
				 sortProperty: "name"
			 },
		     {
		    	 desc: "Created On",
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); },
		    	 sortProperty: "createdAt"
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName",
		    	 sortProperty: "createdByName"
		     }];
		
		$scope.linkedKitProjectedFields = ContentKitService.LIST_PROJECTION_FIELDS;
		$scope.contentKitDomainType = ContentKitService.DOMAIN_TYPE;
		
		$scope.contentSelected = function(selectedContentList) {
			
			$scope.contentKit.contentList = [];
			$scope.selectedContent = selectedContentList;
			
			angular.forEach(selectedContentList, function(selectedContentItem) {
				
				$scope.contentKit.contentList.push({
					templateId: enablix.templateId,
					containerQId: selectedContentItem.qualifiedId,
					instanceIdentity: selectedContentItem.identity,
					title: selectedContentItem.label,
					__containerLabel: selectedContentItem.containerLabel
				});
				
			});
			
			$scope.isContentSetCollapsed = false;
		}
		
		$scope.cancelOperation = function() {
			StateUpdateService.goToContentKitList();
		}
		
		$scope.editOperation = function() {
			StateUpdateService.goToContentKitEdit(contentKitIdentity);
		}
		
	}
]);			
