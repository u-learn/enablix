class HomeController {
  constructor ($state, $scope) {
    this.state = $state;
    this.showPromptOverlay = false;
    this.showError = false;

    $scope.$on('upload-prompt', (event, params) => { 
      this.showPromptOverlay = true; 
      $scope.$broadcast(`upload-prompt-${params.type}`);
    });

    $scope.$on('hide-overlay-prompt', () => {
      this.showPromptOverlay = false;
    });

    $scope.$on('fireError', () => {
      this.showError = true;
    });

    this.hideError = () => {
      this.showError = false;
    };
  }
}

HomeController.$inject = ['$state', '$scope'];

module.exports = HomeController;