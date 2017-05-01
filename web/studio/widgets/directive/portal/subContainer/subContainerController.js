enablix.studioApp.controller('PortalSubContainerCtrl',
			['$scope', '$state', 'StateUpdateService', 'UserService', '$stateParams', 'PubSub', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', 'Notification','shareContentModalWindow', 'ActivityAuditService', 'QIdUtil',
    function ($scope,   $state,   StateUpdateService,   UserService,   $stateParams,   PubSub,   ContentTemplateService,   ContentDataService,   ContentUtil,   Notification,    shareContentModalWindow,   ActivityAuditService,   QIdUtil) {
		
		$scope.toggleContainer = function($event) {
			var elem = $event.currentTarget;
			if ($event.target.nodeName != 'A' && $event.target.nodeName != 'SPAN') {
				$(elem).toggleClass('active');
				$(elem).next().slideToggle('fast');
				
				$scope.expanded = elem.className.indexOf('active') > 0;
			}
		};
		
		$scope.toggleContainerItem = function($event, itemId, _dataRecord) {
			
			_dataRecord.showDetails = true;
			
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
		
		$scope.showDataItemActionIcons = function($event) {
			var elem = $event.currentTarget;
			$(elem).children('.action-icons').css('display', 'block');
		}
		
		$scope.hideDataItemActionIcons = function($event) {
			var elem = $event.currentTarget;
			if (!$(elem).find('a.toggle').hasClass("active")) {
				$(elem).children('.action-icons').css('display', 'none');
			}
		}
		
		var setUpMultiRecordData = function(_contentRecords) {
	
			$scope.parentIdentity = $stateParams.elementIdentity;
			
			var data = _contentRecords;
			if (_contentRecords.content) {
				data = _contentRecords.content;
				$scope.pageData = _contentRecords;
			}
			
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
					$scope.parentList[$scope.index].id = "null" + $scope.index; // hack to remove div in portal-container.html. Setting it to null cause duplicate entry error in ng-repeat
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
			
			if ($stateParams.containerQId != $scope.subContainerQId 
					&& isNullOrUndefined($scope.elementIdentity)) {
				$scope.parentIdentity = $stateParams.elementIdentity;
			}
		};
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
									_containerDef, _dataRecord);
			_dataRecord.containerId = _containerDef.id;
			_dataRecord.containerQId = _containerDef.qualifiedId;
			_dataRecord.hasSubContainers =  _dataRecord.identity != $stateParams.elementIdentity && 
							((!isNullOrUndefined(_containerDef.container) && _containerDef.container.length > 0)
											|| (ContentTemplateService.hasContentStackConfigItem(_containerDef)));
		
			ContentUtil.decorateData(_containerDef, _dataRecord, true, true);
		}
				
		$scope.render = function() {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
							enablix.template, $scope.subContainerQId);
			
			$scope.navigableHeader = !isNullOrUndefined($scope.navContentData);
			
			if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
				$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.containerDef.linkContainerQId);
			}

			if (!$scope.showLabel) {
				// do not populate when we have to show label so that it does not match attr id
				$scope.containerLabelAttrId = ContentTemplateService.getContainerLabelAttrId(
					enablix.template, $scope.containerDef.qualifiedId);
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
					
					var dataRecord = $scope.info;
					if (dataRecord.content) {
						dataRecord = dataRecord.content;
					}
					
					var dataRecord = isArray(dataRecord) && dataRecord.length > 0 ? dataRecord[0] : null;
					setUpSingleRecordData(dataRecord);
					
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
					
					var paginationData = isNullOrUndefined($scope.multiListLimit) || isStringAndEmpty($scope.multiListLimit) ? 
							undefined : { pageNum: 0, pageSize: $scope.multiListLimit };
					
					$scope.parentIdentity = $stateParams.elementIdentity;
							
					ContentDataService.getContentData(enablix.templateId, $scope.subContainerQId, $stateParams.elementIdentity,
						function(dataPage) {
							setUpMultiRecordData(dataPage);
						},
						function(errResp) {
							Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
						}, 
						paginationData);
				}
			}
			
			$scope.headers = [];
			
			angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
				
				if (containerAttr.id != $scope.containerLabelAttrId
						&& containerAttr.type != 'CONTENT_STACK') {
					
					var header = {
						"id": containerAttr.qualifiedId,
						"key" : containerAttr.id,
						"label" : containerAttr.label,
						"dataType" : containerAttr.type,
						"typeDef" : containerAttr
					};
					
					$scope.headers.push(header);
				}
				
			});
			
			$scope.expanded = $scope.expanded || true;
			
			$scope.showSubContainerList = function() {
				
				if ($state.includes('portal.enclosure')) {
				
					StateUpdateService.goToPortalEnclosureDetail($stateParams.enclosureId, $scope.subContainerQId);
					
				} else {
					
					var parentQId = $scope.parentContainerQId || QIdUtil.getParentQId($scope.subContainerQId); 
					StateUpdateService.goToPortalSubContainerList(
						parentQId, $scope.subContainerQId, $stateParams.elementIdentity);
				}
			}
		};
		
		$scope.hasData2 = function(_data) {
			return !isNullOrUndefined(_data) && (_data.length != 0); //!isArrayAndEmpty(_data) && !isStringAndEmpty(_data);
		}
		
		$scope.hasData = function(_data) {
			return !isNullOrUndefined(_data) && !isArrayAndEmpty(_data) && !isStringAndEmpty(_data);
		}
		
		$scope.isRenderable = function(_contentDef, _data) {
			return $scope.hasData(_data);
		}
		
		$scope.render();
		
		PubSub.subscribe(ContentDataService.contentChangeEventId($scope.subContainerQId), function() {
			
			if ($scope.category === 'content-stack') {
				StateUpdateService.reload();
			} else {
				$scope.info = null; // setting it to null so that we pick up fresh data from database
				$scope.render();
			}
		});
		
	}]);
