import CompanyPropertiesTemplate from './company-properties.template.html';

  /* @ngInject*/
export default class CompanyProperties {
  constructor () {
    this.template = CompanyPropertiesTemplate;
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
      scope.$emit('dimOrObjSelected', tagData);
    }

    scope.removeTag = (tagData) => {
      if(scope.showClose){
        scope.$emit('removedimOrObjSelected', tagData);
      }
    }
    element.bind('click', (event) => {
      
    });
  }
}