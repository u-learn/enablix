import ContentTypesTemplate from './content-types.template.html';

  /* @ngInject*/
export default class ContentTypes {
  constructor () {
    this.template = ContentTypesTemplate;
    this.restrict = 'E';
    this.scope = {
      hideHeader: '@',
      forSearchFilter: '@',
      data: '=',
      showClose: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.selectedTag = (tagData) => {

      //don't want tags selected to be clickable
      if(!scope.showClose){
        scope.$emit('contentTypeSelected', tagData);
      }
    };

    scope.removeTag = (tagData) => {
      if(scope.showClose){
        scope.$emit('removeContentTypeSelected', tagData);
      }
    }

    element.bind('click', (event) => {

    });
  }
}