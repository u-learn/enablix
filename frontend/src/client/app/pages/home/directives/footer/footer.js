import FooterTemplate from './footer.html';

  /* @ngInject*/
export default class Footer {
  constructor () {
    this.template = FooterTemplate;
    this.restrict = 'E';
    this.scope = {
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
