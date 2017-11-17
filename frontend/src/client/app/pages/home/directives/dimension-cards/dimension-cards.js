import DimensionCardsTemplate from './dimension-cards.template.html';

  /* @ngInject*/
export default class DimensionCards {
  constructor ($state) {
    this.template = DimensionCardsTemplate;
    this.restrict = 'E';
    this.scope = {
      state: '=',
      data: '=',
      showAllCards: '@',
      fullWidth: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) { 
    scope.dimensionTypes = Object.keys(scope.data);

    const shortenCompanyProperties = (card) => {
      if(card.companyProperties.length > 3){
        const originalCardLength = card.companyProperties.length;
        card.companyProperties = card.companyProperties.slice(0, 3);
        card.companyProperties.push(`+${originalCardLength - 3}`);
      };
      return card;
    }

    //map data to reformat displayed company properties
    (() => {
      scope.displayData = _.cloneDeep(scope.data);

      if(!scope.showAllCards) {
        for(let a in scope.displayData) {
          scope.displayData[a].cards = scope.displayData[a].cards.map(card => {
            return shortenCompanyProperties(card);
          });
        };
      } else {
        scope.displayData.cards.map(card => {
          return shortenCompanyProperties(card);
        })
      }
    })();

    scope.limitCards = (cards) => {
      const cardsLength = cards.length;
      if (cards.length > 4) {
        cards.splice(3);
        cards.push({remainingCards: `${cardsLength - 3}`});
      };
      return cards;
    };

    scope.hideNewDimForm = true;
    scope.newDimData = {'title': null};
    scope.saveNewCard = () => {
      scope.displayData.cards.unshift({
        'title': scope.newDimData.title
      });
      scope.newDimData = {'title': null};
      scope.hideNewDimForm = true;
    };

    scope.deleteNewCard = () => {
      scope.hideNewDimForm = true;
      scope.newDimData = {'title': null};
    };

    scope.$on('show-new-dim-form', () => scope.hideNewDimForm = false);
    
    element.bind('click', (event) => {

    });
  }
}
