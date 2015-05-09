enablix.studioApp.directive('ebDocDownload', [
		function() {

			return {
				restrict : 'E',
				scope : {
					docValue : '='
				},
				link: function(scope, element, attrs) {
					var params = { };
					var requestConfig = genereateRequestConfig("downloadDocument", params);
					scope.docBaseUrl = requestConfig.url; 
					
					scope.downloadFile = function($event) {
						//this trick will generate a temp <a /> tag
					    var link = document.createElement("a");    
					    link.href = scope.docBaseUrl + "/" + scope.docValue;
					    
					    //set the visibility hidden so it will not effect on your web-layout
					    link.style = "visibility:hidden";
					    
					    //this part will append the anchor tag and remove it after automatic click
					    document.body.appendChild(link);
					    link.click();
					    document.body.removeChild(link);
					    
					    $event.stopPropagation();
					}
				},
				templateUrl : "widgets/directive/doc/download/downloadFile.html"
			};
	} ]);