import SimpleSidebarTemplate from './simple-sidebar.html';


  /* @ngInject*/
export default class SimpleSidebar {
  constructor () {
    this.template = SimpleSidebarTemplate;
    this.restrict = 'E';
    this.scope = {
      data: '='
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {

    scope.isFirstFunnel = (idx) => {
      return idx > 0;
    };

    element.bind('click', (event) => {

    });
  }
}
