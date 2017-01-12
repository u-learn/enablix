enablix.studioApp.factory('PlayDefinitionService', 
	[
	 			'RESTService', 'Notification', 'DataSearchService',
	 	function(RESTService,   Notification,   DataSearchService) {
	 		
	 		var DOMAIN_TYPE = "com.enablix.core.domain.play.PlayDefinition";
	 		
	 		var FILTER_METADATA = {
					"prototypeId" : {
	 					"field" : "playTemplate.prototypeId",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				},
	 				"prototype" : {
	 					"field" : "playTemplate.prototype",
	 					"operator" : "EQ",
	 					"dataType" : "BOOL"
	 				},
	 				"executable" : {
	 					"field" : "playTemplate.executable",
	 					"operator" : "EQ",
	 					"dataType" : "BOOL"
	 				},
	 				"id" : {
	 					"field" : "id",
	 					"operator" : "EQ",
	 					"dataType" : "STRING"
	 				}
				};
	 		
	 		var SUMMARY_FIELDS = 
	 			[ "playTemplate.id", "playTemplate.name", "playTemplate.prototype", "playTemplate.executable", 
	 			  "playTemplate.prototypeId", "createdAt", "createdBy", "modifiedAt", "modifiedBy"];
	 				
	 		var getAllPrototypePlaysSummary = function(_onSuccess, _onError) {
	 			
	 			var filters = {
	 				prototype: true
	 			};
	 			
	 			var pagination = {
	 					pageSize: 20,
	 					pageNum: 0,
	 					sort: {
	 						field: "playTemplate.name",
	 						direction: "ASC"
	 					}
	 				};
	 			
	 			getPlayDefSummaryList(filters, pagination, function(_dataPage) {
	 				_onSuccess(_dataPage.content);
	 			}, _onError)
	 			
	 		};
	 		
	 		var getPlayDefSummaryList = function(_filters, _pagination, _onSuccess, _onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError, SUMMARY_FIELDS);
			};
			
			var getPlayDefinition = function(_playDefId, _onSuccess, _onError) {
				
				var params = {
						playDefId: _playDefId
				};
				
				RESTService.getForData("getPlayDefinition", params, null, _onSuccess, _onError);
			};
	 		
	 		return {
	 			getAllPrototypePlaysSummary: getAllPrototypePlaysSummary,
	 			getPlayDefSummaryList: getPlayDefSummaryList,
	 			getPlayDefinition: getPlayDefinition
	 		};
	 	}
	 ]);
