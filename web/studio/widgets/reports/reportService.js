enablix.studioApp.factory('ReportService', 
	[	        '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'UserPreferenceService',
	 	function($q,   $filter,   RESTService,   Notification,   StateUpdateService,   UserPreferenceService) {
		  
			var cannedReportsProcessed = false;
			
			var getReportDefinitions = function() {
				
				if (!cannedReportsProcessed) {
					
					var cannedRptsConfig = UserPreferenceService.getPrefByKey('reports.canned.list');
					
					if (!isNullOrUndefined(cannedRptsConfig)) {
						
						var cannedReports = cannedRptsConfig.config.reports;
						
						angular.forEach(cannedReports, function(cannedRep) {
							
							for (var i in enablix.reports) {
							
								var baseRptDef = enablix.reports[i];
								
								if (cannedRep.baseReportId == baseRptDef.id) {
								
									var cannedRptDef = angular.copy(baseRptDef);
									cannedRptDef.id = cannedRep.id;
									cannedRptDef.name = cannedRep.name;
									cannedRptDef.heading = cannedRep.heading;
									
									if (cannedRptDef.options && cannedRep.options) {
										cannedRptDef.options = angular.extend(cannedRptDef.options, cannedRep.options);
									}
									
									angular.forEach(cannedRep.filters, function(cannedFilter) {
										
										for (var k in cannedRptDef.filters) {
										
											var rptFilter = cannedRptDef.filters[k];
											
											if (rptFilter.id == cannedFilter.id) {
												rptFilter.type = cannedFilter.type;
												
												rptFilter.masterList = function() { // This must return a promise
													
													var deferred = $q.defer();
													deferred.resolve(cannedFilter.value);
													
													return deferred.promise;
												};
												
												rptFilter.validateBeforeSubmit = function(_selValues) {
													return true;
												};
												
												var baseTxFn = rptFilter.filterValueTransformer;
												rptFilter.filterValueTransformer = function(_selectedValues) {
													return baseTxFn(cannedFilter.value);
												};
												
												rptFilter.defaultValue = function() {
													return cannedFilter.value;
												};
												
												break;
											}
										}
									});
									
									enablix.reports.push(cannedRptDef);
									
									break;
								}
							}
						});
					}
					
					cannedReportsProcessed = true;
				}
				
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