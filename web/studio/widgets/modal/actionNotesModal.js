enablix.studioApp.factory('ActionNotesWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal) {
		
			var showWindow = function(title, notesLabel) {
				return $modal.open({
				      templateUrl: 'views/modal/actionNotesModal.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ActionNotesController',
				      resolve: {
				    	  modalTitle: function() {
				    		  return title;
				    	  },
				    	  notesLabel: function() {
				    		  return notesLabel;
				    	  }
				      }
				    });
			};

	
			return {
				showWindow : showWindow
			};

		
		}
	]);


// Controller
enablix.studioApp.controller('ActionNotesController', 
			['$scope', '$modalInstance', 'modalTitle', 'notesLabel',
	function( $scope,   $modalInstance,   modalTitle,   notesLabel) {
		
		$scope.modalTitle = modalTitle;
		$scope.notesLabel = notesLabel;
		$scope.notes = "";
				
		$scope.ok = function() {
			$modalInstance.close($scope.notes);
		};
		
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
		
	}
]);