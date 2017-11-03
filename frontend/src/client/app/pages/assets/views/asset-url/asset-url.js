class AssetTextController {
    constructor ($scope, $state) {
      this.state = $state;
      // $state.current.name
      

    //   this.navigateToRecentActivity = () => {
    //     $state.go('recent-activity');
    //   };
    }
  }
  
  AssetTextController.$inject = ['$scope', '$state'];
  
  module.exports = AssetTextController;