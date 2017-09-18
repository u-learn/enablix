enablix.studioApp.directive('ebContentQualityAlerts', [
             'StateUpdateService',
	function (StateUpdateService) {

		return {
			restrict : 'E',
			scope : {
				alerts: "=?"
			},
			controller : function($scope) {
				
			},
			templateUrl : "widgets/directive/content/contentQualityAlerts.html"
		};
	} ]);