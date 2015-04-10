enablix.studioApp.directive('upload', ['FileUploadService', function factory(FileUploadService) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            $(element).fileupload({
                dataType: 'text',
                add: function (e, data) {
                	FileUploadService.clear();
                	FileUploadService.add(data);
                },
                progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    FileUploadService.setProgress(progress);
                },
                done: function (e, data) {
                	FileUploadService.setProgress(0);
                }
            });
        }
    };
}]);