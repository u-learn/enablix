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
		
			/** ========================================= Content Coverage Report ======================================= **/
			var contentCoverageReport = {
					id: "content-coverage-report",
					name: "Content Coverage",
					heading: "Content Coverage",
					type: "HEATMAP",
					options: {
								margin: { top: 150, right: 0, bottom: 70, left: 170 },
								colors: [/*"#f7fbff", */"#deebf7", "#c6dbef", /*"#9ecae1", */"#6baed6", "#4292c6", /*"#2171b5", "#08519c", "#08306b"*/],
								customColors : { "0": "#f58080"},
								buckets: 4,
								valueText: true,
								categorize: true
							 },
					init: function($scope) {
						
						$scope.$watch('dispatch', function(newValue, oldValue) {
							if (newValue !== oldValue && !isNullOrUndefined(newValue)) {
								$scope.dispatch.on("click", function(e) {
									if (e.value != 0) { // if there are any records, then nav to list page
										StateUpdateService.goToPortalSubContainerList(
												e.data.contentQId, e.data.subContainerQId, e.data.recordIdentity);
									}
								});
							}
						})
						
					},
					filterMetadata: {
						"latest" : {
		 					"field" : "latest",
		 					"operator" : "EQ",
		 					"dataType" : "BOOL"
		 				},
		 				"contentQIdIn" : {
		 					"field" : "contentQId",
		 					"operator" : "IN",
		 					"dataType" : "STRING"
		 				}
					},
					filters: 
						[{
							id: "contentQIdIn",
							type: "multi-select",
							name: "Business Dimensions",
							masterList: function() { // This must return a promise
								var businessDimensionContainers = ContentTemplateService.getContainersByBusinessCategory("BUSINESS_DIMENSION");
								var contentTypeList = [];
								
								angular.forEach(businessDimensionContainers, function(cont) {
									contentTypeList.push({
										id: cont.qualifiedId,
										label: cont.label
									});
								});
								
								var deferred = $q.defer();
								deferred.resolve(contentTypeList);
								
								return deferred.promise;
							},
							validateBeforeSubmit: function(_selectedValues) {
								
								if (_selectedValues.length == 0) {
									this.errorMessage = "Please select one or more Business Dimensions";
									return false;
								}
								
								this.errorMessage = null;
								return true;
							},
							filterValueTransformer: function(_selectedValues) {
								
								var returnVal = [];
								
								angular.forEach(_selectedValues, function(val) {
									returnVal.push(val.id);
								});
								
								return returnVal;
							},
							defaultValue: function() {
								
								var businessDimensionContainers = ContentTemplateService.getContainersByBusinessCategory("BUSINESS_DIMENSION");
								var contentTypeList = [];
								
								angular.forEach(businessDimensionContainers, function(cont) {
									contentTypeList.push({
										id: cont.qualifiedId,
										label: cont.label
									});
								});
								
								return contentTypeList;
							}
						},
						{
							id: "contentTypes",
							type: "multi-select",
							name: "Content Type",
							masterList: function() { // This must return a promise
								var contentTypeContainers = ContentTemplateService.getContainersByBusinessCategory("BUSINESS_CONTENT");
								var contentTypeList = [];
								
								angular.forEach(contentTypeContainers, function(cont) {
									contentTypeList.push({
										id: cont.qualifiedId,
										label: cont.label
									});
								});
								
								contentTypeList.sort(sortByLabelProp);
								
								var deferred = $q.defer();
								deferred.resolve(contentTypeList);
								
								return deferred.promise;
							},
							filterValueTransformer: function(_selectedValues) {
								// this will be a client side filtering, so do not send any value to server
								return null;
							},
							defaultValue: function() {
								// nothing selected by default
								return [];
							}
						}],
					dataTransformer: function(_data, _filterValues) {
						
						var reportData = [];
						var reportDef = this;
						
						this.options.restrictedXLabels = [];
						
						if (_filterValues.contentTypes && _filterValues.contentTypes.length > 0) {
							var restrictedXLabels = [];
							angular.forEach(_filterValues.contentTypes, function(contTypeVal) {
								restrictedXLabels.push(contTypeVal.label);
							});
							this.options.restrictedXLabels = restrictedXLabels;
						}
						
						
						this.options.yLabelCategories = [];
						if (_filterValues.contentQIdIn && _filterValues.contentQIdIn.length > 0) {
							var yLabelCategories = [];
							angular.forEach(_filterValues.contentQIdIn, function(contentCat) {
								yLabelCategories.push(contentCat.label);
							});
							this.options.yLabelCategories = yLabelCategories;
						}
						
						// change heading
						if (_data.length > 0) {
							this.heading = this.name + " ( As of " + $filter('ebDateTime')(_data[0].asOfDate) + ")";
						}
						
						angular.forEach(_data, function(dataRecord) {
							
							var recCategory = ContentTemplateService.getContainerLabel(dataRecord.contentQId);
							
							angular.forEach(dataRecord.stats, function(stat) {
								
								var contDef = ContentTemplateService.getConcreteContainerDefinition(enablix.template, stat.itemId);
								
								if (!isNullOrUndefined(contDef) && // below check is to remove data items which do not belong to selected content type
										(!reportDef.options.restrictedXLabels || reportDef.options.restrictedXLabels.length == 0
												|| reportDef.options.restrictedXLabels.indexOf(contDef.label) > -1)) {
									// x, y, value, category are required for the heatmap chart to display
									reportData.push({
										y: dataRecord.recordTitle,
										x: contDef.label,
										value: stat.count,
										category: recCategory,
										data: {
											contentQId: dataRecord.contentQId,
											recordIdentity: dataRecord.recordIdentity,
											subContainerQId: stat.itemId
										}
									});
								}
								
							})
							
						});
						
						return reportData;
					},
					fetchData: function(_dataFilters, _onSuccess, _onError) {
						
						var CONTENT_COVERAGE_DOMAIN = "com.enablix.core.domain.content.summary.ContentCoverage";
						
						var searchFilters = _dataFilters || {};
						searchFilters.latest = true;
						
						DataSearchService.getSearchResult(CONTENT_COVERAGE_DOMAIN, searchFilters, null, 
									this.filterMetadata, _onSuccess, _onError)
					} 
			};
			
			
			enablix.reports.push(contentCoverageReport);
			
			
			/** =========================================== Content coverage report end ================================= **/
			var eventOccurenceLst = [
			{
				label: 'Last 1 Day',
	            id: '1'
	        },	
			{
                label: 'Last Week',
                id: '7'
            },
            {
                label: 'Last Month',
                id: '30'
            },
            {
                label: 'Last 60 Days',
                id: '60'
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
					    { head: 'Metric', cl: 'metricName', html: d3.f('metricName') },
					    { head: 'Value', cl: 'metricValue', html: d3.f('metricValue') }
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
				                    name: "Time",
				                    masterList: function() { // This must return a promise
				                        

				                        var deferred = $q.defer();
				                        deferred.resolve(eventOccurenceLst);

				                        return deferred.promise;
				                    },
				                    validateBeforeSubmit: function(_selectedValues) {
										
										if (_selectedValues.length == 0) {
											this.errorMessage = "Please select one or more Values";
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
						return _data;
					},
					fetchData: function(_dataFilters, _onSuccess, _onError) {
						var ACTIVITY_METRIC = "getActivityMetric";
						
						var searchFilters = _dataFilters || {};
						ActivityMetricService.getActivityMetric(ACTIVITY_METRIC, searchFilters, _onSuccess, _onError)
									
					} 
			};
			enablix.reports.push(activityMetricReport);
		
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