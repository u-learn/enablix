import ContentTypesTemplate from './content-types.template.html';

  /* @ngInject*/
export default class ContentTypes {
  constructor () {
    this.template = ContentTypesTemplate;
    this.restrict = 'E';
    this.scope = {
      hideHeader: '@',
      forSearchFilter: '@',
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
    element.bind('click', (event) => {

    });
  }
}