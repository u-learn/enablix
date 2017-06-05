enablix.studioApp.factory('ActivityMetricReport', 
	[	    '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'ActivityMetricService',
	function($q,   $filter,   RESTService,   Notification,   StateUpdateService , ActivityMetricService) {
		
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
		
		var eventOccurenceLst = [
			{
				label: 'Last Day',
	            id: '1'
	        },	
			{
                label: 'Last 7 Days',
                id: '6'
            },
            {
                label: 'Last 30 Days',
                id: '29'
            },
            {
                label: 'Last 90 Days',
                id: '89'
            }
        ];
		
		var init = function() {
			
			var activityMetricReport = {
					id: "activity-metric-calculator",
					name: "Activity Summary",
					heading: "Activity Summary",
					type: "TABULAR",
					init: function($scope) {
						
					},
					downloadReport : function(){
						html2canvas($("#tabularReport"), {
				            onrendered: function(canvas) {
				                document.getElementById("reports").appendChild(canvas);
				                var ctx = canvas.getContext('2d');
				                var img = new Image();
				                img.src = canvas.toDataURL();
				                img.onload = function() {
				                	  ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
									  canvasdata = canvas.toDataURL("image/png");
									  var a = document.createElement("a");
									  a.id = "imagepng"
									  a.download = "activity-metric-report.png";
									  a.href = canvasdata;
									  document.getElementById("reports").appendChild(a);
									  a.click();
									  document.getElementById("reports").removeChild(a);
									  document.getElementById("reports").removeChild(canvas);
								}
				            }
				        });
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
		}
	
		return {
			init: init
		}
	}
]);

angular.module("studio").run(['ActivityMetricReport', 
	function(ActivityMetricReport) { 
		ActivityMetricReport.init(); 
	}
]);