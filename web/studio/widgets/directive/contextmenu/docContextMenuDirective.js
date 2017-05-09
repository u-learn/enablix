enablix.studioApp.directive('ebxDocContextMenu',
			['RESTService', 'Notification', '$state', 'StateUpdateService', 'DocPreviewService',
	function (RESTService,   Notification,   $state,   StateUpdateService,   DocPreviewService) {
	return {
		restrict: 'E',
		scope : {
			contentRecord : "=",
			label: "="
		},
		link: function(scope, element, attrs) {
			if (scope.contentRecord.docMetadata) {
				scope.hasPreviewOption = DocPreviewService.getPreviewHandler(scope.contentRecord.docMetadata) != null;
			}
		},
		templateUrl: "widgets/directive/contextmenu/docContextMenu.html"
	};
}]);
