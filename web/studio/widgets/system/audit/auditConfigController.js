enablix.studioApp.controller('AuditController', 
				['$scope', '$stateParams', '$filter', 'RESTService', 'AuditConfigService', '$rootScope', 'StateUpdateService', 'Notification',
		 function($scope,   $stateParams,   $filter,   RESTService,   auditConfigService,   $rootScope,   StateUpdateService,   Notification) {
					
	
	var userData=JSON.parse(window.localStorage.getItem("userData"));


	$scope.navToContent = function(record) {
		var _containerQId = record.activity.containerQId;
		var _contentIdentity = record.activity.itemIdentity;
		StateUpdateService.goToPortalContainerBody(_containerQId, _contentIdentity, 'single', _containerQId);
	}

	$scope.pagination = {
			pageSize: enablix.defaultPageSize,
			pageNum: 0,
			sort: {
				field: "createdAt",
				direction: "DESC"
			}
	}

	$scope.activityTypeNameMap = {};
	
	$scope.tableHeaders =
		[{
			desc: "Name",
			valueFn: function(record) {
				return record.activity.itemTitle;
			},
			sortProperty: "activity.itemTitle"
		},
		{
			desc: "Activity Type",
			valueFn: function(record) { return $scope.activityTypeNameMap[record.activity.activityType]; },
			sortProperty: "activity.activityType"
		},
		{
			desc: "Date",
			valueFn: function(record) { return $filter('ebDate')(record.activityTime); },
			sortProperty: "activityTime"
		},
		{
			desc: "User",
			valueFn: function(record) { return record.actor.name },
			sortProperty: "actor.name"
		}];
	
	$scope.tableRecordActions = 
		[{
			actionName: "View Details",
			tooltip: "Details",
			iconClass: "fa fa-eye",
			tableCellClass: "details",
			actionCallbackFn: $scope.navToContent,
			checkApplicable: function(record) { return true; }
		}];
	
	
	$scope.setPage = function(pageNum) {
		$scope.pagination.pageNum = pageNum;
		fetchAuditResult();
	}
	
	$scope.dataList = [];
	$scope.userLst=[];
	$scope.activityTypeLst=[]; 
	$scope.searchCriteria = {};
	$scope.dataFilters = {};
	$scope.dataFilters.activitycat = "CONTENT";
	
	var fetchAuditResult = function() {
		auditConfigService.getAuditData($scope.dataFilters,$scope.pagination ,function(dataPage) {
			$scope.dataList = dataPage.content;
			$scope.pageData = dataPage;
		}, function(errorData) {
			Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
		});
	}

	var getAllUsers= function(_success){
		RESTService.getForData('systemuser', null, null, function(data) {
			_success(data);	    	
		}, function() {    		
			Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
		});
	};
	
	var getActivityTypes= function(_success) {
		RESTService.getForData('getAuditActivityTypes', null, null, function(data) {
			_success(data);	    	
		}, function() {    		
			Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
		});
	};
	
	var initUserDropDown = function (data) {
		var size = data.length;
		for (var i=0; i<size;i++) {
			var UserObj= {
					label: data[i].profile.name,
					id: data[i].profile.name,
			};
			$scope.userLst.push(UserObj);
		}
	}
	
	var initActivityTypes = function(data) {
		$.each( data, function(index,value){		
			var activityObj= {
					label: value,
					id: index,
			};
			$scope.activityTypeLst.push(activityObj);
			
			$scope.activityTypeLst.sort(function(a,b) {
				return a.label === b.label ? 0 : (a.label < b.label ? -1 : 1);
			});
			
			$scope.activityTypeNameMap[index] = value;
		});
	}
	
	getAllUsers(initUserDropDown);
	getActivityTypes(initActivityTypes);
	
	auditConfigService.getAuditData($scope.dataFilters,$scope.pagination ,function(dataPage) {
		$scope.dataList = dataPage.content;
		$scope.pageData = dataPage;
	}, function(errorData) {
		Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
	});
	
	var getEventDate = function(noDeduced){
		var m_names = new Array("Jan", "Feb", "Mar", 
				"Apr", "May", "Jun", "Jul", "Aug", "Sep", 
				"Oct", "Nov", "Dec");
		var d = new Date();
		var curr_month;
		var curr_date;
		curr_date = d.getDate()-$scope.searchCriteria.eventOccurence;
		if(curr_date < 0) {
			curr_date = 30+curr_date;
			curr_month = d.getMonth()-1;
		}
		else if (curr_date==0){
			curr_date=curr_date+1;
			curr_month = d.getMonth();
		} else {
			curr_date = d.getDate()-$scope.searchCriteria.eventOccurence;
			curr_month = d.getMonth();
		}
		var curr_year = d.getFullYear().toString().substr(2,2);
		return curr_date + "-" + m_names[curr_month] + "-" + curr_year;
	}
	
	$scope.resetAudit = function(){
		var d = new Date();
		$scope.searchCriteria = {};
		$scope.dataFilters = {};
		$scope.dataFilters.activitycat = "CONTENT";
		fetchAuditResult();
	}
	
	$scope.searchAudit = function(){
		formSearchObject();
		fetchAuditResult();
	}
	
	var formSearchObject = function(){
		if($scope.searchCriteria.user!=undefined){
			$scope.dataFilters.auditUser=$scope.searchCriteria.user;
		}
		if($scope.searchCriteria.activity!=undefined){
			$scope.dataFilters.auditActivityType=$scope.searchCriteria.activity;
		}
		if($scope.searchCriteria.eventOccurence!=undefined){
			$scope.dataFilters.auditEventOcc=getEventDate();
		}
	}

	$scope.eventOccurenceLst = [{
		label: 'Last 1 day',
		id: '1'
	},{
		label: 'Last 3 days',
		id: '3'
	},{
		label: 'Last 1 Week',
		id: '7'
	},{
		label: 'Last Month',
		id: '30'
	}];
}	
]);