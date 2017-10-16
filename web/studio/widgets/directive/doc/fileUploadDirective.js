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
			scope.temporary = scope.temporary || false; // will check and revert to true .. currently causing error in haystax while moving file when the destination file already exists in dropbox.. true; // always temporary. will get moved when content is actually saved. //scope.temporary || false;
		},

		controller: 'FileUploadCtrl',
		templateUrl: "widgets/directive/doc/fileUpload.html"
	}
});