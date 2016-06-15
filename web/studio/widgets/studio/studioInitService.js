enablix.studioApp.factory('StudioSetupService', 
	[
	 	         '$rootScope', 'ContentTemplateService', 
	 	function ($rootScope,   ContentTemplateService) {
	 		
	 		var setupStudio = function() {
	 			
	 			if (enablix.template != null && enablix.template != undefined) {
	 				return enablix;
	 			}
	 			
	 			return ContentTemplateService.loadTemplate();
	 			
	 		};
	 		
	 		$rootScope.$watch('authenticated', function(newValue, oldValue) {
	 			
	 			if (!$rootScope.authenticated) {
	 				enablix.template = null;
	 				enablix.templateId = null;
	 			}
	 			
	 		});
	 		
	 		return {
	 			setupStudio: setupStudio
	 		};
	 	}
	 ]);