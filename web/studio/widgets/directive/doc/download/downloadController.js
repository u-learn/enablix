enablix.studioApp.controller('DownloadCtrl', 
			['$scope', '$state',
    function ($scope,   $state) {
    	
		$scope.downloadFile = function($event) {
			//this trick will generate a temp <a /> tag
		    var link = document.createElement("a");    
		    link.href = $scope.docBaseUrl + "/" + $scope.docValue
		    	+ ($state.includes('portal') ? '?atChannel=WEB' : "");
		    
		    //set the visibility hidden so it will not effect on your web-layout
		    link.style = "visibility:hidden";
		    
		    //this part will append the anchor tag and remove it after automatic click
		    document.body.appendChild(link);
		    link.click();
		    document.body.removeChild(link);
		    
		    $event.stopPropagation();
		};
}]);
