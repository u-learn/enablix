enablix.studioApp.directive('ebxUrlPreview', function() {

	return {
		restrict: 'E',
		scope : {
			urlInfo : "=",
			contentQId: "=?",
			contentIdentity: "=?"
		},

		link: function(scope, element, attrs) {
		},

		controller: 'UrlPreviewCtrl',
		template: "<div class='embed-preview' ng-bind-html='urlPreviewHtml'></div>"
	}
});

enablix.studioApp.controller('UrlPreviewCtrl', 
			['$scope', '$sce', '$timeout', 'UrlEmbedService', 'LocationUtil',
	function ($scope,   $sce,   $timeout,   UrlEmbedService,   LocationUtil) {
				
		var linkParams = {
				"cId" : $scope.contentIdentity,
				"cQId" : $scope.contentQId
			};
		
		var navUrl = $scope.navUrl = LocationUtil.createExtLinkUrl($scope.urlInfo.url, linkParams);
		
		UrlEmbedService.getEmbedInfo($scope.urlInfo.url, 
			function(data) {
			
				var type = data.oembed ? data.oembed.type : data.type;
				var html = '';
				
				if (type == 'rich') {
					var imgSrc = data.images && data.images.length > 0 ? data.images[0].url : null;
					html = '<div class="embed-rich">' +
									'<div class="embed-heading"><a target="_blank" href="' + navUrl + '">' + data.title + '</a></div>' +
									'<div class="embed-excerpt">' +
										'<a target="_blank" href="' + navUrl + '">' +
											(imgSrc != null ? '<div class="embed-image"><img src="' + imgSrc + '"></img></div>' : '') + 
											'<div class="embed-desc">' + data.description + '</div>' +
										'</a>' +
									'</div>' + 
							   '</div>';
				} else if (data.oembed && data.oembed.html) {
					html = data.oembed.html;
				} 
				
				$scope.urlPreviewHtml = $sce.trustAsHtml(html);
				
			}, function(errorData) {
				Notification.error({message: "Error getting preview information", delay: enablix.errorMsgShowTime});
			});

}]);
