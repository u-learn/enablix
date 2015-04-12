enablix.studioApp.directive('ebFileUpload', function() {

	return {
		restrict: 'E',
		scope : {
			docValue : "=",
			contentDef : "="
		},

		link: function(scope, element, attrs) {
			var _dataDef = scope.contentDef;
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
		},

		controller: 'FileUploadCtrl',
		templateUrl: "widgets/directive/doc/fileUpload.html"
	}
});