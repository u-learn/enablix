enablix.studioApp.directive('ebxContextMenu',['RESTService','Notification','shareContentModalWindow','$window','$location',
	function(RESTService,Notification,shareContentModalWindow,$window,$location) {
		return {
			restrict: 'E',
			scope : true,
			link: function(scope, element, attrs) {

				scope.openMailClient =function(contentIdentity){

					var params = {containerQId: scope.subContainerQId,
							contentIdentity:contentIdentity};

					RESTService.getForData('shareOptsEmailClient', params, null, function(data) {
						openLocalEmailClient(data);	    	
					}, function() {    		
						Notification.error({message: "Error loading user data", delay: enablix.errorMsgShowTime});
					});

				}

				var openLocalEmailClient = function(data){
					var replaceHTMLBody = data.htmlBodyContent.replace(/\n/g,'');
					var encodeHTML = encodeURIComponent(replaceHTMLBody);
					$window.open("mailto:"+ "" + "?subject="+data.subject+"&body="+encodeHTML,"_self");
				};

				scope.copyPortalURL =function(contentIdentity,contentName){
					var portalURL=$location.protocol() + "://" + $location.host() 
					+ ":" + $location.port() + "/app.html#/portal/container/"+scope.subContainerQId+"/"+contentIdentity;
					var aux = document.createElement("input");
					aux.setAttribute("value", portalURL);
					document.body.appendChild(aux);
					aux.select();
					document.execCommand("copy");
					document.body.removeChild(aux);
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