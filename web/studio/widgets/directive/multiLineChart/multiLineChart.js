enablix.studioApp.directive('ebxMultiLineChart', [
			'$compile', '$filter',
	function($compile,   $filter) {

		return {
			restrict: "E",
			replace: true,
			scope: {
				data: "=",
				columndetails: "=?",
				filterValues: '=',
			},
			transclude: false,
			link: function(scope, element, attrs) {

				var color = d3.scale.category10();
				var xAxisTickFormat = {};
				
		        scope.options = {
		            chart: {
		                type: 'lineChart',
		                height: 500,
		                margin : {
		                    top: 20,
		                    right: 20,
		                    bottom: 80,
		                    left: 55
		                },
		                x: function(d){ return d.x; },
		                y: function(d){ return d.y; },
		                useInteractiveGuideline: true,
		                dispatch: {
		                    stateChange: function(e) { console.log("stateChange"); },
		                    changeState: function(e) { console.log("changeState"); },
		                    tooltipShow: function(e) { console.log("tooltipShow"); },
		                    tooltipHide: function(e) { console.log("tooltipHide"); }
		                },
		                xScale: d3.time.scale(),
		                xAxis: {
		                    axisLabel: 'Time (ms)',
		                    tickFormat: function(d) {
		                    	var monthlyTrend = scope.filterValues.activityMetricTrend[0].label === "Monthly";
				        		var dateStr = monthlyTrend ? $filter('ebMonYear')(d) : $filter('ebDate')(d);
				        		return dateStr;
		                    },
		                    rotateLabels: -45
		                },
		                yAxis: {
		                    axisLabel: 'Activity Count',
		                    tickFormat: function(d) {
		                        return d;
		                    },
		                    axisLabelDistance: -10
		                },
		                legendPosition: 'right',
		                callback: function(chart){
		                    console.log("!!! lineChart callback !!!");
		                }
		            }
		        };

		        scope.chartData = [];
		        
		        function metricIdLabelMapping(){
					var idLabelMap = {};
					angular.forEach(scope.filterValues.activityMetric, function(metric) {
						idLabelMap[metric.id] = metric.label;
					});
					return idLabelMap;
				}

		        scope.$watch("data", function(newValue, oldValue) {
		        	if (newValue && newValue !== oldValue) {
		        		var idLabelMap = metricIdLabelMapping();
		        		setChartData(idLabelMap);
					}
				}, true);

		        function setChartData(_idLabelMap) {
		        	
		        	var newChartData = {};
		        	
		        	angular.forEach(scope.data, function(dataItem) {
		        	
		        		var timeVal = new Date(dataItem.startDate);
		        		
		        		var dateVal = new Date(dataItem.startDate);
		        		var dateStr = $filter('ebDate')(dateVal);
		        		xAxisTickFormat[timeVal] = dateStr;
		        		
		        		angular.forEach(dataItem, function(value, key) {
		        		
		        			if (key !== "Time" && key != "startDate") {
		        			
		        				var dataPoints = newChartData[key];
		        				
		        				if (isNullOrUndefined(dataPoints)) {
		        					dataPoints = [];
		        					newChartData[key] = dataPoints;
		        				}
		        				
		        				dataPoints.push({y: value, x: timeVal});
		        			}
		        			
		        		});
		        		
		        	});
		        	
		        	var nvd3ChartData = [];
		        	var indx = 0;
		        	angular.forEach(newChartData, function(value, key) {
		        		nvd3ChartData.push({
		        			values: value,
		        			key: _idLabelMap[key]
		        		});
		        	});
		        	
		        	scope.chartData = nvd3ChartData;
		        	
		        	var xAxisLabel = "";
		        	
		        	switch(scope.filterValues.activityMetricTrend[0].label) {
		        		case "Daily" : xAxisLabel = "Date"; break;
		        		case "Weekly" : xAxisLabel = "Week Start Date"; break;
		        		case "Monthly" : xAxisLabel = "Month"; break;
		        	}
		        	
		        	scope.options.chart.xAxis.axisLabel = xAxisLabel;
		        	
		        }
		        
			},
			templateUrl: "widgets/directive/multiLineChart/multiLineChart.html"
		};
	}]);