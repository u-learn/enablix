class CompanyInformation {
  constructor ($scope, $state) {
    this.navigateToRecentActivity = () => {
      $state.go('recent-activity');
    };
      // $scope.company = {};
  }
}

CompanyInformation.$inject = ['$scope', '$state'];

module.exports = CompanyInformation;