enablix.studioApp.controller('DocPreviewActionCtrl', 
			['$scope', '$modal', 'DocPreviewService', 'DocService',
    function ($scope,   $modal,   DocPreviewService,   DocService) {
    
		$scope.previewSupported = false;
		$scope.docMd = $scope.docMetadata || {};
		
		var checkPreviewSupported = function() {
			var previewHandler = DocPreviewService.getPreviewHandler($scope.docMd);
			$scope.previewSupported = !isNullOrUndefined(previewHandler);
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
			var modalInstance = $modal.open({
			      templateUrl: 'widgets/directive/doc/preview/docPreviewWindow.html',
			      //size: 'sm', // 'sm', 'lg'
			      controller: 'DocPreviewCtrl',
			      resolve: {
			    	  docMetadata: function() {
			    		  return $scope.docMd;
			    	  }
			      }
			    });
		};
				
	}]);
