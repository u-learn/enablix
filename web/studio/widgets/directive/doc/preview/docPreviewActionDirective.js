enablix.studioApp.directive('ebDocPreviewAction', function() {

	return {
		restrict: 'E',
		scope : {
			docIdentity : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'DocPreviewActionCtrl',
		templateUrl: "widgets/directive/doc/preview/docPreviewAction.html"
	}
});