import CompanyNavbarTemplate from './company-navbar.template.html';
import { consolidateContentData, consolidateContentTabs } from '../../../../helper/data/consolidate-content';
import { companyTabs } from '../../../../helper/data/company';

  /* @ngInject*/
export default class CompanyNavbar {
  constructor () {
    this.template = CompanyNavbarTemplate;
    this.restrict = 'E';
    this.scope = {
      tabs: '=',
      emitState: '@',
      state: '=',
      page: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.activeTab = null;

    scope.activateTab = (tab) => {
      scope.activeTab = tab;
      if (scope.emitState) scope.$emit('change-consolidate-content-tab', tab.sref);
    };

    if (scope.state) {
      const pageTabs = scope.page === 'company' ? companyTabs : consolidateContentTabs;
      const defaultActiveTab = pageTabs.filter(tab => {
        return scope.state.current.name === tab.sref;
      })[0];
      scope.activateTab(defaultActiveTab);
    }
    
    element.bind('click', (event) => {

    });
  }
}
