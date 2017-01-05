enablix.studioApp.factory('DocPreviewService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 			
	 		var handlers = [];
	 		
	 		function PdfPreviewHandler() {
	 			
	 			this.previewHtml = function(_docMd) {
	 				if (this.canHandleDoc(_docMd)) {
	 					return '<iframe src="/js/vendor/viewerjs-0.5.8/ViewerJS/index.html#/doc/preview/' + _docMd.identity + '.pdf?atChannel=WEB"' 
	 						+ ' style="width:100%; height:500px;" allowfullscreen webkitallowfullscreen></iframe>';
	 				} else {
	 					return '<p>Document preview not supported</p>';
	 				}
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
	 			
	 			this.canHandleDoc = function(_docMd) {
	 				return _docMd.contentType && _docMd.contentType.indexOf("image") >= 0;
	 			};
	 		};
	 		
	 		handlers.push(new PdfPreviewHandler());
	 		handlers.push(new ImagePreviewHandler());
	 		
	 		var getPreviewHandler = function(_docMd) {
	 			
	 			for (var i = 0; i < handlers.length; i++) {
	 				var handler = handlers[i];
	 				if (handler.canHandleDoc(_docMd)) {
	 					return handler;
	 				}
	 			}
	 			
	 			return null;
	 		}
	 		
	 		return {
	 			getPreviewHandler : getPreviewHandler
	 		};
	 	}
	 ]);