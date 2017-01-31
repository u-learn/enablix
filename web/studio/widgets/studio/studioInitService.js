enablix.studioApp.factory('StudioSetupService', 
	[
	 	         '$rootScope', '$q', 'ContentTemplateService', 'ResourceVersionService',
	 	function ($rootScope,   $q,   ContentTemplateService,   ResourceVersionService) {
	 		
	 		var setupStudio = function() {
	 			
	 			if (enablix.template != null && enablix.template != undefined) {
	 				return enablix;
	 			}
	 			
	 			var promises = [
	 			    ResourceVersionService.loadResourceVersions(),
	 			    ContentTemplateService.loadTemplate(),
	 			];
	 			
	 			return $q.all(promises);
	 			
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