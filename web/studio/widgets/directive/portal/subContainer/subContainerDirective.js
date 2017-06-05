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
				showLabel: "@"
			},
			controller : 'PortalSubContainerCtrl',
			templateUrl : "widgets/directive/portal/subContainer/subContainer-both.html",
			link: function(scope, element, attrs) {
				scope.singleItemCard = scope.type === 'single';
			}
			
		};
	} ]);