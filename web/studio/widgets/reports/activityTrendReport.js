enablix.studioApp.factory('ActivityTrendReport', 
		[	    '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'ActivityMetricService',
			function($q,   $filter,   RESTService,   Notification,   StateUpdateService , ActivityMetricService) {

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
				}
				];
			
			function applyStylesheets(svgEl) {
				// use an empty svg to compute the browser applied stylesheets
				var emptySvg = window.document.createElementNS("http://www.w3.org/2000/svg", 'svg');
				window.document.body.appendChild(emptySvg);
				var emptySvgDeclarationComputed = getComputedStyle(emptySvg);
				emptySvg.parentNode.removeChild(emptySvg);

				// traverse the element tree and explicitly set all stylesheet values
				// on an element. this is ripped from svg-crowbar
				var allElements = traverse(svgEl);
				var i = allElements.length;
				while (i--){
					explicitlySetStyle(allElements[i], emptySvgDeclarationComputed);
				}
			}


			function explicitlySetStyle (element, emptySvgDeclarationComputed) {
				var cSSStyleDeclarationComputed = getComputedStyle(element);
				var i, len, key, value;
				var computedStyleStr = "";
				for (i=0, len=cSSStyleDeclarationComputed.length; i<len; i++) {
					key=cSSStyleDeclarationComputed[i];
					value=cSSStyleDeclarationComputed.getPropertyValue(key);
					if (value!==emptySvgDeclarationComputed.getPropertyValue(key)) {
						computedStyleStr+=key+":"+value+";";
					}
				}
				element.setAttribute('style', computedStyleStr);
			}


			// traverse an svg and append all of the elements to the tree array. This
			// ignores some elements that can appear in <svg> elements but whose
			// children's styles should not be tweaked
			function traverse(obj){
				var tree = [];
				var ignoreElements = {
						'script': undefined,
						'defs': undefined,
				};
				tree.push(obj);
				visit(obj);
				function visit(node) {
					if (node && node.hasChildNodes() && !(node.nodeName.toLowerCase() in ignoreElements)) {
						var child = node.firstChild;
						while (child) {
							if (child.nodeType === 1) {
								tree.push(child);
								visit(child);
							}
							child = child.nextSibling;
						}
					}
				}
				return tree;
			}
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
			var init = function() {

				var activityTrendReport = {
						id: "activity-trend-calculator",
						name: "Activity Trend",
						heading: "Activity Trend",
						type: "MULTILINE",
						init: function($scope) {

						},
						downloadReport : function(){
							applyStylesheets(document.getElementById("reports").querySelector('svg'));
							var w = 100, h = 100;
							var blankCanvas = document.createElement('canvas');
							blankCanvas.id = "blankCanvas";
							blankCanvas.width = w * 50;
							blankCanvas.height = h * 50;

							document.getElementById("reports").appendChild(blankCanvas);

							var html = new XMLSerializer().serializeToString(document.getElementById("reports").querySelector('svg'));
							var imgsrc = 'data:image/svg+xml;base64,' + btoa(html);
							var canvas = document.getElementById("blankCanvas");
							var context = canvas.getContext("2d");
							context.fillStyle = 'white';
							context.fillRect(0, 0, canvas.width, canvas.height);			
							var canvasdata;
							var image = new Image;
							image.src = imgsrc;

							image.onload = function() {
								context.drawImage(image, 0, 0, canvas.width, canvas.height);
								canvasdata = canvas.toDataURL("image/png");
								var a = document.createElement("a");
								a.id = "imagepng"
									a.download = "activity-trend-report.png";
								a.href = canvasdata;
								document.getElementById("reports").appendChild(a);
								a.click();
								document.getElementById("reports").removeChild(a);
								document.getElementById("reports").removeChild(blankCanvas);
							}
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
			}

			return {
				init: init
			}
		}
		]);

angular.module("studio").run(['ActivityTrendReport', 
	function(ActivityTrendReport) { 
	ActivityTrendReport.init(); 
}
]);