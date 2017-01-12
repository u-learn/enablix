enablix.studioApp.directive('ebxConditionAttributes', function() {

	return {
		restrict: 'E',
		scope : {
			conditionHolder : "=",
			conditionOnContainer : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'ConditionAttrsCtrl',
		templateUrl: "widgets/directive/condition/conditionAttrs.html"
	}
});