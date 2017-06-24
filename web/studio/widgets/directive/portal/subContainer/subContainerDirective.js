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
				ebLayout: "@"
			},
			controller : 'PortalSubContainerCtrl',
			templateUrl : function(elem, attr) {
				var layout = attr.ebLayout || "default";
				return "widgets/directive/portal/subContainer/subContainer-" + layout + ".html";
			},
			link: function(scope, element, attrs) {
				scope.singleItemCard = scope.type === 'single';
			}
			
		};
	} ]);