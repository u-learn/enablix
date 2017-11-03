import RecentActivityTemplate from './recent-activity.template.html';

  /* @ngInject*/
export default class RecentActivity {
  constructor () {
    this.template = RecentActivityTemplate;
    this.restrict = 'E';
    this.scope = {
      inSidebar: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.data = [
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Company Edit'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Invitation'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Search'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Share'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Company Edit'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Access'},
      {'description': 'Dan changed team URL to brillantAB.enablix.com', 'date': 'March 12, 2017 11:02am', 'type': 'Approval'}
    ];
    
    element.bind('click', (event) => {

    });
  }
}
