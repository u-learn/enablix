enablix.studioApp.controller('SaveUserController', ['$scope', '$stateParams', 'RESTService', 'UserService', '$rootScope', 'StateUpdateService',
    function($scope, $stateParams, RESTService, UserService, $rootScope, StateUpdateService) {
        $scope.newUser = {};
        var identity = $stateParams.identity;
        UserService.getAllUsers(function(data) {
            $scope.userData = data;
            var user = $scope.userData.filter(function(obj) {
                if (obj.identity === identity)
                    return obj;
            });
            $scope.newUser = user[0];
        });

        $scope.saveUserData = function() {
            UserService.addUserData($scope.newUser);
        };
        $scope.cancelOperation = function() {
            StateUpdateService.goToListUser();
        };
        $scope.checkUserName = function() {
            if ($scope.newUser) {
                UserService.checkUserName($scope.newUser.userId, function(status) {
                    if (status) {
                        $scope.userForm.userName.$setValidity('unexits', false);
                    } else {
                        $scope.userForm.userName.$setValidity('unexits', true);
                    }
                });
            }
        };
    }
]);