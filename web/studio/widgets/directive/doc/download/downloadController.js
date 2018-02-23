enablix.studioApp.controller('DownloadCtrl', 
			['$scope', '$state', 'ConfirmationModalWindow', 'InfoModalWindow',
    function ($scope,   $state,   ConfirmationModalWindow,   InfoModalWindow) {
    	
		$scope.downloadFile = function($event) {
			
			var continueDownload = true;
			
			if ($scope.parentRecord && $scope.parentRecord.__indicators 
					&& $scope.parentRecord.__indicators["DOWNLOAD_MSG"]) {
				
				var downloadMsgs = $scope.parentRecord.__indicators["DOWNLOAD_MSG"];
				
				if (downloadMsgs && downloadMsgs.length > 0) {
					
					for (var i = 0; i < downloadMsgs.length; i++) {
						
						var downloadMsg = downloadMsgs[i];
						
						if (downloadMsg.value) {
							
							continueDownload = false;
							
							var infoModal = InfoModalWindow.showInfoWindow("Information", downloadMsg.config.message, 'sm');
							
							infoModal.result.then(function() {
								goDownload($event);
							});
							
							break;
						}
					}
				}
				
			} else if ($scope.parentRecord && $scope.parentRecord.__indicators 
					&& $scope.parentRecord.__indicators["DOWNLOAD_PROMPT"]) {
				
				var downloadPrompts = $scope.parentRecord.__indicators["DOWNLOAD_PROMPT"];
				
				if (downloadPrompts && downloadPrompts.length > 0) {
					
					for (var i = 0; i < downloadPrompts.length; i++) {
					
						var downloadPrompt = downloadPrompts[i];
						
						if (downloadPrompt.value) {
					
							continueDownload = false;	
							
							var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
													downloadPrompt.config.message, 
													"Proceed", "Cancel");
											
							confirmModal.result.then(function(confirmed) {
								if (confirmed) {
									goDownload($event);
								}
							});
							
							break;
						}
					}
				}
				
			} 
			
		    if (continueDownload) {
		    	goDownload($event);
		    }
		    
		    $event.stopPropagation();
		};
		
		var goDownload = function($event) {
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
		};
}]);
