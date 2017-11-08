import CompanyPropertiesTemplate from './company-properties.template.html';

  /* @ngInject*/
export default class CompanyProperties {
  constructor () {
    this.template = CompanyPropertiesTemplate;
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