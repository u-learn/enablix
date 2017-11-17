import RelevanceDropdownTemplate from './relevance-dropdown.template.html';

export default class RelevanceDropdown {
  constructor () {
    this.template = RelevanceDropdownTemplate;
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
    scope.search = {};    
    scope.displayData = [];

    scope.displayDropdown = () => {
      scope.displayData = _.clone(objectData);
      console.log(scope.displayData)
    };

    scope.searchObject = () => {
      const input = scope.search.object;
      
      if(input.length){
        //filter data from the table
        scope.displayData = filterData(scope.displayData, input);
      } else {
        scope.displayData = _.clone(objectData); //reset the data
      }
    };

    scope.hideDropdown = () => {
      scope.$emit('closeRelevanceDropdown');
    };

    scope.addNewTag = (tagData) => {
      scope.$emit('addRelevanceTag', tagData);
    };

    element.bind('click', (event) => {

    });
  }
}


const filterData = (data, searchInput) => {
  return data.filter(d => d.title.includes(searchInput)); 
}

const objectData = [{
  title: 'Automated Cloud',
  type: 'Competitors',
  color: 'red'
}, {
    title: 'IdentityGuard',
    type: 'Product',
    color: 'purple'
}, {
    title: 'IdentityGuard Mobile',
    type: 'Product',
    color: 'purple'
}, {
    title: 'Identity',
    type: 'Industries',
    color: 'gold'
}];