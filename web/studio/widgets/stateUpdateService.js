enablix.studioApp.factory('StateUpdateService', 
	[
	 			'$state', '$stateParams',
	 	function($state,   $stateParams) {
	 		
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

	 		var goToRefdataDetail = function(_containerQId, _elementIdentity) {
				$state.go('refdata.detail', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};
	 		
	 		var goToRefdataEdit = function(_containerQId, _elementIdentity) {
				$state.go('refdata.edit', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};

	 		
	 		var goToHome = function(_containerQId) {
				$state.go('home');
	 		};
	 		
	 		var goToLogin = function(_containerQId) {
				$state.go('login');
	 		};
	 		
	 		var goToStudio = function(_containerQId) {
				$state.go('studio');
	 		};
	 		
	 		var goToPortalContainerDetail = function(_containerQId, _elemIdentity) {
	 			$state.go("portal.container", {
	 				"containerQId": _containerQId,
	 				"elementIdentity": _elemIdentity
	 			});
	 		}
	 		
	 		var goToPortalContainerBody = function(_containerQId, _containerInstanceIdentity, 
	 												_subCntnrType, _subContainerQId) {
	 			$state.go("portal.container.body", {
	 				"containerQId": _containerQId,
	 				"elementIdentity": _containerInstanceIdentity,
	 				"type": _subCntnrType,
	 				"subContainerQId": _subContainerQId
	 			});
	 		};

	 		var goToPortalSubItem = function(_containerQId, _containerInstanceIdentity, 
						_subContainerQId, _subItemIdentity) {
				$state.go("portal.subItem", {
					"containerQId": _containerQId,
					"elementIdentity": _containerInstanceIdentity,
					"subItemIdentity": _subItemIdentity,
					"subContainerQId": _subContainerQId
				});
			};
	 		
	 		var goToPortalHome = function() {
	 			$state.go("portal.home");
	 		};

	 		var goToPortalEnclosureDetail = function(_enclosureId) {
	 			$state.go("portal.enclosure", {
	 				"enclosureId": _enclosureId
	 			});
	 		}
	 		
	 		var goToPortalEnclosureBody = function(_enclosureId, _subCntnrType, _subContainerQId) {
	 			$state.go("portal.enclosure.body", {
	 				"enclosureId": _enclosureId,
	 				"type": _subCntnrType,
	 				"subContainerQId": _subContainerQId
	 			});
	 		};
	 		
	 		var goToPortalSearch = function(_searchText) {
	 			$state.go("portal.search", {
	 				"searchText": _searchText
	 			});
	 		}
	 		
	 		var reload = function() {
	 			$state.transitionTo($state.current, $stateParams, {
	 			    reload: true,
	 			    inherit: false,
	 			    notify: true
	 			});
	 		};

	 		
	 		return {
	 			goToStudioList: goToStudioList,
	 			goToStudioDetail: goToStudioDetail,
	 			goToStudioAdd: goToStudioAdd,
	 			goToStudioEdit: goToStudioEdit,
	 			goToRefdataList: goToRefdataList,
	 			goToRefdataAdd: goToRefdataAdd,
	 			goToRefdataDetail: goToRefdataDetail,
	 			goToRefdataEdit: goToRefdataEdit,
	 			goToHome: goToHome,
	 			goToLogin: goToLogin,
	 			goToStudio: goToStudio,
	 			goToPortalHome: goToPortalHome,
	 			goToPortalContainerDetail: goToPortalContainerDetail,
	 			goToPortalContainerBody: goToPortalContainerBody,
	 			goToPortalEnclosureDetail: goToPortalEnclosureDetail,
	 			goToPortalEnclosureBody: goToPortalEnclosureBody,
	 			goToPortalSubItem: goToPortalSubItem,
	 			goToPortalSearch: goToPortalSearch,
	 			reload: reload
	 		};
	 	}
	 ]);