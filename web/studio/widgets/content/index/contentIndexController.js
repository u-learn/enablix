enablix.studioApp.controller('contentIndexCtrl', ['$scope', 'ContentIndexService', function($scope, ContentIndexService) {
	
	ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
    	 $scope.indexData = data; 
    }, function(data) {
    	alert("Error fetching content index");
    });
    
}]);