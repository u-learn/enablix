enablix.studioApp.directive('ebxShareContextMenu', [
			 'RESTService','Notification','shareContentModalWindow','$window','$location','$state','StateUpdateService','LocationUtil',
	function (RESTService,  Notification,  shareContentModalWindow,  $window,  $location,  $state,  StateUpdateService,  LocationUtil ) {
	return {
		restrict: 'E',
		scope : {
			contextData : "=",
			contentQId: "="
		},
		link: function(scope, element, attrs) {
			scope.openMailClient =function(downloadDocId,contentIdentity,contentName){
				var _data = {
						"containerQId" : scope.contentQId,
						"instanceIdentity" : contentIdentity,
						"itemTitle" : contentName
				};
				RESTService.postForData("shareOptsEmailClientAudit", {}, _data, null, function(data){
					var _subject = "Shared Content - "+contentName;
					var portalURL = "Portal URL : "+getPortalURL(contentIdentity);
					var _body=portalURL;
					if ( downloadDocId != null ) { 
						var downloadURL  = "Download URL : "+getDownloadURL(downloadDocId);
						_body=_body+"%0D%0A"+downloadURL;
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
				return requestConfig.url + downloadDocId + '?atChannel=WEB&atContext=COPYNSHARE';
			}
			
			scope.copyDownloadURL = function(downloadDocId,contentIdentity,contentName){
				
				var downloadURL = getDownloadURL(downloadDocId);
				
				var auditRequest = {
					"containerQId" : scope.contentQId,
					"instanceIdentity" : contentIdentity,
					"itemTitle" : contentName
				};
				
				var _data = {
					"url" : downloadURL,
					"auditRequest" : auditRequest
				};
				
				RESTService.postForDataSync("getShareableDocUrl", {}, _data, null, function(data) {
						copyToClipboard(data);
						Notification.primary({message: "Download URL is Copied",  delay: 3000});
					}, function(data){
						Notification.error({message: "Error getting download URL", delay: enablix.errorMsgShowTime});
					}, null);
				
			}
			
			scope.copyLinkUrl = function(_url, _contentIdentity, _contentName) {
				
				var linkParams = {
					cId: _contentIdentity,
					cQId: scope.contentQId,
					atContext: "COPYNSHARE"
				}
				
				var linkURL = LocationUtil.createRelativeExtLinkUrl(_url, linkParams);
				
				var auditRequest = {
					"containerQId" : scope.contentQId,
					"instanceIdentity" : _contentIdentity,
					"itemTitle" : _contentName
				};
				
				var request = {
					"url" : linkURL,
					"auditRequest" : auditRequest
				};
				
				RESTService.postForDataSync("getShareableLinkUrl", {}, request, null, function(data) {
					copyToClipboard(data.shareableUrl);
					Notification.primary({message: "Link URL is copied",  delay: 3000});
				}, function(data){
					Notification.error({message: "Error getting link URL", delay: enablix.errorMsgShowTime});
				}, null);
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
				+ ":" + $location.port() + "/app.html#/portal/container/"+scope.contentQId+"/"+contentIdentity;
			
			}
			scope.shareToSlack = function(contentIdentity,$event){
				checkAuthAndShare(contentIdentity, $event);
			}
			var checkAuthAndShare= function(contentIdentity,$event){
				
				RESTService.getForData('getSlackStoredAuthAccessToken', null, null, function(data) {
					if( data != null && data.accessToken!=null && data.teamName!=null ){
						shareContentModalWindow.showShareToSlackModal(
								scope.contentQId, contentIdentity, $event);
					}
					else{
						StateUpdateService.goToSlackAuth("shareContextMenu");
					}
				}, function() {    		
					Notification.error({message: "Error Sharing to Slack", delay: enablix.errorMsgShowTime});
				},null);
			}
			scope.copyPortalURL =function(contentIdentity,contentName,contentTitle){
				if( contentName==null || contentName == undefined){
					contentName=contentTitle;
				}
				var portalURL = getPortalURL(contentIdentity);
				copyToClipboard(portalURL);
				var _data = {
						"containerQId" : scope.contentQId,
						"instanceIdentity" : contentIdentity,
						"itemTitle" : contentName
				};

				RESTService.postForData("shareOptsPortalAudit", {}, _data, null, function(data){
					Notification.primary({message: "Portal URL is Copied",  delay: 3000});
				}, function(data){}, null);

			}
			scope.openShareContentModal =function(contentIdentity,$event){
				shareContentModalWindow.showShareContentModal(scope.contentQId, contentIdentity,$event);
			}

		},
		templateUrl: "widgets/directive/portal/sharePopup/shareContextMenu/shareContextMenu.html"
	};
}]);
