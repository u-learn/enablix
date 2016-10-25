enablix.studioApp.directive('ebxSuggestContent', [
                 'StateUpdateService', '$modal',
		function (StateUpdateService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					contentQId: "=",
					parentIdentity: "="
				},
				link : function(scope, element, attrs) {
					
					scope.openAddContentWindow = function() {
						var modalInstance = $modal.open({
						      templateUrl: 'views/content/content-suggest.html',
						      size: 'lg', // 'sm', 'lg'
						      controller: 'ContentSuggestCtrl',
						      resolve: {
						    	  parentIdentity: function() {
						    		  return scope.parentIdentity;
						    	  },
						    	  containerQId: function() {
						    		  return scope.contentQId;
						    	  }
						      }
						});
					}
					
				},
				templateUrl : "widgets/directive/content/suggestContent.html"
			};
		} ]);