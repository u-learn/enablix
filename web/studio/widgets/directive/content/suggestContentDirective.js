enablix.studioApp.directive('ebxSuggestContent', [
                 'StateUpdateService', '$modal',
		function (StateUpdateService,   $modal) {

			return {
				restrict : 'E',
				scope : {
					contentQId: "=",
					parentIdentity: "=?",
					contentIdentity: "=?",
					icon: "@"
				},
				link : function(scope, element, attrs) {
					
						
					var checkEditSuggestion = function() {
						
						if (!isNullOrUndefined(scope.contentIdentity)) {
							scope.editOper = true;
						} else {
							scope.editOper = false;
						}
						
						if (!isNullOrUndefined(scope.icon)) {
							scope.iconClass = scope.icon;
						} else {
							scope.iconClass = scope.editOper ? "fa fa-pencil suggest-edit" : "fa fa-plus-square suggest-add";
						}
					}

					checkEditSuggestion();
					
					
					
					scope.$watch('contentIdentity', function() {
						checkEditSuggestion();
					});
					
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
						    	  },
						    	  contentIdentity: function() {
						    		  return scope.contentIdentity
						    	  }
						      }
						});
					}
					
				},
				templateUrl : "widgets/directive/content/suggestContent.html"
			};
		} ]);