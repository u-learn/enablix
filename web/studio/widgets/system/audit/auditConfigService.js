enablix.studioApp.factory('AuditConfigService', 
	[			'RESTService', 'Notification',
	 	function(RESTService,   Notification) {
		
		var getAuditData= function(tenantId,searchJSONTemplate,_success,_error)
		{
			
			RESTService.postForData('getAuditConfiguration', null, searchJSONTemplate, function(data) {  	
					_success(data);	    	
	    	}, function(errorObj) {    		
	    		_error(errorObj)
	    	});
		};
			
			return {
				getAuditData: getAuditData
			};
		
		}
	]);