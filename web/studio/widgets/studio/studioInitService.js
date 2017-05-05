enablix.studioApp.factory('StudioSetupService', 
	[
	 	         '$rootScope', '$q', '$location', 'ContentTemplateService', 'ResourceVersionService', 'TenantInfoService', 'UserPreferenceService',
	 	function ($rootScope,   $q,   $location,   ContentTemplateService,   ResourceVersionService,   TenantInfoService,   UserPreferenceService) {
	 		
	 		var setupStudio = function() {
	 			
	 			if (enablix.template != null && enablix.template != undefined) {
	 				return enablix;
	 			}
	 			
	 			TenantInfoService.init();
	 			
	 			var promises = [
	 			    ResourceVersionService.loadResourceVersions(),
	 			    ContentTemplateService.loadTemplate(),
	 			    UserPreferenceService.loadApplicablePreferences()
	 			];
	 			
	 			return $q.all(promises);
	 			
	 		};
	 		
	 		$rootScope.$watch('authenticated', function(newValue, oldValue) {
	 			
	 			if (!$rootScope.authenticated) {
	 				enablix.template = null;
	 				enablix.templateId = null;
	 			} else {
	 				// make the body visible only after authentication is complete
	 				$(document.body).css({"display": "block"});
	 			}
	 			
	 		});
	 		
	 		return {
	 			setupStudio: setupStudio
	 		};
	 	}
	 ]);
