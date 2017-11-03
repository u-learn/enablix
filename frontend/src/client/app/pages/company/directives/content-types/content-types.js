import ContentTypesTemplate from './content-types.template.html';

  /* @ngInject*/
export default class ContentTypes {
  constructor () {
    this.template = ContentTypesTemplate;
    this.restrict = 'E';
    this.scope = {
      hideHeader: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.tags = [
      {title: 'Data Sheet'},
      {title: 'Blog'},
      {title: 'White Paper'},
      {title: 'Case Study'},
      {title: 'FAQ'},
      {title: 'Discovery Question'},
      {title: 'Battle Card'},
      {title: 'Sales Deck'},
      {title: 'Feature Sheet'},
      {title: 'Demo Environment'},
      {title: 'NDA'},
      {title: 'Demo Script'},
      {title: 'Proposal'}
    ];
    element.bind('click', (event) => {

    });
  }
}
