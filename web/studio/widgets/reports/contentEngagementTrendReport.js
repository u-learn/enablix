enablix.studioApp.factory('ContentEngagementTrendReport', 
	[	    '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'ContentTemplateService',
	function($q,   $filter,   RESTService,   Notification,   StateUpdateService ,  ContentTemplateService) {
		
		var reportDataContent = [
			{ dateValue:new Date('2017-04-01T00:00:00'), accessCnt:54, engagementCnt:54, sharesCnt:215, prospectEngmntCnt:113, successRatio:52 },
			{ dateValue:new Date('2017-05-01T00:00:00'), accessCnt:40, engagementCnt:40, sharesCnt:325, prospectEngmntCnt:152, successRatio:46 },
			{ dateValue:new Date('2017-06-01T00:00:00'), accessCnt:42, engagementCnt:42, sharesCnt:190, prospectEngmntCnt:88, successRatio:46 },
			{ dateValue:new Date('2017-07-01T00:00:00'), accessCnt:23, engagementCnt:23, sharesCnt:85, prospectEngmntCnt:36, successRatio:42 },
			{ dateValue:new Date('2017-08-01T00:00:00'), accessCnt:53, engagementCnt:53, sharesCnt:271, prospectEngmntCnt:111, successRatio:40 },
			{ dateValue:new Date('2017-09-01T00:00:00'), accessCnt:46, engagementCnt:46, sharesCnt:410, prospectEngmntCnt:96, successRatio:23 },
			{ dateValue:new Date('2017-10-01T00:00:00'), accessCnt:34, engagementCnt:34, sharesCnt:310, prospectEngmntCnt:71, successRatio:22 },
			{ dateValue:new Date('2017-11-01T00:00:00'), accessCnt:45, engagementCnt:45, sharesCnt:415, prospectEngmntCnt:94, successRatio:22 },
			{ dateValue:new Date('2017-12-01T00:00:00'), accessCnt:32, engagementCnt:32, sharesCnt:301, prospectEngmntCnt:67, successRatio:22 },
			{ dateValue:new Date('2018-01-01T00:00:00'), accessCnt:22, engagementCnt:22, sharesCnt:245, prospectEngmntCnt:35, successRatio:14 },
			{ dateValue:new Date('2018-02-01T00:00:00'), accessCnt:19, engagementCnt:19, sharesCnt:406, prospectEngmntCnt:39, successRatio:9 },
			{ dateValue:new Date('2018-03-01T00:00:00'), accessCnt:43, engagementCnt:43, sharesCnt:801, prospectEngmntCnt:70, successRatio:8 },
		];
		
		function getTotalCount(dim) {
			var total = 0;
			reportDataContent.forEach(function(rec) {
				total += rec[dim];
			});
			return total;
		}
		
		var summaryInfo = { 
				"Access": getTotalCount('accessCnt'), 
				"Engagement": getTotalCount('engagementCnt'), 
				"Shares": getTotalCount('sharesCnt'), 
				"Prospect Engagement": getTotalCount('prospectEngmntCnt'), 
				"Success Ratio": Math.round(getTotalCount('prospectEngmntCnt')*100/getTotalCount('sharesCnt'))
		};
		
		var cntDimLabel = { accessCnt:'Access', engagementCnt:'Engagement', sharesCnt:'Shares', prospectEngmntCnt:'Prospect Engagement', successRatio:'Success Ratio' };
		
		var contentTitle = "Healthcare Case Study";
		
		var pageSize = 10;
		var pageNum = 0;
		var totalPages = Math.ceil(reportDataContent.length/pageSize);
		
		var getReportData = function() {
			
			var newChartData = {};
        	
        	angular.forEach(reportDataContent, function(dataItem) {
        	
        		var xValue = dataItem.dateValue;
        		
        		angular.forEach(dataItem, function(value, key) {
        		
        			if (key != "dateValue") {
        				var keyLabel = cntDimLabel[key];
        				var dataPoints = newChartData[keyLabel];
        				
        				if (isNullOrUndefined(dataPoints)) {
        					dataPoints = [];
        					newChartData[keyLabel] = dataPoints;
        				}
        				
        				dataPoints.push({y: value, x: xValue});
        			}
        			
        		});
        		
        	});
        	
        	var nvd3ChartData = [];
        	angular.forEach(newChartData, function(value, key) {
        		nvd3ChartData.push({
        			values: value,
        			key: key
        		});
        	});
        	
        	return nvd3ChartData;
		}
		
		
		var metricStartDate = null;
		
		var eventOccurenceLst = [
			{ label: 'Last Day', id: '1' },	
			{ label: 'Last 7 Days', id: '6' },
            { label: 'Last 30 Days', id: '29' },
            { label: 'Last 90 Days', id: '89' }
        ];
		
		var init = function() {
			
			var contentEngagementTrendRpt = {
					id: "content-engagement-trend",
					name: "Content Engagement Trend",
					heading: "Content Engagement Trend",
					type: "NV_LINECHART",
					chartOptions: {
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
			                xScale: d3.time.scale.utc(),
			                xAxis: {
			                    axisLabel: 'Month',
			                    tickFormat: function(d) {
			                    	var monthlyTrend = "Monthly";
					        		var dateStr = monthlyTrend ? $filter('ebMonYear')(d) : $filter('ebDate')(d);
					        		return dateStr;
			                    },
			                    rotateLabels: -45
			                },
			                yAxis: {
			                    axisLabel: 'Count',
			                    tickFormat: function(d) {
			                        return d;
			                    },
			                    axisLabelDistance: -10
			                },
			                legendPosition: 'right'
			            }
					},
					init: function($scope) {
						
					},
					filterMetadata: {
		                
		            },
					dataTransformer: function(_data, _filterValues) {
						return _data;
					},
					fetchData: function(_dataFilters, _onSuccess, _onError) {
						this.heading = contentTitle + " - Content Engagement";
						_onSuccess(getReportData());
					},
					fetchSummaryInfo: function(_dataFilters, _onSuccess, _onError) {
						if (_onSuccess) {
							_onSuccess(summaryInfo);
						}
					}
			};
			enablix.reports.push(contentEngagementTrendRpt);
		}
	
		return {
			init: init
		}
	}
]);

angular.module("studio").run(['ContentEngagementTrendReport', 
	function(ContentEngagementTrendReport) { 
	ContentEngagementTrendReport.init(); 
	}
]);