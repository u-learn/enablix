enablix.studioApp.factory('ConfirmationModalWindow', 
	[			'$state', '$stateParams', 'RESTService', 'Notification', '$modal',
	 	function($state,   $stateParams,   RESTService,   Notification,   $modal) {
		
			var showWindow = function(title, confirmationTxt, okLabel, cancelLabel) {
				return $modal.open({
				      templateUrl: 'views/modal/confirmModal.html',
				      size: 'sm', // 'sm', 'lg'
				      controller: 'ConfirmationModalController',
				      backdrop: 'static',
				      resolve: {
				    	  modalTitle: function() {
				    		  return title;
				    	  },
				    	  confirmationText: function() {
				    		  return confirmationTxt;
				    	  },
				    	  okLabel: function() {
				    		  return okLabel || "Ok";
				    	  },
				    	  cancelLabel: function() {
				    		  return cancelLabel || "Cancel";
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
enablix.studioApp.controller('ConfirmationModalController', 
			['$scope', '$modalInstance', 'modalTitle', 'confirmationText', 'okLabel', 'cancelLabel',
	function( $scope,   $modalInstance,   modalTitle,   confirmationText,   okLabel,   cancelLabel) {
		
		$scope.modalTitle = modalTitle;
		$scope.confirmationText = confirmationText;
		$scope.okLabel = okLabel;
		$scope.cancelLabel = cancelLabel;
				
		$scope.ok = function() {
			$modalInstance.close(true);
		};
		
		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
		
	}
]);