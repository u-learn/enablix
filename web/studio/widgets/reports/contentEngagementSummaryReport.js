enablix.studioApp.factory('ContentEngagementReport', 
	[	    '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'ContentTemplateService',
	function($q,   $filter,   RESTService,   Notification,   StateUpdateService ,  ContentTemplateService) {
		
		var reportDataContent = [
			{ contentTitle:'Identity Management at Bank of the West', contentLabel:'Case Study', accessCnt:34, engagementCnt:34, sharesCnt:83, prospectEngmntCnt:54, successRatio:65 },
			{ contentTitle:'The Business Value of Extended Validation', contentLabel:'White Paper', accessCnt:54, engagementCnt:54, sharesCnt:215, prospectEngmntCnt:113, successRatio:52 },
			{ contentTitle:'Healthcare Case Study', contentLabel:'Case Study', accessCnt:40, engagementCnt:40, sharesCnt:325, prospectEngmntCnt:152, successRatio:46 },
			{ contentTitle:'Mobile Security Standard Sales Presentation', contentLabel:'Presentation', accessCnt:42, engagementCnt:42, sharesCnt:190, prospectEngmntCnt:88, successRatio:46 },
			{ contentTitle:'Mobile Security for First Republic Bank', contentLabel:'Case Study', accessCnt:23, engagementCnt:23, sharesCnt:85, prospectEngmntCnt:36, successRatio:42 },
			{ contentTitle:'Blueprint for Centralized SSL Certificate Mgmt', contentLabel:'White Paper', accessCnt:53, engagementCnt:53, sharesCnt:271, prospectEngmntCnt:111, successRatio:40 },
			{ contentTitle:'The Business Value of Document Signing', contentLabel:'White Paper', accessCnt:46, engagementCnt:46, sharesCnt:410, prospectEngmntCnt:96, successRatio:23 },
			{ contentTitle:'Why Mobile is the Next Digital Identity', contentLabel:'White Paper', accessCnt:34, engagementCnt:34, sharesCnt:310, prospectEngmntCnt:71, successRatio:22 },
			{ contentTitle:'Authentication Cloud Service Data Sheet', contentLabel:'Data Sheet', accessCnt:45, engagementCnt:45, sharesCnt:415, prospectEngmntCnt:94, successRatio:22 },
			{ contentTitle:'Certificate Management Standard Sales Presentation', contentLabel:'Presentation', accessCnt:32, engagementCnt:32, sharesCnt:301, prospectEngmntCnt:67, successRatio:22 },
			{ contentTitle:'Certificate Services Case Study for JPMC', contentLabel:'Case Study', accessCnt:22, engagementCnt:22, sharesCnt:245, prospectEngmntCnt:35, successRatio:14 },
			{ contentTitle:'Advanced Analytics Techniques for Insider Threat', contentLabel:'Videos', accessCnt:19, engagementCnt:19, sharesCnt:406, prospectEngmntCnt:39, successRatio:9 },
			{ contentTitle:'Certificate Transparency Deployment in 2017', contentLabel:'Blog', accessCnt:43, engagementCnt:43, sharesCnt:801, prospectEngmntCnt:70, successRatio:8 },
			{ contentTitle:'IdentityGuard Data Sheet', contentLabel:'Data Sheet', accessCnt:10, engagementCnt:10, sharesCnt:325, prospectEngmntCnt:21, successRatio:6 },
			{ contentTitle:'Minimum Requirements for Code Signing Certificates', contentLabel:'Blog', accessCnt:22, engagementCnt:22, sharesCnt:283, prospectEngmntCnt:19, successRatio:6 },
			{ contentTitle:'Sharpening First-Time Access Alert for Insider Threat Detection', contentLabel:'Blog', accessCnt:20, engagementCnt:20, sharesCnt:124, prospectEngmntCnt:8, successRatio:6 },
		];
		
		reportDataContent.sort(function(c1, c2) {
			return c1.prospectEngmntCnt == c2.prospectEngmntCnt ? 0 : (c1.prospectEngmntCnt < c2.prospectEngmntCnt ? 1 : -1);
		});
		
		var pageSize = 10;
		var pageNum = 0;
		var totalPages = Math.ceil(reportDataContent.length/pageSize);
		
		var getReportData = function() {
			
			var sliceStart = pageNum*pageSize;
			var sliceEnd = sliceStart + pageSize;
			sliceEnd = sliceEnd > reportDataContent.length ? reportDataContent.length : sliceEnd;
			
			var slice = reportDataContent.slice(sliceStart, sliceEnd);
			
			return {
				content: slice,
				first: true,
				last: true,
				number: pageNum,
				numberOfElements: slice.length,
				size: pageSize,
				totalElements: reportDataContent.length,
				totalPages: totalPages
			};
		}
		
		
		var metricStartDate = null;
		
		var getEventDate = function(_offsetDays) {
            
			var m_names = new Array("Jan", "Feb", "Mar",
                "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec");

            var d = new Date();
            var offsetTime = (24 * 60 * 60 * 1000) * _offsetDays;
            d.setTime(d.getTime() - offsetTime);
            
            var curr_month = d.getMonth();
            var curr_date = d.getDate();
            var curr_year = d.getFullYear().toString().substr(2, 2);

            metricStartDate = d;
            
            return curr_date + "-" + m_names[curr_month] + "-" + curr_year;
        }
		
		var eventOccurenceLst = [
			{ label: 'Last Day', id: '1' },	
			{ label: 'Last 7 Days', id: '6' },
            { label: 'Last 30 Days', id: '29' },
            { label: 'Last 90 Days', id: '89' }
        ];
		
		var userRoleItemDef = {
				bounded : {
					refList : {
						datastore : {
							storeId: "businessRole",
							dataId: "identity",
							dataLabel: "roleName"
						}
					}
				}
			};
		
		var userItemDef = {
				bounded : {
					refList : {
						datastore : {
							location: "TENANT_DB",
							storeId: "ebx_user_profile",
							dataId: "identity",
							dataLabel: "name"
						}
					}
				}
			};
		
		var init = function() {
			
			var contentEngagementRpt = {
					id: "content-engagement-summary",
					name: "Content Engagement Summary",
					heading: "Content Engagement Summary",
					type: "TABULAR",
					init: function($scope) {
						
					},
					columndetails : [
					    { head: 'Name', column: 'contentTitle' },
					    { head: 'Content Type', column: 'contentLabel' },
					    { head: 'Access', column: 'accessCnt' },
					    { head: 'Engagement', column: 'engagementCnt' },
					    { head: 'Shares', column: 'sharesCnt' },
					    { head: 'Prospect Engagement', column: 'prospectEngmntCnt' },
					    { head: 'Success Ratio', column: 'successRatio' }
					],
					filterMetadata: {
		                
		            },
					filters: 
						[
							{
			                    id: "userRoles",
			                    type: "multi-select",
			                    options: {
			                    	singleSelect: false
			                    },
			                    name: "Role",
			                    masterList: function() { // This must return a promise
	
			                        var deferred = $q.defer();
			                        
			                        ContentTemplateService.getBoundedValueList(enablix.templateId, userRoleItemDef, null, function(data) {
			                        	data.sort(sortByLabelProp);
			                        	deferred.resolve(data);
			        				}, function(errorData) {
			        					Notification.error({message: "Error retrieving roles", delay: enablix.errorMsgShowTime});
			        				});
	
			                        return deferred.promise;
			                    },
			                    validateBeforeSubmit: function(_selectedValues) {
									this.errorMessage = null;
									return true;
								},
			                    defaultValue: function() {
			                    	 return [];
								},
			                    filterValueTransformer: function(_selectedValues) {
			                        return null;
			                    }
							},
							{
			                    id: "users",
			                    type: "multi-select",
			                    options: {
			                    	singleSelect: false
			                    },
			                    name: "User",
			                    masterList: function() { // This must return a promise
	
			                        var deferred = $q.defer();
			                        
			                        ContentTemplateService.getBoundedValueList(enablix.templateId, userItemDef, null, function(data) {
			                        	data.sort(sortByLabelProp);
			                        	deferred.resolve(data);
			        				}, function(errorData) {
			        					Notification.error({message: "Error retrieving roles", delay: enablix.errorMsgShowTime});
			        				});
	
			                        return deferred.promise;
			                    },
			                    validateBeforeSubmit: function(_selectedValues) {
									this.errorMessage = null;
									return true;
								},
			                    defaultValue: function() {
			                    	 return [];
								},
			                    filterValueTransformer: function(_selectedValues) {
			                        return null;
			                    }
							},
							{
			                    id: "contentTypes",
			                    type: "multi-select",
			                    options: {
			                    	singleSelect: false
			                    },
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
			                    validateBeforeSubmit: function(_selectedValues) {
									this.errorMessage = null;
									return true;
								},
			                    defaultValue: function() {
			                    	 return [];
								},
			                    filterValueTransformer: function(_selectedValues) {
			                        return null;
			                    }
							},
							{
			                    id: "engagementTime",
			                    type: "multi-select",
			                    options: {
			                    	singleSelect: true
			                    },
			                    name: "Time Period",
			                    masterList: function() { // This must return a promise
	
			                        var deferred = $q.defer();
			                        deferred.resolve(eventOccurenceLst);
	
			                        return deferred.promise;
			                    },
			                    validateBeforeSubmit: function(_selectedValues) {
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
			                        return null;
			                    }
							}
						],
					dataTransformer: function(_data, _filterValues) {
						return _data;
					},
					fetchData: function(_dataFilters, _onSuccess, _onError, _pageNum) {
						pageNum = _pageNum || 0;
						_onSuccess(getReportData());
					} 
			};
			enablix.reports.push(contentEngagementRpt);
		}
	
		return {
			init: init
		}
	}
]);

angular.module("studio").run(['ContentEngagementReport', 
	function(ContentEngagementReport) { 
		ContentEngagementReport.init(); 
	}
]);