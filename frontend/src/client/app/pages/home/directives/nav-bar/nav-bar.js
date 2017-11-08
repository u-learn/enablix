import NavBarTemplate from './nav-bar.template.html';
import { searchBarData } from '../../../../helper/data/search-data';

  /* @ngInject*/
export default class NavBar {
  constructor () {
    this.template = NavBarTemplate;
    this.restrict = 'E';
    this.scope = {
      state: '='
      // tabs: '=',
      // emitState: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.isConsolidateContentState = scope.state.current.name.includes('consolidate-content');
    scope.isDashboardState = scope.state.current.name.includes('home') || scope.state.current.name.includes('dashboard');
    scope.isCompanyState = scope.state.current.name.includes('company');
    scope.isRecentActivitiesState = scope.state.current.name.includes('recent-activity');

    scope.searchBarData = searchBarData;

    scope.openUploadPrompt = (type) => {
      scope.uploadOptions = false;
      scope.$emit('upload-prompt', {type});
    };

    scope.showUploadOptions = () => {
      scope.uploadOptions = true;
    };

    scope.goToConsolidateState = () => {
      scope.state.go('consolidate-content');
    };

    scope.goToCompanyBusinessState = () => {
      scope.state.go('company.info');
    };

    scope.showNotifications = () => {
      scope.showNotificationsPopup = !scope.showNotificationsPopup;
    }

    scope.fireError = () => {
      scope.$emit('fireError');
    }
  };
}