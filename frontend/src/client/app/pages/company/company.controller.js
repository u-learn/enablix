import { companyTabs } from '../../helper/data/company';

class CompanyController {
  constructor ($state) {
    this.state = $state;
    this.companyTabs = companyTabs;
  }
}

CompanyController.$inject = ['$state'];

module.exports = CompanyController;