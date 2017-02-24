enablix.studioApp.directive('ebRichtextEditor', [
        'ContentTemplateService', 'Notification',
function(ContentTemplateService, Notification) {

	return {
		restrict: 'E',
		scope : {
			plainText: '=',
			richText: '=',
			contentDef: '='
		},
		link: function(scope, element, attrs) {
			
			var _dataDef = scope.contentDef;
			
			scope.contentChanged = function (editor, html, text, delta, oldDelta, source) {
				scope.plainText = text;
			}
			
			scope.label = _dataDef.label;
			scope.placeholder = "Enter " + _dataDef.label;
			
			scope.quillModules = {
		        toolbar: [
		                  ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
		                  //['blockquote', 'code-block'],

		                  //[{ 'header': 1 }, { 'header': 2 }],               // custom button
		        															// values
		                  [{ 'list': 'ordered' }, { 'list': 'bullet' }],
		                  //[{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
		                  [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
		                  //[{ 'direction': 'rtl' }],                         // text direction

		                  //[{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
		                  //[{ 'header': [1, 2, 3, 4, 5, 6, false] }],

		                  //[{ 'color': [] }, { 'background': [] }],          // dropdown with
		        															// defaults from
		        															// theme
		                  //[{ 'font': [] }],
		                  //[{ 'align': [] }],

		                  //['clean'],                                         // remove
		        																// formatting
		        																// button

		                  //['link', 'image', 'video']                         // link and
		        																// image, video
		                ]
			}
			
		},
		templateUrl: "widgets/directive/richtext/richtextEditor.html"
	};
}]);