import { freeResultsData } from '../../../../helper/data/free-results';

class DashboardSearchResult {
    constructor ($scope, $state) {       
        $scope.data = freeResultsData;   
    }
}

DashboardSearchResult.$inject = ['$scope', '$state'];

module.exports = DashboardSearchResult;