/*@ngInject*/
export default ($stateProvider) => {
    $stateProvider
        .state('home', {
            url: '/home',
            template: require('./home.template.html'),
            controller: 'HomeController',
            controllerAs: 'ctrl'
        });
};