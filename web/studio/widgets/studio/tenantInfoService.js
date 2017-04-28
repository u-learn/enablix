enablix.studioApp.factory('TenantInfoService', 
[
 	         '$location', '$window',
 	function ($location,   $window) {
 		
    	var TENANT_ID_PREFIX = "/t/";
		var TENANT_ID_PREFIX_OFFSET = TENANT_ID_PREFIX.length;
		
		var CLIENT_ID_PREFIX = "c/";
		var CLIENT_ID_PREFIX_OFFSET = CLIENT_ID_PREFIX.length;
		
 	    var tenantInfo = {};
 	        	 
 		var init = function() {
 			
 			var path = window.location.pathname;
 			
 			if (path.startsWith(TENANT_ID_PREFIX)) {
 				// get the tenant id from url
 				var nextSlash = path.indexOf("/", TENANT_ID_PREFIX_OFFSET);
 				var tenantId = path.substr(TENANT_ID_PREFIX_OFFSET, nextSlash - TENANT_ID_PREFIX_OFFSET);
 				
 				tenantInfo.tenantId = tenantId;
 				
 				path = path.substr(nextSlash + 1);
 				if (path.startsWith(CLIENT_ID_PREFIX)) {
 					// get the client id from url
	 				var nextSlash = path.indexOf("/", CLIENT_ID_PREFIX_OFFSET);
	 				var clientId = path.substr(CLIENT_ID_PREFIX_OFFSET, nextSlash - CLIENT_ID_PREFIX_OFFSET);
	 				
	 				tenantInfo.clientId = clientId;
 				}
 				
 			}
 			
 			var urlParams = $location.search();
 			if (isNullOrUndefined(tenantInfo.tenantId)) {
 				tenantInfo.tenantId = urlParams.tenantId;
 			}
 			
 			if (isNullOrUndefined(tenantInfo.clientId)) {
 				tenantInfo.clientId = urlParams.clientId;
 			}
 			
 		};
 		
 		var initFromLoggedInUser = function(_loggedInUser) {
 			tenantInfo.tenantId = _loggedInUser.user.tenantId;
 			tenantInfo.clientId = _loggedInUser.userProfile.systemProfile.defaultClientId;
 		}
 		
 		var getTenantInfo = function() {
 			return tenantInfo;
 		};
 		
 		var getTenantContextUrl = function() {
 			
 			var ctxUrl = "";
 			
 			if (!isNullOrUndefined(tenantInfo.tenantId)) {
 				
 				ctxUrl = TENANT_ID_PREFIX + tenantInfo.tenantId;
 				
 				if (!isNullOrUndefined(tenantInfo.clientId)) {
 					ctxUrl += "/" + CLIENT_ID_PREFIX + tenantInfo.clientId;
	 			}
 			}
 			
 			return ctxUrl;
 		}
 		
 		return {
 			init: init,
 			initFromLoggedInUser: initFromLoggedInUser,
 			getTenantInfo: getTenantInfo,
 			getTenantContextUrl: getTenantContextUrl
 		};
 	}
]);