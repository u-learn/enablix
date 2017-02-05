enablix.studioApp.factory('shareContentModalWindow', 
		[	 '$state', '$stateParams', 'Notification', '$modal',
			function( $state,   $stateParams,   Notification,   $modal) {

			var showShareContentModal = function(containerQId, contentIdentity,$event) {
				var modalInstance = $modal.open({
					templateUrl: 'widgets/directive/portal/sharePopup/share-content-popup.html',
					size: 'md', // 'sm', 'lg'
					controller: 'ShareController',
					parent: angular.element(document.body),
					targetEvent: $event,
					clickOutsideToClose:true,
					resolve: {				    	  
						containerQId: function() {
							return containerQId;
						},
						contentIdentity : function() {
							return contentIdentity;
						}
					}
				});
			};
		
			var showShareToSlackModal = function(containerQId, contentIdentity,contentName,
					portalURL,$event) {
				var modalInstance = $modal.open({
					templateUrl: 'widgets/directive/portal/sharePopup/shareSlack/shareToSlack.html',
					size: 'sm', // 'sm', 'lg'
					controller: 'ShareToSlackController',
					parent: angular.element(document.body),
					targetEvent: $event,
					clickOutsideToClose:true,
					resolve: {				    	  
						containerQId: function() {
							return containerQId;
						},
						contentIdentity : function() {
							return contentIdentity;
						},
						contentName : function() {
							return contentName;
						},
						portalURL : function() {
							return portalURL;
						}
					}
				});
			};
			return {
				showShareContentModal : showShareContentModal,
				showShareToSlackModal: showShareToSlackModal
			};	

		}]);
