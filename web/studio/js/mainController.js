enablix.studioApp.controller('MainStudioCtrl', ['$scope', 'ContentTemplateService', 
    function($scope, ContentTemplateService) {
		ContentTemplateService.getTemplate(enablix.templateId, function(data) {
			enablix.template = data;
		}, function(data) {
			alert("Error retrieving template for id [" + enablix.templateId + "]");
		})
	}                                          
]);