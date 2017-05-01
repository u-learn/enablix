enablix.studioApp.directive('ebPortalSubContainer', [
	function() {

		return {
			restrict : 'E',
			scope : {
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
			//template : '<ng-include src="::templateUrl"></ng-include>',
			templateUrl : function(elem, attr) {
				return "widgets/directive/portal/subContainer/subContainer-both.html";
			},
			link: function(scope, element, attrs) {
				scope.singleItemCard = scope.type === 'single';
			}
			
		};
	} ]);