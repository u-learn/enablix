enablix.studioApp.directive('ebRenderData', [
        'ContentTemplateService', 'StateUpdateService', '$filter', '$compile',
function(ContentTemplateService,   StateUpdateService,   $filter,   $compile) {

	return {
		restrict: 'E',
		scope : {
			contentValue: '=',
			contentDef: '=',
			showDocLink:'=',
			contentRecord: '='
		},
		link: function(scope, element, attrs) {
			
			var template = ""; 
				
			var contentDataType = scope.contentDef.dataType;
			
			if (contentDataType === 'TEXT' 
				|| contentDataType === 'DATE_TIME' 
				|| contentDataType === 'NUMERIC' 
				|| contentDataType === 'RICH_TEXT') {
				
				var displayValue = $filter('ebxFormatData')(scope.contentValue, scope.contentDef.typeDef, scope.contentRecord);
				template += '<span class="content-data">' + displayValue + '</span>'
			}
				
			if (contentDataType === 'BOUNDED' && scope.contentValue && scope.contentValue.length != 0) {
				
				template += '<span class="content-data">';
				
				for (var i = 0; i < scope.contentValue.length; i++) {
					
					var bndItem = scope.contentValue[i];
					
					if (bndItem.hrefData) {
						template += '<a ng-click="navToItemDetail(\'' + bndItem.hrefData.containerQId + '\',\'' + bndItem.hrefData.contentIdentity + '\')">' + bndItem.label + '</a>';
					} else {
						template += '<span>' + bndItem.label + '</span>';
					}
					
					if (i != (scope.contentValue.length - 1)) {
						template += "<span>, </span>"
					}
				}
				
				template += '</span>';
			}
			
			if (contentDataType == 'DOC' && scope.contentValue.name) {
				
				if (scope.showDocLink) {
					template += '<span class="content-data">' + scope.contentValue.name + '</span>'
					template += '<eb-doc-download doc-value="contentValue.identity"></eb-doc-download>';
					template += '<eb-doc-preview-action doc-identity="contentValue.identity" doc-metadata="contentValue"></eb-doc-preview-action>';
				} else {
					template += '<span class="dataItem"><ebx-doc-context-menu content-record="contentRecord" label="contentValue.name"></ebx-doc-context-menu></span>'
				}
			}
			
			scope.navToItemDetail = function(_containerQId, _contentIdentity) {
				
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
				
				if (scope.$parent && scope.$parent.navToItemDetail) {
					scope.$parent.navToItemDetail(_containerQId, _contentIdentity);
				}
			}
			
			element.html('').append( $compile( template )( scope ) );
		}//,
		//templateUrl: "widgets/directive/renderData/renderData.html"
	};
}]);