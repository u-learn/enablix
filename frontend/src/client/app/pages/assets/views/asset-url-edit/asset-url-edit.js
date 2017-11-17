class AssetUrlEditController {
    constructor ($scope, $state) {
    this.url = 'https://medium.com/swlh/premature-scaling-why-it-kills-startups-and-how-to-avoid-it-500677f45024';

    this.showDropdown = false;
    this.showRelevanceDropdown = () => {
      this.showDropdown = true;
    }
    this.relevanceTags = [];
    $scope.$on('addRelevanceTag', (event, data) => {
      this.relevanceTags.push(data);
      this.showDropdown = false;
    })

    $scope.$on('closeRelevanceDropdown', () => {
      this.showDropdown = false;
    });
  }
}
  
  AssetUrlEditController.$inject = ['$scope', '$state'];
  
  module.exports = AssetUrlEditController;