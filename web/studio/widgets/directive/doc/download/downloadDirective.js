enablix.studioApp.directive('ebDocDownload', [
		function() {

			return {
				restrict : 'E',
				scope : {
					docValue : '=',
					actionLabel : "@"
				},
				link: function(scope, element, attrs) {
					var params = { };
					var requestConfig = genereateRequestConfig("downloadDocument", params);
					scope.docBaseUrl = requestConfig.url; 
				},
				
				controller: 'DownloadCtrl',
				templateUrl : "widgets/directive/doc/download/downloadFile.html"
			};
	} ]);