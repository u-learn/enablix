enablix.studioApp.controller('RecordDetailWindowCtrl', 
			['$scope', '$state', '$stateParams', 'containerQId', 'recordDetail', '$modalInstance', 'DocPreviewService', 'ContentTemplateService', 'ContentUtil',
	function( $scope,   $state,   $stateParams,   containerQId,   recordDetail,   $modalInstance,   DocPreviewService,   ContentTemplateService,   ContentUtil) {
		
		$scope.recordDetail = recordDetail;
		$scope.containerQId = containerQId;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		$scope.headers = ContentUtil.getContentDetailHeaders($scope.containerDef, true, false); 

		if (recordDetail.downloadDocIdentity) {
		
			$scope.previewHtml = "<p>Loading...</p>";
			var docMetadata = $scope.docMetadata = recordDetail.docMetadata;
			
			var previewHandler = DocPreviewService.getPreviewHandler(docMetadata);
			
			if (!isNullOrUndefined(previewHandler)) {
				
				$scope.htmlType = previewHandler.htmlType();
				var pHtml = previewHandler.previewHtml(docMetadata);
				
				if ($scope.htmlType == 'html') {
					$scope.previewHtml = $sce.trustAsHtml(pHtml);
				} else if ($scope.htmlType == 'angular-html') {
					$scope.previewHtml = pHtml;
				}
			}
			
		}
		
		$scope.isRenderable = function(_contentDef, _data) {
			return ContentUtil.isContentFieldRenderable(_contentDef, _data);
		}
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
	}
]);