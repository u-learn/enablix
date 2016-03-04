enablix.studioApp.config(function ($translateProvider) {

	$translateProvider.preferredLanguage('enUS');

	$translateProvider.useStaticFilesLoader({
		  prefix: '/widgets/messages/',
		  suffix: '.json'
		});
});