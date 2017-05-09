enablix.studioApp.factory('ReportService', 
	[	        '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'DataSearchService', 'ContentTemplateService', 'ActivityMetricService',
	 	function($q,   $filter,   RESTService,   Notification,   StateUpdateService,   DataSearchService,   ContentTemplateService, ActivityMetricService) {
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
			/** ----------------------------------------- Report Definitions start -----------------------------------------**/
		
			var eventOccurenceLst = [
				{
					label: 'Last Day',
		            id: '1'
		        },	
				{
	                label: 'Last 7 Days',
	                id: '7'
	            },
	            {
	                label: 'Last 30 Days',
	                id: '30'
	            },
	            {
	                label: 'Last 90 Days',
	                id: '90'
	            }
            ];
			var eventTrend = [
				{
					label: 'Last 7 Days',
					id: '7'
				},	
				{
					label: 'Last 30 Days',
					id: '30'
				},
				{
					label: 'Last 90 Days',
					id: '90'
				}
				];
			var activityTrend = [
				{
					label: 'Daily',
					id: 'daily'
				},
				{
					label: 'Weekly',
					id: 'weekly'
				},	
				{
					label: 'Monthly',
					id: 'monthly'
				},
				
				{
					label: 'Yearly',
					id: 'yearly'
				}
				];
			var activityMetricReport = {
					id: "activity-metric-calculator",
					name: "Activity Summary",
					heading: "Activity Summary",
					type: "TABULAR",
					init: function($scope) {
						
					},
					columndetails : [
					    { head: 'Metric', column: 'metricName' },
					    { head: 'Value', column: 'metricValue' }
					],
					filterMetadata: {
		                "activityMetricTime": {
		                    "field": "asOfDate",
		                    "operator": "GTE",
		                    "dataType": "DATE"
		                }
		            },
					filters: 
						[
							 {
				                    id: "activityMetricTime",
				                    type: "multi-select",
				                    options: {
				                    	singleSelect: true
				                    },
				                    name: "Time",
				                    masterList: function() { // This must return a promise
				                        

				                        var deferred = $q.defer();
				                        deferred.resolve(eventOccurenceLst);

				                        return deferred.promise;
				                    },
				                    validateBeforeSubmit: function(_selectedValues) {
										
										if (_selectedValues.length == 0) {
											this.errorMessage = "Please select a value";
											return false;
										}
										
										this.errorMessage = null;
										return true;
									},
				                    defaultValue: function() {
				                    	 var returnVal = [];
				                    	 // By Default setting it to Last One Day
				                    	 returnVal.push(eventOccurenceLst[0]);
				                    	 return returnVal;
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
					dataTransformer: function(_data, _filterValues) {
						// change heading
						this.heading = this.name + " ( As of " + $filter('ebDateTime')(_data.asOfDate) + ")";
						return _data.metricData;
					},
					fetchData: function(_dataFilters, _onSuccess, _onError) {
						var ACTIVITY_METRIC = "getActivityMetric";
						var searchFilters = _dataFilters || {};
						ActivityMetricService.getActivityMetric(ACTIVITY_METRIC, searchFilters, _onSuccess, _onError)
									
					} 
			};
			enablix.reports.push(activityMetricReport);
			
			
			var activityTrendReport = {
					id: "activity-trend-calculator",
					name: "Activity Trend",
					heading: "Activity Trend",
					type: "MULTILINE",
					init: function($scope) {

					},
					columndetails :
					{				
						'Number of Logins': {column: 'ACTMETRIC2'},
						'Number of Distinct Logins': {column: 'ACTMETRIC3'},
						'Content Add': {column: 'ACTMETRIC4'},
						'Content Updates': {column: 'ACTMETRIC5'},
				        'Content Access': {column: 'ACTMETRIC6'},
				        'Content Preview': {column: 'ACTMETRIC7'},
				        'Content Download': {column: 'ACTMETRIC8'},
				        'Searches': {column: 'ACTMETRIC9'}
				    },
					filterMetadata: {
						
					},
					filters: 
						[
							{
								id: "activityMetricTrend",
								type: "multi-select",
								options: {
			                    	singleSelect: true
			                    },
								name: "Trend",
								masterList: function() { // This must return a promise

									var deferred = $q.defer();
									deferred.resolve(activityTrend);

									return deferred.promise;
								},
								validateBeforeSubmit: function(_selectedValues) {

									if (_selectedValues.length == 0) {
										this.errorMessage = "Please select one or more Trend values";
										return false;
									}

									this.errorMessage = null;
									return true;
								},
								defaultValue: function() {
									var returnVal = [];
									// By Default setting it to Last One Day
									returnVal.push(activityTrend[0]);
									return returnVal;
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
								id: "activityMetricTime",
								type: "multi-select",
								options: {
			                    	singleSelect: true
			                    },
								name: "Time",
								masterList: function() { // This must return a promise


									var deferred = $q.defer();
									deferred.resolve(eventTrend);

									return deferred.promise;
								},
								validateBeforeSubmit: function(_selectedValues) {

									if (_selectedValues.length == 0) {
										this.errorMessage = "Please select one or more Time values";
										return false;
									}

									this.errorMessage = null;
									return true;
								},
								defaultValue: function() {
									var returnVal = [];
									// By Default setting it to Last One Day
									returnVal.push(eventTrend[0]);
									return returnVal;
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
							},
							{
								id: "activityMetric",
								type: "multi-select",
								name: "Activity Metric",
								masterList: function() { // This must return a promise
									var activityMetricTypeLst = [];
									RESTService.getForData('getActivityMetricesTypes', null, null, function(data) {
										angular.forEach(data, function (activityMetric) {
											var metricObj = {
													label: activityMetric.metricName,
													id: activityMetric.metricCode
											};

											activityMetricTypeLst.push(metricObj);

											activityMetricTypeLst.sort(function(a, b) {
												return a.label === b.label ? 0 : (a.label < b.label ? -1 : 1);
											});
										});

									}, function() {
										Notification.error({
											message: "Error loading Activity Metric data",
											delay: enablix.errorMsgShowTime
										});
									});

									var deferred = $q.defer();
									deferred.resolve(activityMetricTypeLst);

									return deferred.promise;
								},
								validateBeforeSubmit: function(_selectedValues) {
									if (_selectedValues.length == 0) {
										this.errorMessage = "Please select one or more Activity Metric values";
										return false;
									}

									this.errorMessage = null;
									return true;
								},
								//FIXME Cannot hardcode the default value. Need to think about this.
								defaultValue: function() {
									var returnVal = [{label:'Content Access',id:'ACTMETRIC6'}];
									return returnVal;
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
							}
							],
							dataTransformer: function(_data, _filterValues) {
								this.heading = this.name + " ( As of " + $filter('ebDateTime')(_data.asOfDate) + ")";
								return _data.trendData;
							},
							fetchData: function(_dataFilters, _onSuccess, _onError) {
								var ACTIVITY_TREND = "getActivityTrend";

								var searchFilters = _dataFilters || {};
								ActivityMetricService.getActivityMetric(ACTIVITY_TREND, searchFilters, _onSuccess, _onError)
							} 
			};
			enablix.reports.push(activityTrendReport);
			/** ------------------------------------------ Report Definitions end here ------------------------------------------**/
			
			
			
			var getReportDefinitions = function() {
				return enablix.reports;
			};
			
			
			var getReportDef = function(_reportId) {
				
				var reportDefs = getReportDefinitions();
				
				for (var i in reportDefs) {
					if (reportDefs[i].id == _reportId) {
						return reportDefs[i];
					}
				}
				
				return null;
			}
		
			return {
				getReportDefinitions: getReportDefinitions,
				getReportDef: getReportDef
			};
	 	}
	]);