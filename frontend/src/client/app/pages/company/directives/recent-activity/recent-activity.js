import RecentActivityTemplate from './recent-activity.template.html';

  /* @ngInject*/
export default class RecentActivity {
  constructor () {
    this.template = RecentActivityTemplate;
    this.restrict = 'E';
    this.scope = {
      inSidebar: '@',
      headerData: '=',
      data: '=',
      hideImageFirst: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {

    scope.search = {}
    
    scope.header = scope.headerData ? scope.headerData.title : 'Recent Activities';
    scope.subheader = scope.headerData ? scope.headerData.subheader : 'See what your team members were doing lately';
    
    element.bind('click', (event) => {

    });
  }
}
