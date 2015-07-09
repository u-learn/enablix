enablix.studioApp.directive('ebPortalSubContainer', [
	function() {

		return {
			restrict : 'E',
			scope : {
				type: "@",
				subContainerQId: "=",
				subContainerLabel: "=",
				index: "="
			},
			controller : 'PortalSubContainerCtrl',
			templateUrl : function(elem, attr) {
				return "widgets/directive/portal/subContainer/subContainer-" + attr.type + ".html";
			}
		};
	} ]);