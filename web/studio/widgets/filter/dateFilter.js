enablix.filters.filter('ebDate', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateFormat);
    };
});

enablix.filters.filter('ebDateTime', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        // Small hack to get the time zone abbreviation. Angular date $filter doesn't have inbuilt method to get (https://docs.angularjs.org/api/ng/filter/date)
        // Followed the link http://stackoverflow.com/questions/1954397/detect-timezone-abbreviation-using-javascript
        var rightNow = new Date();
        var timeZoneAbbr = String(String(rightNow).split("(")[1]).split(")")[0];
        return $filter('date')(input, enablix.dateTimeFormat)+" "+timeZoneAbbr+" ";
    };
});