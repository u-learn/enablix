import CardsTemplate from './cards.template.html';

  /* @ngInject*/
export default class Cards {
  constructor () {
    this.template = CardsTemplate;
    this.restrict = 'E';
    this.scope = {
      state: '=',
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
    console.log(scope.data);
    element.bind('click', (event) => {

    });
  }
}
