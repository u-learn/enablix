class CompanyInformation {
  constructor ($scope, $state) {

    this.navigateToRecentActivity = () => {
      $state.go('recent-activity');
    };
  }
}

CompanyInformation.$inject = ['$scope', '$state'];

module.exports = CompanyInformation;