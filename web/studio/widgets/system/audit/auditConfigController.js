enablix.studioApp.controller('AuditController', ['$scope', '$stateParams', 'RESTService', 'AuditConfigService', '$rootScope', 'StateUpdateService',
                                                 function($scope, $stateParams, RESTService, auditConfigService, $rootScope, StateUpdateService) {  
	$scope.breadcrumbList = 
		[
		 { label: "Setup" },
		 { label: "Audit" }
		 ];
	var userData=JSON.parse(window.localStorage.getItem("userData"));

	var defaultSearchJSONTemplate = JSON.parse('{   	"filters" : {       "activitycat" : "CONTENT"     },   	"filterMetadata" : {       "activitycat" : {         "field" : "activity.category",         "operator" : "EQ",         "dataType" : "STRING"       }     }, 	"pagination" : {       	"pageSize" : 10,       	"pageNum" : 0,     	"sort" : {           	"field" : "activityTime",           	"direction" : "DESC"         }     } }');
	var searchJSONTemplate = JSON.parse('{   	"filters" : {            },   	"filterMetadata" : {            }, 	"pagination" : {       	"pageSize" : 10,       	"pageNum" : 0,     	"sort" : {           	"field" : "activityTime",           	"direction" : "DESC"         }     } }');
	var activityUserMetaData = JSON.parse('{"searchKey":"auditUser","searchOperator":"EQ","searchField":"actor.name","searchDataType":"STRING"}');
	var activityTypeMetaData = JSON.parse('{"searchKey":"auditActivityType","searchOperator":"EQ","searchField":"activity.activityType","searchDataType":"STRING"}');
	var eventOccMetaData = JSON.parse('{"searchKey":"auditEventOcc","searchOperator":"GTE","searchField":"activityTime","searchDataType":"DATE"}');
	$scope.filteredAudits = [];
	$scope.itemsPerPage = 10;
	$scope.currentPage = 4;


	$scope.navToContent = function(auditRow) {
		var _containerQId = auditRow.activity.containerQId;
		var _contentIdentity = auditRow.activity.itemIdentity;
		StateUpdateService.goToPortalContainerBody(_containerQId, _contentIdentity, 'single', _containerQId);
	}

	$scope.pageChanged = function() {
		var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
		var end = begin + $scope.itemsPerPage;
		$scope.filteredAudits = $scope.auditData.content.slice(begin, end);
	};


	$scope.userLst=[];
	$scope.activityTypeLst=[]; 
	$scope.searchCriteria = {};
	auditConfigService.getAuditData(userData.tenantId,defaultSearchJSONTemplate ,function(auditData) {
		//prompt('auditData',JSON.stringify(auditData));
		$scope.auditData=auditData;
		var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
		var end = begin + $scope.itemsPerPage;
		$scope.filteredAudits = $scope.auditData.content.slice(begin, end);
		getAllUsers(initUserDropDown);
		getActivityTypes(initActivityTypes);	
	});

	var getAllUsers= function(_success){
		RESTService.getForData('systemuser', null, null, function(data) {
			_success(data);	    	
		}, function() {    		
			Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
		});
	};
	var getActivityTypes= function(_success){
		RESTService.getForData('getAuditActivityTypes', null, null, function(data) {
			_success(data);	    	
		}, function() {    		
			Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
		});
	};
	var initUserDropDown = function (data){
		var size = data.length;
		for(var i=0; i<size;i++)
		{
			var UserObj= {
					label: data[i].profile.name,
					id: data[i].profile.name,
			};
			$scope.userLst.push(UserObj);
		}
	}
	var initActivityTypes = function(data)
	{
		$.each( data, function(index,value){		
			var activityObj= {
					label: value,
					id: index,
			};
			$scope.activityTypeLst.push(activityObj);
		})
	}
	var getEventDate = function(noDeduced){
		var m_names = new Array("Jan", "Feb", "Mar", 
				"Apr", "May", "Jun", "Jul", "Aug", "Sep", 
				"Oct", "Nov", "Dec");
		var d = new Date();
		var curr_month;
		var curr_date;
		curr_date = d.getDate()-$scope.searchCriteria.eventOccurence;
		if(curr_date<0)
		{
			curr_date = 30+curr_date;
			curr_month = d.getMonth()-1;
		}
		else if (curr_date==0){
			curr_date=curr_date+1;
			curr_month = d.getMonth();
		}
		else{
			curr_date = d.getDate()-$scope.searchCriteria.eventOccurence;
			curr_month = d.getMonth();
		}
		var curr_year = d.getFullYear().toString().substr(2,2);
		return curr_date + "-" + m_names[curr_month] 
		+ "-" + curr_year;
	}
	$scope.resetAudit = function(){
		var d = new Date();
		$scope.searchCriteria = {};
		searchJSONTemplate = JSON.parse('{   	"filters" : {            },   	"filterMetadata" : {            }, 	"pagination" : {       	"pageSize" : 10,       	"pageNum" : 0,     	"sort" : {           	"field" : "activityTime",           	"direction" : "DESC"         }     } }');
		auditConfigService.getAuditData(userData.tenantId,defaultSearchJSONTemplate,function(auditData) {
			//prompt('auditData in search',JSON.stringify(auditData));
			$scope.auditData=auditData;
			var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
			var end = begin + $scope.itemsPerPage;
			$scope.filteredAudits = $scope.auditData.content.slice(begin, end);
		});
	}
	$scope.searchAudit = function(){
		//alert(""+$scope.searchCriteria.eventOccurence);
		//alert(""+$scope.searchCriteria.activity);
		//alert(""+$scope.searchCriteria.user);
		formSearchObject();
		//prompt("Search JSON Template",JSON.stringify(searchJSONTemplate))	;
		auditConfigService.getAuditData(userData.tenantId,searchJSONTemplate,function(auditData) {
			//prompt('auditData in search',JSON.stringify(auditData));
			$scope.auditData=auditData;
			var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
			var end = begin + $scope.itemsPerPage;
			$scope.filteredAudits = $scope.auditData.content.slice(begin, end);
		});
	}
	var formSearchObject = function(){
		if($scope.searchCriteria.user!=undefined){
			appendIndividualSearchCriteria(activityUserMetaData ,$scope.searchCriteria.user);
		}
		if($scope.searchCriteria.activity!=undefined){
			appendIndividualSearchCriteria(activityTypeMetaData,$scope.searchCriteria.activity);
		}
		if($scope.searchCriteria.eventOccurence!=undefined){
			appendIndividualSearchCriteria(eventOccMetaData,getEventDate());
		}
	}



	var appendIndividualSearchCriteria = function(searchMetadata, searchVal){
		var searchKey = searchMetadata["searchKey"];
		searchJSONTemplate.filters[searchKey]=searchVal;
		var filterMetadata = JSON.parse('{"field":"","dataType":"","operator":""}');
		filterMetadata["field"]=searchMetadata["searchField"];
		filterMetadata["operator"]=searchMetadata["searchOperator"];
		filterMetadata["dataType"]=searchMetadata["searchDataType"];;
		searchJSONTemplate.filterMetadata[searchKey]=filterMetadata;

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