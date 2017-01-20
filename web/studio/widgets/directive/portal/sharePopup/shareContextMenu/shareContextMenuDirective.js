enablix.studioApp.directive('ebxContextMenu',['RESTService','Notification',
	'shareContentModalWindow','$window','$location','$state',
	function(RESTService,Notification,shareContentModalWindow,$window,$location,$state) {
	return {
		restrict: 'E',
		scope : true,
		link: function(scope, element, attrs) {

			scope.openMailClient =function(downloadDocId,contentIdentity,contentName){
				var _data = {
						"containerQId" : scope.subContainerQId,
						"instanceIdentity" : contentIdentity,
						"itemTitle" : contentName
				};
				RESTService.postForData("shareOptsEmailClientAudit", {}, _data, null, function(data){
					var _subject = "Shared Content - "+contentName;
					var portalURL = "Portal URL : "+getPortalURL(contentIdentity);
					var _body=portalURL;
					if ( downloadDocId != null ) { 
						var downloadURL  = "Download URL : "+getDownloadURL(downloadDocId);
						_body=_body+"<br/>"+downloadURL;
					}
					openLocalEmailClient(_subject,_body);	   
				}, function(data){}, null);
			}

			var openLocalEmailClient = function(_subject,_body){
				$window.open("mailto:"+ "" + "?subject="+_subject+"&body="+_body,"_self");
			};
			function getDownloadURL(downloadDocId){
				var params = { };
				var requestConfig = genereateRequestConfig("downloadDocument", params);
				var link = document.createElement("a");    
				link.href = requestConfig.url  + "/" + downloadDocId
				+ ($state.includes('portal') ? '?atChannel=WEB' : "");
				return link.href;
			}
			scope.copyDownloadURL = function(downloadDocId,contentIdentity,contentName){
				var downloadURL = getDownloadURL(downloadDocId)
				copyToClipboard(downloadURL);
				var _data = {
						"containerQId" : scope.subContainerQId,
						"instanceIdentity" : contentIdentity,
						"itemTitle" : contentName
				};
				RESTService.postForData("shareOptsDocDownloadAudit", {}, _data, null, function(data){
					Notification.primary({message: "Download URL is Copied",  delay: 3000});
				}, function(data){}, null);
				
			}
			function copyToClipboard(textToCopy){
				var aux = document.createElement("input");
				aux.setAttribute("value", textToCopy);
				document.body.appendChild(aux);
				aux.select();
				document.execCommand("copy");
				document.body.removeChild(aux);
			}
			function getPortalURL(contentIdentity){
				return $location.protocol() + "://" + $location.host() 
				+ ":" + $location.port() + "/app.html#/portal/container/"+scope.subContainerQId+"/"+contentIdentity;
			
			}
			
			scope.copyPortalURL =function(contentIdentity,contentName){

				var portalURL = getPortalURL(contentIdentity);
				copyToClipboard(portalURL);
				var _data = {
						"containerQId" : scope.subContainerQId,
						"instanceIdentity" : contentIdentity,
						"itemTitle" : contentName
				};

				RESTService.postForData("shareOptsPortalAudit", {}, _data, null, function(data){
					Notification.primary({message: "Portal URL is Copied",  delay: 3000});
				}, function(data){}, null);

			}
			scope.openShareContentModal =function(contentIdentity,$event){
				shareContentModalWindow.showShareContentModal(scope.subContainerQId, contentIdentity,$event);
			}

		},
		templateUrl: "widgets/directive/portal/sharePopup/shareContextMenu/shareContextMenu.html"
	};
}]);
