enablix.studioApp.directive('focus',
['$timeout',
function($timeout) {
 return {
 scope : {
   trigger : '@focus'
 },
 link : function(scope, element) {
  scope.$watch('trigger', function(value) {
    if (value === "true") {
      $timeout(function() {
       element[0].focus();
      });
   }
 });
 }
};
}]); 

enablix.studioApp.directive('back', ['$window', function($window) {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                elem.bind('click', function () {
                    $window.history.back();
                });
            }
        };
    }]);


enablix.studioApp
    .directive("ngJsonFileModel", [function () {
        return {
            scope: {
                ngJsonFileModel: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var reader = new FileReader();
                    reader.onload = function (loadEvent) {
                        scope.$apply(function () {
                            scope.ngFileModel = {
                                name: changeEvent.target.files[0].name,
                                size: changeEvent.target.files[0].size,
                                type: changeEvent.target.files[0].type,
                                data: JSON.parse(loadEvent.target.result)
                            };
                        });
                    }
                    reader.readAsText(changeEvent.target.files[0]);
                });
            }
        }
    }]);
