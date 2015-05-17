enablix.filters.filter('ebDate', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateFormat);
    };
});