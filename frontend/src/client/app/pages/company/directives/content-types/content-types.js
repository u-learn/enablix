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
    // const d = _.clone(scope.data);

    // console.log(scope.data);
    
    //modify display data for search filters 
    // const displayData = (data) => {
    //   if (data.length > 4){
    //     const firstFour = data.slice(0, 4);
    //     firstFour.push(`+${data.length - 4}`);
    //     return firstFour;
    //   } else {
    //     return data;
    //   }
    // };

    scope.data = scope.data ? scope.data : defaultTags;
    
    element.bind('click', (event) => {

    });
  }
}

const defaultTags = [
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