enablix.studioApp.directive('ebFileUpload', function() {

	return {
		restrict: 'E',
		scope : {
			docValue : "=",
			contentDef : "=",
			parentIdentity : "=",
			containerIdentity : "=",
			docIdentity : "=",
			temporary: "="
		},

		link: function(scope, element, attrs) {
			var _dataDef = scope.contentDef;
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
			scope.qualifiedId = _dataDef.qualifiedId
			scope.temporary = scope.temporary || false;
		},

		controller: 'FileUploadCtrl',
		templateUrl: "widgets/directive/doc/fileUpload.html"
	}
});