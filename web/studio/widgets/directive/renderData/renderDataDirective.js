enablix.studioApp.directive('ebRenderData', [
        'ContentTemplateService', 'StateUpdateService',
function(ContentTemplateService,   StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			contentValue: '=',
			contentDef: '='
		},
		link: function(scope, element, attrs) {
			scope.navToItemDetail = function(_containerQId, _contentIdentity) {
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
			}
		},
		templateUrl: "widgets/directive/renderData/renderData.html"
	};
}]);