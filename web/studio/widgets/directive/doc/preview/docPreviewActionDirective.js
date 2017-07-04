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

enablix.studioApp.directive('ebRecordPreviewAction', function() {

	return {
		restrict: 'E',
		scope : {
			docIdentity : "=",
			docMetadata : "=?",
			containerQId : "=",
			recordIdentity : "=?",
			recordDetail : "=?",
			actionLabel : "@"
		},

		link: function(scope, element, attrs) {
		},

		controller: 'RecordPreviewActionCtrl',
		templateUrl: "widgets/directive/doc/preview/docPreviewAction.html"
	}
});