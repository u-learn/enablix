enablix.studioApp.controller('AuditController', 
		   ['$scope', '$stateParams', '$filter', 'RESTService', 'AuditConfigService', '$rootScope', 'StateUpdateService', 'Notification', '$q',
    function($scope,  $stateParams,    $filter,   RESTService,   AuditConfigService,   $rootScope,   StateUpdateService,   Notification,   $q) {


        $scope.navToContent = function(record) {
            var _containerQId = record.activity.containerQId;
            var _contentIdentity = record.activity.itemIdentity;
            StateUpdateService.goToPortalContainerBody(_containerQId, _contentIdentity, 'single', _containerQId);
        }

        $scope.pagination = {
            pageSize: enablix.defaultPageSize,
            pageNum: 0,
            sort: {
                field: "activityTime",
                direction: "DESC"
            }
        }

        $scope.activityTypeNameMap = {};

        $scope.tableHeaders = [{
                desc: "Details",
                valueFn: function(record) {
                	
                	if (record.activity.category === "CONTENT" || record.activity.category === "NAVIGATION") {
                		return record.activity.itemTitle;
                		
                	} else if (record.activity.category === "SEARCH") {
                		
                		if (record.activity.activityType === "SUGGESTED_SEARCH") {
                		
                			var suggType = record.activity.suggestionType;
                			var retVal = "Accessed Via Search: ";
                			
                			if (suggType === "BizDimensionNode" || suggType === 'BizContentNode') {
                				retVal += "All ";
                			}
                			
                			return retVal + record.activity.searchTerm
                		}
                		return "Searched: " + record.activity.searchTerm;
                		
                	} else if (record.activity.activityType === "LOGIN") {
                		return "User Logged In";
                		
                	} else if (record.activity.activityType === "LOGOUT") {
                		return "User Logged Out";
                	}
                	
                    return "";
                }
            },
            {
                desc: "Activity Type",
                valueFn: function(record) {
                	if (record.activity.activityType === "SUGGESTED_SEARCH") {
                		return "Search";
                	}
                    return $scope.activityTypeNameMap[record.activity.activityType];
                },
                sortProperty: "activity.activityType"
            },
            {
                desc: "Date",
                valueFn: function(record) {
                    return $filter('ebDate')(record.activityTime);
                },
                sortProperty: "activityTime"
            },
            {
                desc: "User",
                valueFn: function(record) {
                    return record.actor.name
                },
                sortProperty: "actor.name"
            }
        ];

        $scope.tableRecordActions = [{
            actionName: "View Details",
            tooltip: "Details",
            iconClass: "fa fa-eye",
            tableCellClass: "details",
            actionCallbackFn: $scope.navToContent,
            checkApplicable: function(action, record) {
            	return record.activity 
            			&& (record.activity.category === "CONTENT" 
            				|| record.activity.category === "NAVIGATION")
            			&& !(record.activity.activityType === 'DOC_PREVIEW' 
            				|| record.activity.activityType === "DOC_DOWNLOAD" 
            				|| record.activity.activityType === 'DOC_UPLOAD');
            }
        }];


        $scope.setPage = function(pageNum) {
            $scope.pagination.pageNum = pageNum;
            fetchAuditResult();
        }

        $scope.dataList = [];
        $scope.userLst = [];
        $scope.activityTypeLst = [];
        $scope.dataFilters = {};

        var getEventDate = function(eventOccurence) {

            var m_names = new Array("Jan", "Feb", "Mar",
                "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec");

            var d = new Date();
            var curr_month;
            var curr_date = d.getDate() - eventOccurence;

            if (curr_date < 0) {

                curr_date = 30 + curr_date;
                curr_month = d.getMonth() - 1;

            } else if (curr_date == 0) {

                curr_date = curr_date + 1;
                curr_month = d.getMonth();

            } else {
                curr_date = d.getDate() - eventOccurence;
                curr_month = d.getMonth();
            }

            var curr_year = d.getFullYear().toString().substr(2, 2);
            return curr_date + "-" + m_names[curr_month] + "-" + curr_year;
        }

        $scope.resetAudit = function() {
            $scope.pagination.pageNum = 0;
            $scope.dataFilters = {};
            $scope.dataFilters.activitycat = "CONTENT";
            fetchAuditResult();
        }

        $scope.searchAudit = function(_pagination, freshSearch) {
            fetchAuditResult();
        }

        var activityTypeFilter = {
            id: "auditActivityType",
            type: "multi-select",
            name: "Activity Type",
            masterList: function() { // This must return a promise
                
            	var activityTypeLst = [];
                
                var data = $scope.activityTypeNameMap = AuditConfigService.getActivityTypes();
                
                angular.forEach(data, function(value, key) {
                    
                	var activityObj = {
                        label: value,
                        id: key,
                    };

                    activityTypeLst.push(activityObj);

                    activityTypeLst.sort(function(a, b) {
                        return a.label === b.label ? 0 : (a.label < b.label ? -1 : 1);
                    });

                });

                var deferred = $q.defer();
                deferred.resolve(activityTypeLst);
                
                return deferred.promise;
            },
            validateBeforeSubmit: function(_selectedValues) {
                return true;
            },
            filterValueTransformer: function(_selectedValues) {

                if (_selectedValues && _selectedValues.length > 0) {
                    var returnVal = [];

                    angular.forEach(_selectedValues, function(val) {
                        returnVal.push(val.id);
                        if (val.id === "SEARCH_FREE_TEXT") {
                        	returnVal.push("SUGGESTED_SEARCH");
                        }
                    });

                    return returnVal;
                } else
                    return null;
            },
            defaultValues: function() {
                
            	var activityTypeLst = [];
            	var data = AuditConfigService.getActivityTypes();
                
                angular.forEach(data, function(value, key) {
                    
                	var activityObj = {
                        label: value,
                        id: key,
                    };

                    activityTypeLst.push(activityObj);
                });
                
                return activityTypeLst;
            }
        };
        
        $scope.auditSearch = {
            id: "audit-search",
            name: "Audit Search Panel",
            filterMetadata: {
                "activitycat": {
                    "field": "activity.category",
                    "operator": "EQ",
                    "dataType": "STRING"
                },
                "auditUser": {
                    "field": "actor.name",
                    "operator": "IN",
                    "dataType": "STRING"
                },
                "auditActivityType": {
                    "field": "activity.activityType",
                    "operator": "IN",
                    "dataType": "STRING"
                },
                "auditEventOcc": {
                    "field": "activityTime",
                    "operator": "GTE",
                    "dataType": "DATE"
                }
            },
            filters: [{
                    id: "auditUser",
                    type: "multi-select",
                    name: "User",
                    masterList: function() { // This must return a promise
                        var userList = [];
                        RESTService.getForData('systemuser', null, null, function(data) {
                            var size = data.length;

                            for (var i = 0; i < size; i++) {

                                var UserObj = {
                                    label: data[i].name,
                                    id: data[i].name,
                                };
                                userList.push(UserObj);
                            }
                            
                            userList.push({
                            	label: "System",
                            	id: "System"
                            });

                        }, function() {
                            Notification.error({
                                message: "Error loading user data",
                                delay: enablix.errorMsgShowTime
                            });
                        });

                        var deferred = $q.defer();
                        deferred.resolve(userList);

                        return deferred.promise;
                    },
                    validateBeforeSubmit: function(_selectedValues) {
                        return true;
                    },
                    filterValueTransformer: function(_selectedValues) {

                        if (_selectedValues && _selectedValues.length > 0) {
                            var returnVal = [];

                            angular.forEach(_selectedValues, function(val) {
                                returnVal.push(val.id);
                            });

                            return returnVal;
                        } else
                            return null;
                    }
                },
                activityTypeFilter,
                {
                    id: "auditEventOcc",
                    type: "multi-select",
                    name: "Time",
                    masterList: function() { // This must return a promise
                        var eventOccurenceLst = [{
                                label: 'Last 1 day',
                                id: '1'
                            },
                            {
                                label: 'Last 3 days',
                                id: '3'
                            },
                            {
                                label: 'Last 1 Week',
                                id: '7'
                            },
                            {
                                label: 'Last Month',
                                id: '30'

                            }
                        ];

                        var deferred = $q.defer();
                        deferred.resolve(eventOccurenceLst);

                        return deferred.promise;
                    },
                    validateBeforeSubmit: function(_selectedValues) {
                        return true;
                    },
                    filterValueTransformer: function(_selectedValues) {

                        if (_selectedValues && _selectedValues.length > 0) {
                            var returnVal = [];

                            angular.forEach(_selectedValues, function(val) {
                                returnVal.push(getEventDate(val.id));
                            });

                            orderedDates = returnVal.sort(function(a, b) {
                                return Date.parse(a) > Date.parse(b);
                            });

                            return orderedDates[0];
                        } else
                            return null;
                    }
                }
            ],
        };

        $scope.onAuditSearch = function(_selectedFilters) {
            $scope.pagination.pageNum = 0;
            removeNullProperties(_selectedFilters);
            $scope.dataFilters = _selectedFilters;
            fetchAuditResult();
        }

        var fetchAuditResult = function() {
            AuditConfigService.getAuditData($scope.dataFilters, $scope.pagination, $scope.auditSearch.filterMetadata,
                function(dataPage) {
                    $scope.dataList = dataPage.content;
                    $scope.pageData = dataPage;
                },
                function(errorData) {
                    Notification.error({
                        message: "Error retrieving data",
                        delay: enablix.errorMsgShowTime
                    });
                });
        }
        
        var defaultActivityTypeFilterValue = activityTypeFilter.filterValueTransformer(activityTypeFilter.defaultValues());
        $scope.dataFilters[activityTypeFilter.id] = defaultActivityTypeFilterValue;
        removeNullProperties($scope.dataFilters);
        
        fetchAuditResult();
    }
]);