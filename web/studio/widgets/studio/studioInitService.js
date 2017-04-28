enablix.studioApp.factory('StudioSetupService', 
	[
	 	         '$rootScope', '$q', '$location', 'ContentTemplateService', 'ResourceVersionService', 'TenantInfoService',
	 	function ($rootScope,   $q,   $location,   ContentTemplateService,   ResourceVersionService,   TenantInfoService) {
	 		
	 		var setupStudio = function() {
	 			
	 			if (enablix.template != null && enablix.template != undefined) {
	 				return enablix;
	 			}
	 			
	 			TenantInfoService.init();
	 			
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
