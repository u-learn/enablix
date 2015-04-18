enablix.studioApp.factory('StateUpdateService', 
	[
	 			'$state',
	 	function($state) {
	 		
	 		var goToStudioList = function(_containerQId, _parentIdentity) {
				$state.go('studio.list', {'containerQId' : _containerQId, 
					"parentIdentity" : _parentIdentity});

	 		};
	 		
	 		var goToStudioDetail = function(_containerQId, _elementIdentity) {
				$state.go('studio.detail', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};
	 		
	 		var goToStudioAdd = function(_containerQId, _parentIdentity) {
	 			$state.go('studio.add', {
					'containerQId': _containerQId,
					'parentIdentity': _parentIdentity
				});
	 		};

	 		var goToStudioEdit = function(_containerQId, _elementIdentity) {
				$state.go('studio.edit', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};

	 		var goToRefdataList = function(_containerQId) {
				$state.go('refdata.list', {'containerQId' : _containerQId});
	 		};

	 		var goToRefdataAdd = function(_containerQId) {
				$state.go('refdata.add', {'containerQId' : _containerQId});
	 		};
	 		
	 		
	 		return {
	 			goToStudioList: goToStudioList,
	 			goToStudioDetail: goToStudioDetail,
	 			goToStudioAdd: goToStudioAdd,
	 			goToStudioEdit: goToStudioEdit,
	 			goToRefdataList: goToRefdataList,
	 			goToRefdataAdd: goToRefdataAdd
	 		};
	 	}
	 ]);