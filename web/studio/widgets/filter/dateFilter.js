enablix.filters.filter('ebDate', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateFormat);
    };
});

enablix.filters.filter('ebDateTime', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateTimeFormat);
    };
});