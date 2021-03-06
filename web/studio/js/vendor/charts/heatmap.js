var debounce = function(f, timeout) {
	var id = -1;
	return function() {
		if (id > -1) {
			window.clearTimeout(id);
		}
		id = window.setTimeout(f, timeout);
	}
};

angular.module("heatmap", []).directive("heatmap",
	function() {
		return {
			restrict: "E",
			replace: true,
			scope: {
				data: "=",
				options: "=?",
				dispatch: "=?"
			},
			transclude: false,
			template: "<div></div>",
			link: function(scope, element) {

				var options = {
					legend: true,
					margin: { top: 50, right: 0, bottom: 100, left: 50 },
					buckets: 9,
					colors: ["#ffffd9", "#edf8b1", "#c7e9b4", "#7fcdbb", "#41b6c4", "#1d91c0", "#225ea8", "#253494", "#081d58"],
					duration: 1000,
					legendWidth: 0.3,
					breaks: null,
					valueText: true,
					categorize: false,
					fillEmptyAsZero: false,
					restrictedXLabels: []
				};

				if (scope.options) {
					options = angular.extend(options, scope.options);
				}

				scope.dispatch = d3.dispatch("click", "mouseover", "mouseout", "mousemove");
				
				// set up tooltip
				var setupTooltip = function() {
					
					var tooltip = d3.select(".hm-tooltip");
					
					if (tooltip.empty()) {
						
						tooltip = d3.select(element[0])
									.append("div")
									.style("position", "absolute")
									.style("z-index", "10")
									.style("visibility", "hidden")
									.text("a simple tooltip")
									.classed("hm-tooltip", true);
					}
					
					scope.dispatch.on("mouseover", function(e) {
						return tooltip.text(e.y + ": " + e.x + " (" + e.value + ")").style("visibility", "visible");
					})
					
					scope.dispatch.on("mousemove", function(e) {
						return tooltip.style("top", (d3.event.layerY - 10) + "px").style("left", (d3.event.layerX + 10) + "px");
					})
					
					scope.dispatch.on("mouseout", function(e) {
						return tooltip.style("visibility", "hidden");
					});
					
				}
				
				setupTooltip();

				var getCategory = function(d) {

					var dc = "All";
						
					if (options.categorize) {
						dc = d.category ? d.category : "Others";
					}

					return dc;
				}
				
				var render = function() {

					var w = element[0].offsetWidth;
					var h = element[0].offsetHeight;
					var width = w - options.margin.left - options.margin.right;
					var height = h - options.margin.top - options.margin.bottom;

					d3.select(element[0]).select("svg").remove();

					var svg = d3.select(element[0]).append("svg")
						.attr("width", width + options.margin.left + options.margin.right)
						.attr("height", height + options.margin.top + options.margin.bottom)
						.append("g")
						.attr("transform", "translate(" + options.margin.left + "," + options.margin.top + ")");

					var xu = {};
					var x = [];
					var yu = {};
					var y = [];
					var yc = [];
					
					var categories = [];
					var categoryInfo = {}; // order map with values starting for 1 when data needs to be categorized
					
					for (var i = 0; i < scope.data.length; i++) {
						
						var dc = getCategory(scope.data[i]);
												
						if (typeof(categoryInfo[dc]) == "undefined") {
							categories.push(dc);
							categoryInfo[dc] = {
									yLabels: []
							};
						}
						
						if (typeof(xu[scope.data[i].x]) == "undefined") {
							x.push(scope.data[i].x);
						}
						xu[scope.data[i].x] = 0;
						if (typeof(yu[dc + "-" + scope.data[i].y]) == "undefined") {
							categoryInfo[dc].yLabels.push(scope.data[i].y);
						}
						yu[dc + "-" + scope.data[i].y] = 0;
						
					}
			
					if (scope.options.restrictedXLabels && scope.options.restrictedXLabels.length > 0) {
						x = scope.options.restrictedXLabels;
					}
					
					// populate y axis labels
					var noDataCategories = [];
					
					if (categories.length == 1 && categories[0] === 'All') {
						// if there is only one category and that value is 'All', then do not show category label
						var ct = categories[0];
						y = categoryInfo[ct].yLabels.sort();
						for (var yi = 0; yi < y.length; yi++) {
							yc.push(ct + "-" + y[yi]);
						}
					
					} else {
						
						// categories.sort();
						
						for (var c = 0; c < categories.length; c++) {
							var dc = categories[c];
							y.push(dc);
							yc.push("category-" + dc);

							var categoryYs = categoryInfo[dc].yLabels.sort();
							for (var cy = 0; cy < categoryYs.length; cy++) {
								y.push(categoryYs[cy]);
								yc.push(dc + "-" + categoryYs[cy]);
							}
						}
						
						angular.forEach(scope.options.yLabelCategories, function(yLabelCat) {
							if (!categories.contains(yLabelCat)) {
								
								if (noDataCategories.length == 0) {
									// add a separator
									y.push("");
									yc.push("");
								}
								
								noDataCategories.push({label: yLabelCat});
								
								categories.push(yLabelCat);
								categoryInfo[yLabelCat] = {};
								
								y.push(""); // do not show label
								yc.push("category-" + yLabelCat);
							}
						});
					}
					
					x.sort();
					
					var dataMatrix = [];
					for (var d = 0; d < x.length; d++) {
						dataMatrix[d] = new Array(yc.length);
					}
					
					var allData = [];
					// populate x and y index on data
					for (var d = 0; d < scope.data.length; d++) {
						
						var xIndx = x.indexOf(scope.data[d].x);
						
						if (xIndx >= 0) {
							scope.data[d].xIndex = x.indexOf(scope.data[d].x);
							scope.data[d].yIndex = yc.indexOf(getCategory(scope.data[d]) + "-" + scope.data[d].y);
							dataMatrix[scope.data[d].xIndex][scope.data[d].yIndex] = 1;
							allData.push(scope.data[d]);
						}
						
					}
					
					if (options.fillEmptyAsZero) {
						for (var m = 0; m < dataMatrix.length; m++) {
							for (var n = 0; n < dataMatrix[m].length; n++) {
								if (!dataMatrix[m][n] && !yc[n].startsWith("category")) {
									// push zero data 
									allData.push({
										x: x[m], // labels
										y: y[n], // labels
										value: 0,
										xIndex: m,
										yIndex: n
									});
								}
							}
						}
					}
					
					// populate index for no data found
					for (var d = 0; d < noDataCategories.length; d++) {
						noDataCategories[d].xIndex = 0;
						noDataCategories[d].yIndex = yc.indexOf("category-" + noDataCategories[d].label);
					}

					var xGridSize = Math.floor(width / x.length);
					xGridSize = Math.min(xGridSize, 60);
					
					var yGridSize = Math.floor(height / y.length);
					
					yGridSize = Math.min(yGridSize, 30);
					yGridSize = Math.max(yGridSize, 20);
					
					var legendElementWidth = Math.floor(width * options.legendWidth / (options.buckets));
					var legendElementHeight = height / 20;
					legendElementHeight = Math.min(legendElementHeight, 20);

					var yLabels = svg.selectAll(".hm-yLabel")
						.data(y)
						.enter().append("text")
						.text(function (d) { return d; })
						.attr("x", 0)
						.attr("y", function (d, i) { return i * yGridSize; })
						.style("text-anchor", "end")
						.style("font-weight", function(d) { return typeof(categoryInfo[d]) == "undefined" ? "normal" : "bold"})
						.style("font-size", "12px")
						.attr("transform", "translate(-6," + yGridSize / 1.5 + ")")
						.attr("class", function (d, i) { return ("hm-yLabel hm-axis"); });

					var xLabels = svg.selectAll(".hm-xLabel")
						.data(x)
						.enter().append("text")
						.text(function(d) { return d; })
						.attr("y", function(d, i) { return i * xGridSize; })
						.attr("x", 0)
						.style("text-anchor", "start")
						.style("font-size", "12px")
						.attr("transform", "rotate(-90) translate(10, " + xGridSize / 2 + ")")
						.attr("class", function(d, i) { return ("hm-xLabel hm-axis"); });

					var colorScales = [];
					if (options.breaks != null && options.breaks.length > 0) {
						for (b in options.colors) {
							colorScales.push(d3.scale.quantile()
								.domain([0, options.buckets - 1, d3.max(scope.data, function(d) { return d.value; })])
								.range(options.colors[b]));
						}
					} else {
						colorScales.push(d3.scale.quantile()
							.domain([0, options.buckets - 1, d3.max(scope.data, function(d) { return d.value; })])
							.range(options.colors));
					}

					var maxY = 0;
					
					var cards = svg.selectAll(".hm-square")
						.data(allData);

					var cardsG = cards.enter().append("g");
					
					cardsG.on("click", function(d) { scope.dispatch.click(d); })
							.on("mouseover", function(d) { scope.dispatch.mouseover(d); })
							.on("mouseout", function(d) { scope.dispatch.mouseout(d); })
							.on("mousemove", function(d) { scope.dispatch.mousemove(d); });
					
					var rects = cardsG.append("rect")
						.filter(function(d) { return d.value != null && d.xIndex >= 0 && d.yIndex >= 0; })
						.attr("x", function(d) { return d.xIndex * xGridSize; })
						.attr("y", function(d) { return d.yIndex * yGridSize; })
						.attr("class", "hm-square")
						.attr("width", xGridSize)
						.attr("height", yGridSize)
						.attr("stroke-width", 1)
						.attr("stroke", "black")
						.style("fill", "#ffffff");
					
					if (options.valueText) {
						var textVals = cardsG.append("text")
							.attr("x", function(d) { return d.xIndex * xGridSize + xGridSize/2; })
							.attr("y", function(d) { var _y = d.yIndex * yGridSize + yGridSize/2 + 4; maxY = Math.max(maxY, _y); return _y; })
							.attr("class", function (d, i) { return ("hm-yLabel"); })
							.style("text-anchor", "middle")
							.style("font-size", "12px")
							.text(function(d) { return d.value; });
					}
					
					rects.transition().duration(options.duration).style("fill", function(d) {
						if (options.customColors && options.customColors.hasOwnProperty(d.value)) {
							return options.customColors[d.value];
						} else if (options.breaks != null && options.breaks.length > 0) {
							for (b in options.breaks) {
								if (d.xIndex < options.breaks[b]) {
									return colorScales[b](d.value);
								}
							}
							return colorScales[options.breaks.length](d.value);
						} else {
							return colorScales[0](d.value);
						}
					});

					cards.exit().remove();
					
					// draw category separators
					/* if (categories.length > 1) {
						
						var categorySeps = svg.selectAll("hm-category-separator").data(categories);
						
						categorySeps.enter().append("rect")
							.attr("x", function(d, i) { return 0; })
							.attr("y", function(d, i) { return yc.indexOf("category-" + d) * yGridSize; })
							.attr("class", "hm-category-separator")
							.attr("width", function(d, i) { return x.length * xGridSize})
							.attr("height", yGridSize)
							.attr("stroke-width", 1)
							.attr("stroke", "#f5f5f5")
							.style("fill", "#f5f5f5");
						
						categorySeps.exit().remove();
					} */
					
					// draw no data categories
					if (noDataCategories.length > 0) {
						
						var noDataCatMessages = svg.selectAll("hm-no-data-category").data(noDataCategories);
						
						var noDataG = noDataCatMessages.enter().append("g");						
						noDataG.append("text")
							.attr("x", function(d, i) { return d.xIndex * xGridSize; })
							.attr("y", function(d, i) { var _y = d.yIndex * yGridSize + yGridSize/2 + 5; maxY = Math.max(maxY, _y); return _y; })
							.attr("class", "hm-no-data-category")
							.style("text-anchor", "left")
							.style("font-size", "12px")
							.text(function(d) { return "No data found for " + d.label; });
						
						noDataCatMessages.exit().remove();
					} 

					if (options.legend && scope.data && scope.data.length > 0) {

						var legendY = maxY + 40;
						
						var legend = svg.selectAll(".hm-legend")
							.data([0].concat(colorScales[0].quantiles()).concat(d3.max(scope.data, function (d) { return d.value; })), function(d) { return d; });

						legend.enter().append("g").attr("class", "hm-legend");

						legend.append("rect")
							.attr("x", function(d, i) { return legendElementWidth * i; })
							.attr("y", legendY) //height * 1.05)
							.attr("width", legendElementWidth)
							.attr("height", legendElementHeight)
							.style("fill", function(d, i) { return options.colors[i]; })
							.style("visibility", function(d, i) { return(i < options.buckets ? "visible" : "hidden") });

						legend.append("text")
							.attr("class", "hm-legendLabel")
							.text(function(d) { return Math.round(d); })
							.attr("x", function(d, i) { return legendElementWidth * i; })
							.attr("y", legendY + legendElementHeight + 15) //height * 1.15)
							.style("text-anchor", "middle")
							.style("font-size", "12px");

						legend.exit().remove();
						
						maxY = legendY + legendElementHeight + 15;

					}
					
					if (maxY > height) {
						element[0].style.height = (maxY + 200) + "px";
					}
					

				};

				scope.$watch("data", function() {
					render();
				}, true);
				
				d3.select(window).on("resize", debounce(function() {
					render();
				}, 500));

			}
		}
	}
);
