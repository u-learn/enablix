enablix.studioApp.directive('ebxTabularReport', [
	 '$compile',
	function($compile) {

		return {
			restrict: "E",
			replace: true,
			scope: {
				data: "=",
				columndetails: "=?",
				dispatch: "=?"
			},
			transclude: false,
			link: function(scope, element, attrs) {

				
				 function render(data) {
						d3.selectAll("table").remove();
						var table = d3.select(element[0]).append('table').attr("class", "tabReporttable datatable");
						var thead = table.append('thead');
						var	tbody = table.append('tbody');

						// append the header row
						thead.append('tr')
						  .selectAll('th')
						  .data(scope.columndetails).enter()
						  .append('th').attr('class', 'tabReportth')
						    .text(function (column) { return column.head; });

						// create a row for each object in the data
						var rows = tbody.selectAll('tr')
						  .data(data)
						  .enter()
						  .append('tr');

						// create a cell in each row for each column
						var cells = rows.selectAll('td')
						  .data(function (row) {
						    return scope.columndetails.map(function (column) {
						      return {column: column.head, value: row[column.column]};
						    });
						  })
						  .enter()
						  .append('td').attr('class', 'tabReporttd')
						    .text(function (d) { return d.value; });

					}

				
				scope.$watch("data", function() {
					render(scope.data);
				}, true);
			},
			templateUrl: "widgets/directive/tabularReport/tabularReport.html"
		};
	}]);