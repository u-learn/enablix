enablix.studioApp.controller('RecordDetailWindowCtrl', 
			['$scope', '$sce', 'containerQId', 'recordDetail', 'recordIdentity', '$modalInstance', 'ContentDataService', 'DocPreviewService', 'ContentTemplateService', 'ContentUtil', 'StateUpdateService',
	function( $scope,   $sce,   containerQId,   recordDetail,   recordIdentity,   $modalInstance,   ContentDataService,   DocPreviewService,   ContentTemplateService,   ContentUtil,   StateUpdateService) {
		
		$scope.containerQId = containerQId;
		$scope.recordIdentity = recordIdentity;
		
		$scope.containerDef = ContentTemplateService.getConcreteContainerDefinition(enablix.template, containerQId);
		$scope.headers = ContentUtil.getContentDetailHeaders($scope.containerDef, true, false); 

		var initPreview = function() {
		
			$scope.recordIdentity = recordDetail.identity;
			
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
				
			} else if (recordDetail.__urls && recordDetail.__urls.length > 0) {
				$scope.htmlType = 'angular-html';
				$scope.previewUrlInfo = recordDetail.__urls[0];
				$scope.previewHtml = '<ebx-url-preview url-info="previewUrlInfo" content-q-id="containerQId" content-identity="recordIdentity"></ebx-url-preview>';
			}
		}
		
		$scope.isRenderable = function(_contentDef, _data) {
			return ContentUtil.isContentFieldRenderable(_contentDef, _data);
		}
		
		$scope.navToContentDetail = function(contentRecordIdentity) {
			$scope.close();
			StateUpdateService.goToPortalContainerDetail(containerQId, contentRecordIdentity);
		}
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
		$scope.navToItemDetail = function() {
			$scope.close();
		}
		
		if (isNullOrUndefined(recordDetail)) {
			
			if (!isNullOrUndefined(recordIdentity)) {
				
				ContentDataService.getContentRecordData(enablix.templateId, containerQId, recordIdentity, null, 
					function(recordData) {
						
						ContentUtil.decorateData($scope.containerDef, recordData, true);
						
						$scope.recordDetail = recordDetail = recordData;
						initPreview();
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
			}
			
		} else {
			$scope.recordDetail = recordDetail;
			initPreview();
		}
		
	}
]);