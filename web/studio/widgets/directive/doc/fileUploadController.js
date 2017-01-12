enablix.studioApp.controller('FileUploadCtrl', 
			['$scope', 'FileUploader', 'ContentTemplateService', 'QIdUtil', 'InfoModalWindow',
    function ($scope,   FileUploader,   ContentTemplateService,   QIdUtil,   InfoModalWindow) {
    	
		var uploadUrl = enablix.serviceURL.uploadDocument;
		
		var fileSizeCheckFilter = function(item) {
			
			var passed = item.size < enablix.uploadDocSizeLimit;
			
			if (!passed) {
				InfoModalWindow.showInfoWindow("Error", "File size should be less than " + (enablix.uploadDocSizeLimit/(1024*1024)) + "MB");
			}
			
			return passed;
		}
		
        var uploader = $scope.uploader = new FileUploader({
            url: uploadUrl,
            withCredentials: true,
            removeAfterUpload: false
        });

    	
        // FILTERS

        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });
        
        /*uploader.filters.push({
        	name: 'sizeCheckFilter',
            fn: fileSizeCheckFilter
        });*/

        // CALLBACKS

        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function(fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function(addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function(item) {
            console.info('onBeforeUploadItem', item);
            var fd = {
        			fileSize: item.file.size, 
        			contentQId: $scope.qualifiedId,
        			temporary: $scope.temporary
        		};
            
            var fileContainerQId = QIdUtil.getParentQId(fd.contentQId);
            if (!isNullOrUndefined($scope.parentIdentity) 
            		&& !ContentTemplateService.isRootContainer(enablix.template, fileContainerQId)) {
            	fd['parentIdentity'] = $scope.parentIdentity;
            }
            
            if (!isNullOrUndefined($scope.containerIdentity)) {
            	fd['containerIdentity'] = $scope.containerIdentity;
            }
            
            if (!isNullOrUndefined($scope.docIdentity)) {
            	fd['docIdentity'] = $scope.docIdentity;
            }
            
            item.formData.push(fd);
        };
        uploader.onProgressItem = function(fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function(progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
            console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
            alert("Error: " + response.message);
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
            $scope.docValue = response;
        };
        uploader.onCompleteAll = function() {
            console.info('onCompleteAll');
        };

        console.info('uploader', uploader);
}]);
