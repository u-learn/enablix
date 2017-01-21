enablix.studioApp.controller('SystemContentBrowserController', 
			['$scope', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', '$modalInstance', 'QIdUtil', 'Notification', 'preSelectedRecords',
    function ($scope,   ContentTemplateService,   ContentDataService,   ContentUtil,   $modalInstance,   QIdUtil,   Notification,   preSelectedRecords) {

		$scope.selectedContentItems = angular.copy(preSelectedRecords);
		$scope.chunkedSelectedContent = [];
		$scope.browsePath = [];

		function BrowserPath() {
			this.path = [{
				label: "Home",
				qualifiedId: "~root~",
				type: "home"
			}];
			
			this.addContainer = function(containerDef) {
				
				this.path.push({
					label: containerDef.label,
					type: "container",
					containerDef: containerDef,
					qualifiedId: containerDef.qualifiedId
				});
				
			}
			
			this.addContentInstance = function(dataRecord, containerDef) {
				
				this.path.push({
					label: dataRecord._title,
					type: "instance",
					containerDef: containerDef,
					qualifiedId: containerDef.qualifiedId,
					dataRecord: dataRecord
				});
				
			}
			
			this.addEnclosure = function(enclosureDef) {
				
				this.path.push({
					label: enclosureDef.label,
					type: "enclosure",
					enclosureDef: enclosureDef,
					qualifiedId: enclosureDef.id
				});
				
			}
			
			this.moveToPathIndex = function(index) {
				
				this.path = this.path.slice(0, index + 1);
				var lastItem = this.path[index];
				
				if (lastItem.type === "home") {
					initRootContainer();
				} else if (lastItem.type === "container") {
					showContainerView(lastItem.containerDef);
				} else if (lastItem.type === "instance") {
					showContentInstanceView(lastItem.dataRecord, lastItem.containerDef);
				} else if (lastItem.type === "enclosure") {
					showEnclosureView(lastItem.enclosureDef);
				}
			}
			
			this.getLastContainer = function() {
				
				for (var i = this.path.length - 1; i > 0; i--) {
				
					var pathItem = this.path[i];
					if (pathItem.type === "container") {
						return pathItem.containerDef;
					}
				}
				
				return null;
			}
			
			this.getLastDataInstance = function() {
				
				for (var i = this.path.length - 1; i > 0; i--) {
				
					var pathItem = this.path[i];
					if (pathItem.type === "instance") {
						return pathItem.dataRecord;
					}
				}
				
				return null;
			}
		}
		
		$scope.browserPath = new BrowserPath();
		
		var createContainerNavItem = function(_cntnrQId) {
			
			var container = ContentTemplateService.getContainerDefinition(enablix.template, _cntnrQId);
			
			if (!container || container.refData) {
				return null;
			}
			
			var navItem = {
				"id" : container.id,
				"qualifiedId" : container.qualifiedId,
				"label" : container.label,
				"containerDef" : container,
				"type": "container"
			};
			
			return navItem;
		}
		
		var initRootContainer = function() {
			
			$scope.containerList = [];
			$scope.topNavContainers = ContentTemplateService.getPortalTopNavItemContainers();
			
			angular.forEach($scope.topNavContainers, function(cntnr) {
				var navItem = createContainerNavItem(cntnr.qualifiedId);
				if (navItem != null) {
					$scope.containerList.push(navItem);
				}
			});
			
			var enclosureList = ContentTemplateService.getPortalTopNavEnclosures();
			angular.forEach(enclosureList, function(enclosure) {
				
				var navItem = {
						"id" : enclosure.id,
						"qualifiedId" : enclosure.id,
						"label" : enclosure.label,
						"type": "enclosure",
						"enclosureDef": enclosure
					};
				
				$scope.containerList.push(navItem);
				
			});
			
			$scope.containerList.sort(sortContainerByLabel);
			$scope.chunkedContainerList = chunkArray($scope.containerList, 3);
			
			$scope.currentContainer = null;
			$scope.currentContentRecord = null;
			
			$scope.contentList = null;
		}

		
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		};
		
		$scope.doneBrowsing = function() {
			$modalInstance.close($scope.selectedContentItems);
		};
		
		$scope.addToSelectedContent = function(dataRecord) {
			
			var indx = $scope.indexInSelectedContent(dataRecord.identity);
			
			if (indx == -1) {
				
				var containerQId = $scope.currentContainer.linkContainerQId ? 
						$scope.currentContainer.linkContainerQId : $scope.currentContainer.qualifiedId;
						
				$scope.selectedContentItems.push({
					identity: dataRecord.identity,
					label: dataRecord._title,
					qualifiedId: containerQId,
					containerLabel: $scope.currentContainer.label
				});
			}
		};
		
		$scope.indexInSelectedContent = function(_dataRecordIdentity) {
			for (var i = 0; i < $scope.selectedContentItems.length; i++) {
				if ($scope.selectedContentItems[i].identity == _dataRecordIdentity) {
					return i;
				}
			}
			return -1;
		}
		
		$scope.chunkArray = function(array, size) {
			return chunkArray(array, size);
		}
		
		$scope.removeFromSelectedContent = function(dataRecord) {
			$scope.removeFromSelectedContentByIdentity(dataRecord.identity);
		};
		
		$scope.selectedContentItemRemoved = function(_removedContentItem, $index) {
			if ($scope.contentList) {
				angular.forEach($scope.contentList, function(contentItem) {
					if (contentItem.identity == _removedContentItem.identity) {
						contentItem._selected = false;
					}
				});
			}
		}
		
		$scope.removeFromSelectedContentByIdentity = function(_dataIdentity) {
			var indx = $scope.indexInSelectedContent(_dataIdentity);
			if (indx != -1) {
				$scope.selectedContentItems.splice(indx, 1);
			}
		}
		
		$scope.containerSelected = function(_navItem) {
			
			if (_navItem.qualifiedId === '~root~') {
				
				initRootContainer();
				
			} else if (_navItem.type === 'container') {
				
				$scope.browserPath.addContainer(_navItem.containerDef);
				showContainerView(_navItem.containerDef)
				
			} else if (_navItem.type === 'enclosure') {
				
				$scope.browserPath.addEnclosure(_navItem.enclosureDef);
				showEnclosureView(_navItem.enclosureDef)
			}
			
		}
		
		var showEnclosureView = function(enclosureDef) {
			
			$scope.contentList = null;
			$scope.containerList = [];
			
			angular.forEach(enclosureDef.childContainer, function(childContainer) {
				var navItem = createContainerNavItem(childContainer.id);
				if (navItem != null) {
					$scope.containerList.push(navItem);
				}
			});
			
			$scope.containerList.sort(sortContainerByLabel);
			$scope.chunkedContainerList = chunkArray($scope.containerList, 3);
		}
		
		var sortContainerByLabel = function(c1, c2) {
			return c1.label == c2.label ? 0 : (c1.label < c2.label ? -1 : 1);
		}
		
		var showContainerView = function(containerDef) {
		
			$scope.containerList = [];
			
			$scope.pagination = {
					pageSize: enablix.defaultPageSize,
					pageNum: 0,
					sort: {
						field: "createdAt",
						direction: "DESC"
					}
				};
			
			$scope.currentContentRecord = $scope.browserPath.getLastDataInstance();
			var parentIdentity = $scope.currentContentRecord ? $scope.currentContentRecord.identity : null;
			
			var fetchData = function() {
				
				ContentDataService.getContentData(enablix.templateId, containerDef.qualifiedId, parentIdentity, function(data) {
					
					$scope.contentList = data.content;
					$scope.pageData = data;
					
					ContentUtil.resolveAndAddTitle(containerDef, $scope.contentList);
					
					// mark selected records
					angular.forEach($scope.contentList, function(contentItem) {
						if ($scope.indexInSelectedContent(contentItem.identity) != -1) {
							contentItem._selected = true; 
						}
					});
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				}, $scope.pagination);
			}
			
			fetchData();

			var labelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, containerDef.qualifiedId);
			var labelAttrItemDef = ContentTemplateService.getContentItem(containerDef, labelAttrId);
			var labelHeaderDesc = labelAttrItemDef ? labelAttrItemDef.label : "Title";
					
			
			$scope.tableHeaders =
				 [{
					 desc: labelHeaderDesc,
					 valueFn: function(record) { return record._title; }
				 }];
				 
			$scope.tableRecordActions = 
				[{
					actionName: "Drilldown",
					tooltip: "Sub-content",
					iconClass: "fa fa-play",
					tableCellClass: "edit",
					actionCallbackFn: $scope.navToContentInstance,
					checkApplicable: function(action, record) {
						return $scope.currentContainer.container && $scope.currentContainer.container.length > 0;
					}
				}];
			
			$scope.tableRecordSelectAction = {
					
				actionCallbackFn: function(record) {
					if (record._selected) {
						$scope.addToSelectedContent(record);
					} else {
						$scope.removeFromSelectedContent(record);
					}
				}
			};
			
			$scope.setPage = function(pageNum) {
				$scope.pagination.pageNum = pageNum;
				fetchData();
			}
			
			$scope.currentContainer = containerDef;
			
			$scope.containerList.sort(sortContainerByLabel);
			$scope.chunkedContainerList = chunkArray($scope.containerList, 3);

		}
		
		$scope.navToContentInstance = function(dataRecord) {
			
			var containerDef = $scope.browserPath.getLastContainer();
			$scope.browserPath.addContentInstance(dataRecord, containerDef);
			
			showContentInstanceView(dataRecord, containerDef);
		}
		
		var showContentInstanceView = function(dataRecord, containerDef) {
			
			$scope.currentContentRecord = dataRecord;

			angular.forEach(containerDef.container, function(childContainer) {
				var navItem = createContainerNavItem(childContainer.qualifiedId);
				if (navItem != null) {
					$scope.containerList.push(navItem);
				}
			});
			
			$scope.contentList = null;
			
			$scope.containerList.sort(sortContainerByLabel);
			$scope.chunkedContainerList = chunkArray($scope.containerList, 3);
		}
		
		$scope.navToBreadcrumbItem = function(breadcrumbItem, itemIndx) {
			$scope.browserPath.moveToPathIndex(itemIndx);
		}
		
		initRootContainer();

		
}]);



