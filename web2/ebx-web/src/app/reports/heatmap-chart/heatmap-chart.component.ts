import { Component, OnInit, ViewEncapsulation, Input, ElementRef, Output, EventEmitter } from '@angular/core';

import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-heatmap-chart',
  templateUrl: './heatmap-chart.component.html',
  styleUrls: ['./heatmap-chart.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HeatmapChartComponent implements OnInit {

  @Input() options: any;
  @Output() onDataClick = new EventEmitter<any>();

  mergedOptions: any;
  
  _data: any;
  watchDataChange = false;

  dispatch: any;

  el: HTMLElement;

  constructor(private element: ElementRef) { 
    this.el = element.nativeElement;
  }

  @Input() set data(dt: any) {
    this._data = dt;
    if (this.watchDataChange && this.options && this._data) {
      this.render();
    }
  }

  get data() {
    return this._data;
  }

  ngOnInit() {

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

    if (this.options) {
      this.mergedOptions = Object.assign(options, this.options);
    } else {
      this.mergedOptions = options;
    }

    this.dispatch = d3.dispatch("click", "mouseover", "mouseout", "mousemove");
    
    this.dispatch.on("click", (e) => {
      if (this.mergedOptions.onClick) {
        this.mergedOptions.onClick(e);
      }
      return true;
    });

    this.setupTooltip();

    if (this.data) {
      this.render();
    }

    this.watchDataChange = true;
  }

  setupTooltip() {
          
    var tooltip = d3.select(".hm-tooltip");
    
    if (tooltip.empty()) {
      
      tooltip = d3.select(this.element.nativeElement)
            .append("div")
            .style("position", "absolute")
            .style("z-index", "10")
            .style("visibility", "hidden")
            .text("a simple tooltip")
            .classed("hm-tooltip", true);
    }
    
    this.dispatch.on("mouseover", (e) => {
      return tooltip.text(e.y + ": " + e.x + " (" + e.value + ")").style("visibility", "visible");
    })
    
    this.dispatch.on("mousemove", (e) => {
      let evt: any = d3.event;
      return tooltip.style("top", (evt.layerY - 10) + "px").style("left", (evt.layerX + 10) + "px");
    })
    
    this.dispatch.on("mouseout", (e) => {
      return tooltip.style("visibility", "hidden");
    });
    
  }

  getCategory(d) {
    var dc = "All";
    if (this.mergedOptions.categorize) {
      dc = d.category ? d.category : "Others";
    }
    return dc;
  }

  render() {

    var w = this.el.parentElement.offsetWidth;
    var h = this.mergedOptions.height || this.el.parentElement.offsetHeight;
    var width = w - this.mergedOptions.margin.left - this.mergedOptions.margin.right;
    var height = h - this.mergedOptions.margin.top - this.mergedOptions.margin.bottom;

    d3.select(this.el).select("svg").remove();

    var svg = d3.select(this.el).append("svg")
      .attr("class", "nvd3")
      .attr("width", width + this.mergedOptions.margin.left + this.mergedOptions.margin.right)
      .attr("height", height + this.mergedOptions.margin.top + this.mergedOptions.margin.bottom);

    var svgG = svg.append("g")
      .attr("transform", "translate(" + this.mergedOptions.margin.left + "," + this.mergedOptions.margin.top + ")");

    var xu = {};
    var x = [];
    var yu = {};
    var y = [];
    var yc = [];
    
    var categories: string[] = [];
    var categoryInfo = {}; // order map with values starting for 1 when data needs to be categorized
    
    for (var i = 0; i < this.data.length; i++) {
      
      var dc = this.getCategory(this.data[i]);
                  
      if (typeof(categoryInfo[dc]) == "undefined") {
        categories.push(dc);
        categoryInfo[dc] = {
            yLabels: []
        };
      }
      
      if (typeof(xu[this.data[i].x]) == "undefined") {
        x.push(this.data[i].x);
      }
      xu[this.data[i].x] = 0;
      if (typeof(yu[dc + "-" + this.data[i].y]) == "undefined") {
        categoryInfo[dc].yLabels.push(this.data[i].y);
      }
      yu[dc + "-" + this.data[i].y] = 0;
      
    }

    if (this.options.restrictedXLabels && this.options.restrictedXLabels.length > 0) {
      x = this.options.restrictedXLabels;
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
      
      this.options.yLabelCategories.forEach((yLabelCat) => {
        
        if (!Utility.arrayContains(categories, yLabelCat)) {
          
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
    for (var d = 0; d < this.data.length; d++) {
      
      var xIndx = x.indexOf(this.data[d].x);
      
      if (xIndx >= 0) {
        this.data[d].xIndex = x.indexOf(this.data[d].x);
        this.data[d].yIndex = yc.indexOf(this.getCategory(this.data[d]) + "-" + this.data[d].y);
        dataMatrix[this.data[d].xIndex][this.data[d].yIndex] = 1;
        allData.push(this.data[d]);
      }
      
    }
    
    if (this.mergedOptions.fillEmptyAsZero) {
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
    
    var legendElementWidth = Math.floor(width * this.mergedOptions.legendWidth / (this.mergedOptions.buckets));
    var legendElementHeight = height / 20;
    legendElementHeight = Math.min(legendElementHeight, 20);

    var yLabels = svgG.selectAll(".hm-yLabel")
      .data(y)
      .enter().append("text")
      .text((d) => { return d; })
      .attr("x", 0)
      .attr("y", (d, i) => { return i * yGridSize; })
      .style("text-anchor", "end")
      .style("font-weight", (d) => { return typeof(categoryInfo[d]) == "undefined" ? "normal" : "bold"})
      .style("font-size", "12px")
      .attr("transform", "translate(-6," + yGridSize / 1.5 + ")")
      .attr("class", (d, i) => { return ("hm-yLabel hm-axis"); });

    var xLabels = svgG.selectAll(".hm-xLabel")
      .data(x)
      .enter().append("text")
      .text((d) => { return d; })
      .attr("y", (d, i) => { return i * xGridSize; })
      .attr("x", 0)
      .style("text-anchor", "start")
      .style("font-size", "12px")
      .attr("transform", "rotate(-90) translate(10, " + xGridSize / 2 + ")")
      .attr("class", (d, i) => { return ("hm-xLabel hm-axis"); });

    var colorScales = [];
    if (this.mergedOptions.breaks != null && this.mergedOptions.breaks.length > 0) {
      for (var b in this.mergedOptions.colors) {
        colorScales.push(d3.scale.quantile()
          .domain([0, this.mergedOptions.buckets - 1, d3.max(this.data, (d: any) => { return d.value; })])
          .range(this.mergedOptions.colors[b]));
      }
    } else {
      colorScales.push(d3.scale.quantile()
        .domain([0, this.mergedOptions.buckets - 1, d3.max(this.data, (d: any) => { return d.value; })])
        .range(this.mergedOptions.colors));
    }

    var maxY = 0;
    
    var cards = svgG.selectAll(".hm-square")
      .data(allData);

    var cardsG = cards.enter().append("g");
    
    cardsG.on("click", (d) => { this.dispatch.click(d); })
        .on("mouseover", (d) => { this.dispatch.mouseover(d); })
        .on("mouseout", (d) => { this.dispatch.mouseout(d); })
        .on("mousemove", (d) => { this.dispatch.mousemove(d); });
    
    var rects = cardsG.append("rect")
      .filter((d) => { return d.value != null && d.xIndex >= 0 && d.yIndex >= 0; })
      .attr("x", (d) => { return d.xIndex * xGridSize; })
      .attr("y", (d) => { return d.yIndex * yGridSize; })
      .attr("class", "hm-square")
      .attr("width", xGridSize)
      .attr("height", yGridSize)
      .attr("stroke-width", 1)
      .attr("stroke", "black")
      .style("fill", "#ffffff");
    
    if (this.mergedOptions.valueText) {
      var textVals = cardsG.append("text")
        .attr("x", (d) => { return d.xIndex * xGridSize + xGridSize/2; })
        .attr("y", (d) => { var _y = d.yIndex * yGridSize + yGridSize/2 + 4; maxY = Math.max(maxY, _y); return _y; })
        .attr("class", (d, i) => { return ("hm-yLabel"); })
        .style("text-anchor", "middle")
        .style("font-size", "12px")
        .text((d) => { return d.value; });
    }
    
    rects.transition().duration(this.mergedOptions.duration).style("fill", (d) => {
      if (this.mergedOptions.customColors && this.mergedOptions.customColors.hasOwnProperty(d.value)) {
        return this.mergedOptions.customColors[d.value];
      } else if (this.mergedOptions.breaks != null && this.mergedOptions.breaks.length > 0) {
        for (var b in this.mergedOptions.breaks) {
          if (d.xIndex < this.mergedOptions.breaks[b]) {
            return colorScales[b](d.value);
          }
        }
        return colorScales[this.mergedOptions.breaks.length](d.value);
      } else {
        return colorScales[0](d.value);
      }
    });

    cards.exit().remove();
    
    // draw category separators
    /* if (categories.length > 1) {
      
      var categorySeps = svgG.selectAll("hm-category-separator").data(categories);
      
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
      
      var noDataCatMessages = svgG.selectAll("hm-no-data-category").data(noDataCategories);
      
      var noDataG = noDataCatMessages.enter().append("g");            
      noDataG.append("text")
        .attr("x", (d, i) => { return d.xIndex * xGridSize; })
        .attr("y", (d, i) => { var _y = d.yIndex * yGridSize + yGridSize/2 + 5; maxY = Math.max(maxY, _y); return _y; })
        .attr("class", "hm-no-data-category")
        .style("text-anchor", "left")
        .style("font-size", "12px")
        .text((d) => { return "No data found for " + d.label; });
      
      noDataCatMessages.exit().remove();
    } 

    if (this.mergedOptions.legend && this.data && this.data.length > 0) {

      var legendY = maxY + 40;
      
      let clrScale: any[] = [0].concat(colorScales[0].quantiles()).concat(d3.max(this.data, (d: any) => { return d.value; }));
      var legend = svgG.selectAll(".hm-legend")
        .data(clrScale, (d) => { return d; });

      legend.enter().append("g").attr("class", "hm-legend");

      legend.append("rect")
        .attr("x", function(d, i) { return legendElementWidth * i; })
        .attr("y", legendY) //height * 1.05)
        .attr("width", legendElementWidth)
        .attr("height", legendElementHeight)
        .style("fill", (d, i) => { return this.mergedOptions.colors[i]; })
        .style("visibility", (d, i) => { return(i < this.mergedOptions.buckets ? "visible" : "hidden") });

      legend.append("text")
        .attr("class", "hm-legendLabel")
        .text((d) => { return Math.round(d); })
        .attr("x", (d, i) => { return legendElementWidth * i; })
        .attr("y", legendY + legendElementHeight + 15) //height * 1.15)
        .style("text-anchor", "middle")
        .style("font-size", "12px");

      legend.exit().remove();
      
      maxY = legendY + legendElementHeight + 15;

    }
    
    if (maxY > height) {
      svg.attr('height', (maxY + 200));
      this.element.nativeElement.style.height = (maxY + 200) + "px";
    }
    
  }

}
