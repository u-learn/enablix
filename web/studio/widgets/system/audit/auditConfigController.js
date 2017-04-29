enablix.studioApp.controller('AuditController', ['$scope', '$stateParams', '$filter', 'RESTService', 'AuditConfigService', '$rootScope', 'StateUpdateService', 'Notification', '$q',
    function($scope, $stateParams, $filter, RESTService, auditConfigService, $rootScope, StateUpdateService, Notification, $q) {


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
                desc: "Name",
                valueFn: function(record) {
                    return record.activity.itemTitle;
                },
                sortProperty: "activity.itemTitle"
            },
            {
                desc: "Activity Type",
                valueFn: function(record) {
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
            	return record.activity && !(
                        record.activity.activityType === 'DOC_PREVIEW' || record.activity.activityType === "DOC_DOWNLOAD" 
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
        $scope.dataFilters.activitycat = "CONTENT";


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
                {
                    id: "auditActivityType",
                    type: "multi-select",
                    name: "Activity Type",
                    masterList: function() { // This must return a promise
                        var activityTypeLst = [];
                        RESTService.getForData('getAuditActivityTypes', null, null, function(data) {

                            $.each(data, function(index, value) {
                                var activityObj = {
                                    label: value,
                                    id: index,
                                };

                                activityTypeLst.push(activityObj);

                                activityTypeLst.sort(function(a, b) {
                                    return a.label === b.label ? 0 : (a.label < b.label ? -1 : 1);
                                });

                                $scope.activityTypeNameMap[index] = value;
                            });

                        }, function() {
                            Notification.error({
                                message: "Error loading Activity data",
                                delay: enablix.errorMsgShowTime
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
                            });

                            return returnVal;
                        } else
                            return null;
                    }
                },
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
            $scope.dataFilters.activitycat = "CONTENT";
            fetchAuditResult();
        }

        var fetchAuditResult = function() {
            auditConfigService.getAuditData($scope.dataFilters, $scope.pagination, $scope.auditSearch.filterMetadata,
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
        fetchAuditResult();
    }
]);