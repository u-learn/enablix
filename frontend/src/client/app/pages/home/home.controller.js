class HomeController {
  constructor ($state) {
    this.state = $state;
  }
}

HomeController.$inject = ['$state'];

module.exports = HomeController;