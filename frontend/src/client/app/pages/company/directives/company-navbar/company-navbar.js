import CompanyNavbarTemplate from './company-navbar.template.html';

  /* @ngInject*/
export default class CompanyNavbar {
  constructor () {
    this.template = CompanyNavbarTemplate;
    this.restrict = 'E';
    this.scope = {
      tabs: '=',
      emitState: '@'
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
    
    element.bind('click', (event) => {

    });
  }
}
