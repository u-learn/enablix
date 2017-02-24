enablix.studioApp.controller('PortalSubContainerCtrl',
			['$scope', '$state', 'StateUpdateService', 'UserService', '$stateParams', 'PubSub', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', 'Notification','shareContentModalWindow', 'ActivityAuditService', 'QIdUtil',
    function ($scope,   $state,   StateUpdateService,   UserService,   $stateParams,   PubSub,   ContentTemplateService,   ContentDataService,   ContentUtil,   Notification,    shareContentModalWindow,   ActivityAuditService,   QIdUtil) {
		
		$scope.toggleContainer = function($event) {
			var elem = $event.currentTarget;
			if ($event.target.nodeName != 'A' ) {
				$(elem).toggleClass('active');
				$(elem).next().slideToggle('fast');
			}
		};
		
		$scope.toggleContainerItem = function($event, itemId, _dataRecord) {
			
			var elem = $event.currentTarget;
			$(elem).toggleClass('active');
			$('#' + itemId).slideToggle('fast');
			
			var expanded = elem.className.indexOf('active') > 0;
			
			if (_dataRecord && expanded && !elem.accessAudited) {
				ActivityAuditService.auditContentAccess(
					_dataRecord.containerQId, _dataRecord.identity, _dataRecord.headingLabel, 
					function() { 
						elem.accessAudited = true; 
					}, 
					function() {/* do nothing */});
				
			}
			
			return false;
		};	
		
		var setUpMultiRecordData = function(_contentRecords) {
	
			var data = _contentRecords;
			
			if ($scope.type == 'single' && data && data.length > 0) {
				$scope.bodyData = data[0];
				decorateData($scope.containerDef, $scope.bodyData);
				$scope.showSubContainer = true;
				
			} else {
				
				if (data && data.length > 0) {
					angular.forEach(data, function(dataItem) {
						decorateData($scope.containerDef, dataItem);
					});
					$scope.showSubContainer = true;
				}
				
				$scope.bodyData = data;
			}
			
			if (!$scope.showSubContainer) {
				if ($scope.parentList && $scope.index) {
					$scope.parentList[$scope.index] = "null" + $scope.index; // hack to remove div in portal-container.html. Setting it to null cause duplicate entry error in ng-repeat
				}
			}
		}
		
		var setUpSingleRecordData = function(recordData) {
			
			decorateData($scope.containerDef, recordData);
			$scope.bodyData = recordData;
			
			if ($scope.bodyData != null && $scope.bodyData != undefined) {
			
				$scope.showSubContainer = true;
				$scope.parentIdentity = recordData.parentIdentity;
				
			} else {
				
				if ($scope.parentList && $scope.index) {
					$scope.parentList[$scope.index] = "null" + $scope.index; // hack to remove div in portal-container.html
				}
			}
		};
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
									_containerDef, _dataRecord);
			_dataRecord.containerId = _containerDef.id;
			_dataRecord.containerQId = _containerDef.qualifiedId;
			_dataRecord.hasSubContainers = !isNullOrUndefined(_containerDef.container)
											&& _containerDef.container.length > 0;
		
			ContentUtil.decorateData(_containerDef, _dataRecord, true);
		}
				
		$scope.render = function() {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
							enablix.template, $scope.subContainerQId);
			
			$scope.navigableHeader = !isNullOrUndefined($scope.navContentData);
			
			if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
				$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.containerDef.linkContainerQId);
			}
			
			$scope.showSubContainer = false;
			$scope.$stateParams = $stateParams;
			
			$scope.navToItemDetail = function(_containerQId, _contentIdentity) {
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
			}
			
			if ($scope.info) {
				
				// data record already provided
				if ($scope.type === 'single') {
					
					var dataRecord = isArray($scope.info) && $scope.info.length > 0 ? $scope.info[0] : null;
					if (dataRecord != null) {
						setUpSingleRecordData(dataRecord);
					}
					
				} else {
					setUpMultiRecordData($scope.info);
				}
				
			} else {
				
				// data record(s) not provide, go fetch them
				if ($stateParams.containerQId === $scope.subContainerQId 
						|| !isNullOrUndefined($scope.elementIdentity)) {
				
					var elemIdentity = isNullOrUndefined($scope.elementIdentity) ? $stateParams.elementIdentity
											: $scope.elementIdentity;
					
					var cntnrQId = isNullOrUndefined($stateParams.containerQId) ? $scope.subContainerQId 
											: $stateParams.containerQId;
						
					ContentDataService.getContentRecordData(enablix.templateId, cntnrQId, elemIdentity, 'PORTAL', 
							function(recordData) {
								setUpSingleRecordData(recordData);
							},
							function(errResp) {
								Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
							});
					
				} else {
					
					var paginationData = isNullOrUndefined($scope.multiListLimit) ? undefined
							: { pageNum: 0, pageSize: $scope.multiListLimit };
					
					$scope.parentIdentity = $stateParams.elementIdentity;
							
					ContentDataService.getContentData(enablix.templateId, $scope.subContainerQId, $stateParams.elementIdentity,
						function(dataPage) {
							
							var data = dataPage;
							if (dataPage.content) {
								data = dataPage.content;
								$scope.pageData = dataPage;
							}
							
							setUpMultiRecordData(data);
							
						},
						function(errResp) {
							Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
						}, 
						paginationData);
				}
			}
			
			$scope.headers = [];
			
			angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
				
				var header = {
					"key" : containerAttr.id,
					"label" : containerAttr.label,
					"dataType" : containerAttr.type,
					"typeDef" : containerAttr
				};
				
				$scope.headers.push(header);
				
			});
			
			$scope.expanded = $scope.expanded || false;
			
			$scope.showSubContainerList = function() {
				
				if ($state.includes('portal.enclosure')) {
				
					StateUpdateService.goToPortalEnclosureDetail($stateParams.enclosureId, $scope.subContainerQId);
					
				} else {
					
					StateUpdateService.goToPortalSubContainerList(
						QIdUtil.getParentQId($scope.subContainerQId), 
						$scope.subContainerQId, $stateParams.elementIdentity);
				}
			}
		};
		
		$scope.hasData = function(_data) {
			return !isNullOrUndefined(_data) && !isArrayAndEmpty(_data) && !isStringAndEmpty(_data);
		}
		
		$scope.isRenderable = function(_contentDef, _data) {
			return _contentDef.dataType != 'CONTENT_STACK' && $scope.hasData(_data);
		}
		
		$scope.render();
		
		PubSub.subscribe(ContentDataService.contentChangeEventId($scope.subContainerQId), function() {
			$scope.render();
		});
		
	}]);
