enablix.studioApp.controller('UserController', 
			['$scope', '$state', '$q', 'RESTService','UserService', '$rootScope', 'StateUpdateService', 'ConfirmationModalWindow', 'ContentTemplateService',
	function( $scope,   $state,   $q,   RESTService,  UserService,   $rootScope,   StateUpdateService,   ConfirmationModalWindow,   ContentTemplateService) {

			$scope.breadcrumbList = 
				[
					{ label: "Setup" },
					{ label: "User Management" }
				];

			$scope.dataFilters = {};
			
			function fetchData() {
				UserService.searchTenantUsers($scope.dataFilters, function(data) {	
					$scope.userData = data.content;				 
				}, function() {    		
					Notification.error({message: "Error fetching users", delay: enablix.errorMsgShowTime});
				});
			};

			$scope.onSearch = function(_filterValues) {
				// remove search keys with null values as those get treated as null value on back end
				removeNullProperties(_filterValues);
				
				$scope.dataFilters = _filterValues;
				
				fetchData();
			}
			
			var userRoleItemDef = {
					bounded : {
						refList : {
							datastore : {
								location: "TENANT_DB",
								storeId: "ebx_role",
								dataId: "_id",
								dataLabel: "roleName"
							}
						}
					}
				};
			
			$scope.filters = 
				[
					{
	                    id: "userRoleIn",
	                    type: "multi-select",
	                    name: "User Role",
	                    masterList: function() { // This must return a promise
	                        var deferred = $q.defer();
	                        
	                        ContentTemplateService.getBoundedValueList(enablix.templateId, userRoleItemDef, null, function(data) {
	                        	data.sort(sortByLabelProp);
	                        	deferred.resolve(data);
	        				}, function(errorData) {
	        					Notification.error({message: "Error retrieving roles", delay: enablix.errorMsgShowTime});
	        				});
	                        
	                        return deferred.promise;
	                    },
	                    defaultValue: function() {
	                    	 return [];
						},
	                    filterValueTransformer: function(_selectedValues) {
	                    	
	                        if (_selectedValues && _selectedValues.length > 0) {
	                            var returnVal = [];

	                            angular.forEach(_selectedValues, function(val) {
	                                returnVal.push(val.id);
	                            });

	                            return returnVal;
	                            
	                        } else {
	                            return null;
	                        }
	                    }
					}
				];
			
			$scope.addUser = function() {
				StateUpdateService.goAddUser();
			}

			$scope.userRecordView = function(identity) {
				StateUpdateService.goViewUser(identity);
			}	 

			$scope.userRecordEdit = function(identity) {
				StateUpdateService.goEditUser(identity);
			}	 

			$scope.deleteUserRecord=function(identity) {

				var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
						"Do you want to continue?", 
						"Proceed", "Cancel");

				confirmModal.result.then(function(confirmed) {

					if (confirmed) {
						UserService.deleteUser(identity,function(data){
							init();
						});
					}
				});			 
			}

		}
	]);			
