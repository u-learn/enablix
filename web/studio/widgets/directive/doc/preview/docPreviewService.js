enablix.studioApp.factory('DocPreviewService', 
	[
	 			'RESTService', '$q',
	 	function(RESTService,   $q) {
	 			
	 		var handlers = [];
	 		
	 		var TYPE_HTML = "html";
	 		var TYPE_ANGULAR_HTML = "angular-html";
	 		
	 		function PdfPreviewHandler() {
	 			
	 			this.previewHtml = function(_docMd) {
	 				if (this.canHandleDoc(_docMd)) {
	 					return '<iframe src="/js/vendor/viewerjs-0.5.8/ViewerJS/index.html#/doc/preview/' + _docMd.identity + '.pdf?atChannel=WEB"' 
	 						+ ' style="width:100%; height:500px;" allowfullscreen webkitallowfullscreen></iframe>';
	 				} else {
	 					return '<p>Document preview not supported</p>';
	 				}
	 			};
	 			
	 			this.htmlType = function() {
	 				return TYPE_HTML;
	 			};
	 			
	 			this.canHandleDoc = function(_docMd) {
	 				return (_docMd.contentType && _docMd.contentType == 'application/pdf') 
	 						|| (_docMd.name && _docMd.name.toLowerCase().endsWith(".pdf"));
	 			};
	 		};
	 		
	 		function ImagePreviewHandler() {
	 			
	 			this.previewHtml = function(_docMd) {
	 				if (this.canHandleDoc(_docMd)) {
	 					return '<img src="/doc/preview/' + _docMd.identity + '?atChannel=WEB" style="max-width:100%; max-height:500px;"></img>'; 
	 				} else {
	 					return '<p>Document preview not supported</p>';
	 				}
	 			};
	 			
	 			this.htmlType = function() {
	 				return TYPE_HTML;
	 			};
	 			
	 			this.canHandleDoc = function(_docMd) {
	 				return _docMd.contentType && _docMd.contentType.indexOf("image") >= 0;
	 			};
	 		};
	 		
	 		function PreviewDataBasedHandler() {
	 			
	 			this.previewHtml = function(_docMd) {
	 				if (this.canHandleDoc(_docMd)) {
	 					return '<ebx-preview-data-viewer doc-metadata="docMetadata"></ebx-preview-data-viewer>'; 
	 				} else {
	 					return '<p>Document preview not supported</p>';
	 				}
	 			};
	 			
	 			this.htmlType = function() {
	 				return TYPE_ANGULAR_HTML;
	 			};
	 			
	 			this.canHandleDoc = function(_docMd) {
	 				return _docMd.previewStatus == 'AVAILABLE';
	 			};
	 		};
	 		
	 		//handlers.push(new PdfPreviewHandler());
	 		handlers.push(new ImagePreviewHandler());
	 		handlers.push(new PreviewDataBasedHandler());
	 		
	 		var getPreviewHandler = function(_docMd) {
	 			
	 			for (var i = 0; i < handlers.length; i++) {
	 				var handler = handlers[i];
	 				if (handler.canHandleDoc(_docMd)) {
	 					return handler;
	 				}
	 			}
	 			
	 			return null;
	 		}
	 		
	 		var getPreviewData = function(_docIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 				"docIdentity": _docIdentity
	 			};
	 			
	 			RESTService.getForData("getDocPreviewData", params, null, _onSuccess, _onError, null);
	 			
	 		};
	 		
	 		var checkPreviewAvailable = function(_docMd) {
	 			var deferred = $q.defer();
	 			deferred.resolve(getPreviewHandler(_docMd) != null);
	 			return deferred.promise;
	 		}
	 		
	 		return {
	 			getPreviewHandler : getPreviewHandler,
	 			getPreviewData : getPreviewData,
	 			checkPreviewAvailable: checkPreviewAvailable
	 		};
	 	}
	 ]);