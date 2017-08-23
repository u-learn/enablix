enablix.studioApp.controller('UserController', 
			['$scope', '$state', '$q', 'RESTService','UserService', '$rootScope', 'StateUpdateService', 'ConfirmationModalWindow', 'ContentTemplateService',
	function( $scope,   $state,   $q,   RESTService,  UserService,   $rootScope,   StateUpdateService,   ConfirmationModalWindow,   ContentTemplateService) {

			$scope.breadcrumbList = 
				[
					{ label: "Setup" },
					{ label: "User Management" }
				];

			$scope.dataFilters = {};
			
			$scope.userData = [];
			
			$scope.pagination = {
		    	 		"pageSize" : 10,
		    	 		"pageNum" : 0,
					 	"sort" : {
					 		"field" : "name",
					 		"direction" : "ASC"
					 	}
		    		};
			
			$scope.tableHeaders = [{
	                desc: "Name",
	                valueKey: "name",
	                sortProperty: "name"
	            },
	            {
	                desc: "Email",
	                valueKey: "email",
	                sortProperty: "email"
	            },
	            {
	                desc: "Roles",
	                valueFn: function(record) {
	                	
	                	var roles = record.systemProfile.roles;
	                	
	                	var roleStr = "";
	                	for (var i = 0; i < roles.length; i++) {
	                		if (i > 0 && i == (roles.length - 1)) {
	                			roleStr += " and ";
	                		} else if (i != 0) {
	                			roleStr += ", ";
	                		}
	                		roleStr += roles[i].roleName;
	                	}
	                	
	                    return roleStr;
	                }
	            }
	        ];
			
			$scope.addUser = function() {
				StateUpdateService.goAddUser();
			}

			$scope.userRecordView = function(record) {
				StateUpdateService.goViewUser(record.identity);
			}	 

			$scope.userRecordEdit = function(record) {
				StateUpdateService.goEditUser(record.identity);
			}	 

			$scope.deleteUserRecord = function(record) {

				var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
						"Do you want to continue?", 
						"Proceed", "Cancel");

				confirmModal.result.then(function(confirmed) {

					if (confirmed) {
						UserService.deleteUser(record.identity, function(data){
							
							$scope.pagination.pageNum = 0;
							$scope.fetchData();
							
						});
					}
				});			 
			}
			
			$scope.tableRecordActions = 
				[{
					actionName: "Detail",
					tooltip: "Details",
					iconClass: "fa fa-eye",
					tableCellClass: "details",
					actionCallbackFn: $scope.userRecordView
				},
				{
					actionName: "Edit",
					tooltip: "Edit",
					iconClass: "fa fa-pencil",
					tableCellClass: "edit",
					actionCallbackFn: $scope.userRecordEdit
				},
				{
					actionName: "Remove",
					tooltip: "Delete",
					iconClass: "fa fa-times",
					tableCellClass: "remove",
					actionCallbackFn: $scope.deleteUserRecord
				}];
			
			$scope.fetchData = function() {
				UserService.searchTenantUsers($scope.dataFilters, $scope.pagination, 
					function(data) {	
						$scope.userData = data.content;	
						$scope.pageData = data;
					}, function() {    		
						Notification.error({message: "Error fetching users", delay: enablix.errorMsgShowTime});
					});
			};

			$scope.onSearch = function(_filterValues) {
				// remove search keys with null values as those get treated as null value on back end
				removeNullProperties(_filterValues);
				
				$scope.pagination.pageNum = 0;
				$scope.dataFilters = _filterValues;
				
				$scope.fetchData();
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
			
			

		}
	]);			
