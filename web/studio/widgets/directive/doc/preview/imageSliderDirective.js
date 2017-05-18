enablix.studioApp.directive('ebxImageSlider', function() {

	return {
		restrict: 'E',
		scope : {
			slides : "="		
		},

		link: function(scope, element, attrs) {
			scope.element = element;
		},

		controller: 'ImageSliderCtrl',
		templateUrl: "widgets/directive/doc/preview/imageSlider.html"
	}
});

enablix.studioApp.controller('ImageSliderCtrl', 
			['$scope', '$timeout', 'DocPreviewService', 'DocService',
	function ($scope,   $timeout,   DocPreviewService,   DocService) {
				
	
		$scope.jssor_1_slider_init = function() {
	
			if ($scope.slides && $scope.slides.length > 0) {
				
				var firstSlide = $scope.slides[0];
				
				if (firstSlide.width && firstSlide.height) {
					
					var w = firstSlide.width;
					var h = firstSlide.height;

					var adjustedHeight = (h/w)*500;

					$($scope.element).find(".jssor-cont").height(adjustedHeight);
					$($scope.element).find(".jssor-slides").height(adjustedHeight);
					
				}
				
			}
			
			var jssor_1_options = {
				$AutoPlay : 0,
				$ArrowNavigatorOptions : {
					$Class : $JssorArrowNavigator$
				},
				$BulletNavigatorOptions : {
					$Class : $JssorBulletNavigator$
				}
			};
	
			var jssor_1_slider = new $JssorSlider$("jssor_1", jssor_1_options);
	
			/* responsive code begin */
			/*
			 * remove responsive code if you don't want the slider scales
			 * while window resizing
			 */
			function ScaleSlider() {
				var refSize = jssor_1_slider.$Elmt.parentNode.clientWidth;
				if (refSize) {
					refSize = Math.min(refSize, 600);
					jssor_1_slider.$ScaleWidth(refSize);
				} else {
					window.setTimeout(ScaleSlider, 30);
				}
			}
			ScaleSlider();
			$Jssor$.$AddEvent(window, "load", ScaleSlider);
			$Jssor$.$AddEvent(window, "resize", ScaleSlider);
			$Jssor$.$AddEvent(window, "orientationchange", ScaleSlider);
			/* responsive code end */
		};

		if ($scope.slides && $scope.slides.length > 0) {
			$timeout(function() {
				$scope.jssor_1_slider_init();
			});
		}
		
		$scope.$watchCollection("slides", function(newValue, oldValue) {
			if (newValue != null && (oldValue == null || newValue.length != oldValue.length)) {
				$timeout(function() {
					$scope.jssor_1_slider_init();
				});
			}
		}, true)
	
}]);
