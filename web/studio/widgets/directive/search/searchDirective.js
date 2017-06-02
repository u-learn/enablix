enablix.studioApp.directive('ebxSearchBox', [
		'StateUpdateService',"$q",'DataSearchService','ContentTemplateService',
		function(StateUpdateService,$q,DataSearchService,ContentTemplateService) {
			return {
				restrict : 'E',
				scope : {
				},
				link : function(scope, element, attrs) {
					var itemContainerList = ContentTemplateService.getPortalTopNavItemContainers();
					var asyncData = {template:enablix.pTemplate};
					itemContainerList.forEach(function(containerQId){
						var qualifiedId = containerQId.qualifiedId;
						var value = DataSearchService.promiseContainerDataSearchResult(qualifiedId).then(function(container){
							return _.get(container,"data.content");
						});
						asyncData[qualifiedId] = value;
					});
					var mountPoint = "#enablix-search";
					function onTrueEnter(payload){
						var node = payload.node;
						var searchText = payload.query;
						if(node && node.type === "BizDimEntSubNode"){
							var uri = node.asUri();
							StateUpdateService.goToPortalSubContainerList(uri.containerQId,uri.containerQId+"."+uri.subContainerQId,uri.parentIdentity);
							return;
						}else if(!isNullOrUndefined(searchText)  && searchText.length > 0){
							StateUpdateService.goToPortalSearch(searchText);
						}else{

						}
					}
					enablix.searchComponent = new enablix.SearchComponent({mountPoint:mountPoint,asyncData:asyncData});
					enablix.searchComponent.onTrueEnter = onTrueEnter;
				},
				templateUrl : "widgets/directive/search/searchBox.html"
			};
		} ]);