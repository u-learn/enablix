enablix.studioApp.controller('DocPreviewActionCtrl', 
			['$scope', '$modal', 'DocPreviewService', 'DocService',
    function ($scope,   $modal,   DocPreviewService,   DocService) {
    
		$scope.previewSupported = false;
		$scope.docMd = $scope.docMetadata || {};
		
		var checkPreviewSupported = function() {
			var promise = DocPreviewService.checkPreviewAvailable($scope.docMd);
			promise.then(function(data) {
				$scope.previewSupported = data; //!isNullOrUndefined(previewHandler);
			});
		}
		
		if (!$scope.docMd.identity) {
			
			DocService.getDocMetadata($scope.docIdentity, function(data) {
				$scope.docMd = data;
				checkPreviewSupported();
			});
			
		} else {
			checkPreviewSupported();
		}
		
		$scope.openPreviewWindow = function() {
			DocPreviewService.openPreviewWindow($scope.docMd);
		};
				
	}]);


enablix.studioApp.controller('RecordPreviewActionCtrl', 
			['$scope', '$modal', 'DocPreviewService', 'DocService', 'ContentDataService',
	function ($scope,   $modal,   DocPreviewService,   DocService,   ContentDataService) {
	
		$scope.previewSupported = false;
		$scope.docMd = $scope.docMetadata || {};
		
		var checkPreviewSupported = function() {
			var promise = DocPreviewService.checkPreviewAvailable($scope.docMd);
			promise.then(function(data) {
				$scope.previewSupported = data; //!isNullOrUndefined(previewHandler);
			});
		}
		
		if (!$scope.docMd.identity) {
			
			DocService.getDocMetadata($scope.docIdentity, function(data) {
				$scope.docMd = data;
				checkPreviewSupported();
			});
			
		} else {
			checkPreviewSupported();
		}
		
		$scope.openPreviewWindow = function() {
			ContentDataService.openRecordDetailWindow($scope.containerQId, $scope.recordDetail, $scope.recordIdentity);
		};
				
	}]);
