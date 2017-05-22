enablix.studioApp.factory('ContentCoverageReportDef', 
	[	    '$q', '$filter', 'RESTService', 'Notification', 'StateUpdateService', 'DataSearchService', 'ContentTemplateService',
	function($q,   $filter,   RESTService,   Notification,   StateUpdateService,   DataSearchService,   ContentTemplateService) {

		var init = function() {
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
					downloadReport : function(){
						var w = 100, h = 100;
						var blankCanvas = document.createElement('canvas');
						blankCanvas.id = "canvas";
						blankCanvas.width = w * 50;
						blankCanvas.height = h * 50;
						
						document.getElementById("reports").appendChild(blankCanvas);

						var html = new XMLSerializer().serializeToString(document.getElementById("reports").querySelector('svg'));
						var imgsrc = 'data:image/svg+xml;base64,' + btoa(html);
						var canvas = document.getElementById("canvas");
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
						  a.download = "content-coverage-report.png";
						  a.href = canvasdata;
						  document.getElementById("reports").appendChild(a);
						  a.click();
						  document.getElementById("reports").removeChild(a);
						  document.getElementById("reports").removeChild(blankCanvas);
						}
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
			/** =========================================== Content coverage report end ================================= **/
	
			enablix.reports.push(contentCoverageReport);
		}
	
		return {
			init: init
		}
	}
]);

angular.module("studio").run(['ContentCoverageReportDef', 
	function(ContentCoverageReportDef) { 
		ContentCoverageReportDef.init(); 
	}
]);