enablix.studioApp.factory('ReportService', 
	[	        '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 
	 	function($q,   $filter,   RESTService,   Notification,   StateUpdateService) {
		  
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