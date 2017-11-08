import { recentActivityData } from "../../../../helper/data/recent";
class CompanyInformation {
  constructor ($scope, $state) {

    this.navigateToRecentActivity = () => {
      $state.go('recent-activity');
    };

    this.recentActivityData = recentActivityData;
  }
}

CompanyInformation.$inject = ['$scope', '$state'];

module.exports = CompanyInformation;