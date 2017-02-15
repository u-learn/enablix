enablix.studioApp.controller('ContentKitDetailCtrl', 
			['$scope', '$state', '$stateParams', 'ContentKitService', 'ContentTemplateService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   ContentKitService,   ContentTemplateService,   StateUpdateService,   Notification) {
	
		$scope.portalPage = $state.includes("portal");
		var contentKitIdentity = $stateParams.contentKitIdentity;
		
		$scope.linkedKits = {};
		$scope.contentKit = {};
		$scope.contentGroupsMap = {};
		
		ContentKitService.getContentKitBundle(contentKitIdentity, function(data) {
				
				$scope.linkedKits = data.contentKitDetail.linkedKits;
				$scope.contentKit = data.contentKitDetail.contentKit;
				
				// create a map of contentQId (containerQId) -> content group
				// the content group will group all records with same containerQId
				var contentGroupMap = {};
				
				angular.forEach(data.contentRecords, function(contentDataRecord) {
					
					var contentQId = contentDataRecord.containerQId;
					var contentGroup = contentGroupMap[contentQId];
				
					if (isNullOrUndefined(contentGroup)) {
						
						var containerLabel = ContentTemplateService.getContainerLabel(contentQId);
						
						contentGroup = {
							qualifiedId: contentQId,
							label: containerLabel,
							records: []
						};
						
						contentGroupMap[contentQId] = contentGroup;
					}
					
					contentGroup.records.push(contentDataRecord.record);
				});
				
				$scope.contentGroupsMap = contentGroupMap;
				
			}, function(resp) {
				Notification.error({message: "Error retrieving content kit", delay: enablix.errorMsgShowTime});
			});
				
	}
]);			
