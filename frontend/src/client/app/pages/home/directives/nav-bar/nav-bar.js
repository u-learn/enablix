import NavBarTemplate from './nav-bar.template.html';

  /* @ngInject*/
export default class NavBar {
  constructor () {
    this.template = NavBarTemplate;
    this.restrict = 'E';
    this.scope = {
      state: '='
      // tabs: '=',
      // emitState: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) { 
    scope.isConsolidateContentState = scope.state.current.name.includes('consolidate-content');
    scope.isDashboardState = scope.state.current.name.includes('home') || scope.state.current.name.includes('dashboard');
    scope.isCompanyState = scope.state.current.name.includes('company');
  }
}
