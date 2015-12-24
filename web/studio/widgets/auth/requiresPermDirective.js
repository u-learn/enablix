enablix.studioApp.directive('requiresPermission', [
		'AuthorizationService',
		function(AuthorizationService) {

			return {
				restrict : 'A',
				link: function (scope, element, attributes) {

					var removeElement = function(element) {
						element && element.remove && element.remove();
					}
					
	                if (!AuthorizationService.userHasPermission(attributes.requiresPermission)) {
	                	removeElement(element);
	                }

		        }
			};
		} ]);