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

				function td_data(row, i) {
				  	return scope.columndetails.map(function(c) {
				        // compute cell values for this specific row
				        var cell = {};
				        d3.keys(c).forEach(function(k) {
				          	cell[k] = typeof c[k] == 'function' ? c[k](row,i) : c[k];
				        });
				        return cell;
				 	  });
				}

				function length() {
				  var fmt = d3.format('02d');
				  return function(l) { return Math.floor(l / 60) + ':' + fmt(l % 60) + ''; };
				}
				function render(){
					d3.selectAll("table").remove();
					
					var table = d3.select(element[0])
							.append('table').attr("class","tabReporttable");
					
					table.append('thead').append('tr')
					    .selectAll('th')
					    .data(scope.columndetails).enter()
					    .append('th')
					    .text(d3.f('head'))
					     .attr('class', 'tabReportth');
					
					table.append('tbody')
					.appendMany(scope.data, 'tr')
					.appendMany(td_data, 'td')
					.html(d3.f('html'))
					 .attr('class', 'tabReporttd');
				}
				scope.$watch("data", function() {
					render();
				}, true);
			},
			templateUrl: "widgets/directive/tabularReport/tabularReport.html"
		};
	}]);