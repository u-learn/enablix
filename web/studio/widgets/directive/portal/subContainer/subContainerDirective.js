enablix.studioApp.directive('ebPortalSubContainer', [
	function() {

		return {
			restrict : 'E',
			scope : {
				id: "@",
				type: "@",
				parentContainerQId: "=?",
				subContainerQId: "=",
				subContainerLabel: "=",
				elementIdentity: "=",
				navContentData: "=",
				index: "=",
				parentList: "=",
				expanded: "@",
				multiListLimit: "@",
				info: "=?",
				category: "=?",
				showLabel: "@",
				ebLayout: "@",
				summaryView: "=",
				recordCount: "="
			},
			controller : 'PortalSubContainerCtrl',
			templateUrl : function(elem, attr) {
				var layout = attr.ebLayout || "default";
				return "widgets/directive/portal/subContainer/subContainer-" + layout + ".html";
			},
			link: function(scope, element, attrs) {
				scope.singleItemCard = scope.type === 'single';
				
				scope.boxViewCSSClass = function() {
					return scope.summaryView && !element.expanded && $(element).find('.data-box').height() > 105 ? "box-view" : "" ;
				}
				
				scope.expandBoxView = function($event) {
					element.expanded = true;
					var elem = $event.currentTarget;
					$(elem).parent().parent().removeClass('box-view');
				}
			}
			
		};
	} ]);