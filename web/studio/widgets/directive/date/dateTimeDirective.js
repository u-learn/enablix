enablix.studioApp.directive('ebDatePicker', [
		'ContentTemplateService',
		function(ContentTemplateService) {

			return {
				restrict : 'E',
				scope : {
					dateValue : '=',
					contentDef : '='
				},
				link : function(scope, element, attrs) {

					var _dataDef = scope.contentDef;

					scope.name = _dataDef.id;
					scope.label = _dataDef.label;
					
					scope.minDate = '2010-01-01';
					scope.maxDate = '2020-12-31';
					
					scope.today = function() {
						scope.dateValue = new Date();
					};

					scope.clear = function() {
						scope.dateValue = null;
					};

					// Disable weekend selection
					scope.disabled = function(date, mode) {
						return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
					};


					scope.open = function($event) {
						$event.preventDefault();
						$event.stopPropagation();

						scope.opened = true;
					};

					scope.dateOptions = {
						formatYear : 'yy',
						startingDay : 1,
						showWeeks : false
					};

					scope.format = enablix.dateFormat;
				},
				templateUrl : "widgets/directive/date/dateTime.html"
			};
		} ]);