enablix.studioApp.directive('ebDocPreviewAction', function() {

	return {
		restrict: 'E',
		scope : {
			docIdentity : "=",
			docMetadata : "=?",
			actionLabel : "@"
		},

		link: function(scope, element, attrs) {
		},

		controller: 'DocPreviewActionCtrl',
		templateUrl: "widgets/directive/doc/preview/docPreviewAction.html"
	}
});