enablix.studioApp.controller('FileUploadCtrl',
    ['$scope', '$rootScope', 'FileUploadService', 
    function ($scope, $rootScope, FileUploadService) {
    $scope.files = [];
    $scope.percentage = 0;

    $scope.upload = function () {
    	FileUploadService.upload();
        $scope.files = [];
    };

    $rootScope.$on('fileAdded', function (e, call) {
        $scope.files.push(call);
        $scope.$apply();
    });

    $rootScope.$on('filesCleared', function (e, call) {
        $scope.files = [];
        $scope.$apply();
    });
    
    $rootScope.$on('uploadProgress', function (e, call) {
        $scope.percentage = call;
        $scope.$apply();
    });
}]);