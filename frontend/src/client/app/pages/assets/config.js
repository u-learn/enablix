/*@ngInject*/
export default ($stateProvider) => {
    $stateProvider
      .state('assets', {
        url: '/assets',
        template: require('./assets.template.html'),
        controller: 'AssetsController',
        controllerAs: 'ctrl',
      })
      .state('assets.text', {
        url: '/text',
        template: require('./views/asset-text/asset-text.template.html'),
        controller: require('./views/asset-text/asset-text.js'),
        controllerAs: 'ctrl',
      })
      .state('assets.text-edit', {
        url: '/text-edit',
        template: require('./views/asset-text-edit/asset-text-edit.template.html'),
        controller: require('./views/asset-text-edit/asset-text-edit.js'),
        controllerAs: 'ctrl',
      })
      .state('assets.url', {
        url: '/url',
        template: require('./views/asset-url/asset-url.template.html'),
        controller: require('./views/asset-url/asset-url.js'),
        controllerAs: 'ctrl',
      })
      .state('assets.url-edit', {
        url: '/url-edit',
        template: require('./views/asset-url-edit/asset-url-edit.template.html'),
        controller: require('./views/asset-url-edit/asset-url-edit.js'),
        controllerAs: 'ctrl',
      })
      .state('assets.file', {
        url: '/file',
        template: require('./views/asset-file/asset-file.template.html'),
        controller: require('./views/asset-file/asset-file.js'),
        controllerAs: 'ctrl',
      })
      .state('assets.file-edit', {
        url: '/file-edit',
        template: require('./views/asset-file-edit/asset-file-edit.template.html'),
        controller: require('./views/asset-file-edit/asset-file-edit.js'),
        controllerAs: 'ctrl',
      })
}