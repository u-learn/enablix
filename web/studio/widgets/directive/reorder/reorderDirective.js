enablix.studioApp.directive('ebxReorder', 
				 ['RESTService', 'Notification',
		 function (RESTService,   Notification) {

			return {
				restrict: 'E',
				scope : {
					collectionName: '@',
					reorderRecordFilter: "=?",
					reorderRecordFilterMetadata: "=?",
					currentRecord: "=",
					orderedDataList: "=",
					identityPropId: "@",
					orderPropId: "@"
				},
		
				link: function(scope, element, attrs) {
					
					var identityPropId = scope.identityPropId || "identity";
					var orderPropId = scope.orderPropId || "order";
					
					if (!isNullOrUndefined(scope.orderedDataList)) {
						transformArrayToDoubleLinkedList(scope.orderedDataList);
					}
					
					// swap order with the next. this method moves record1 down to record2 
					var swapOrder = function(_record1, _record2) {
						
						var rec1Order = _record1[orderPropId];
						var rec2Order = _record2[orderPropId]
						
						var data = {
								record1Identity: _record1[identityPropId],
								record2Identity: _record2[identityPropId],
								record1NewOrder: rec2Order,
								record2NewOrder: rec1Order,
								collectionName: scope.collectionName
							}
						
						RESTService.postForData("swapRecordOrder", null, data, null, function(result) {
							
							if (result) {
								
								_record1[orderPropId] = rec2Order;
								_record2[orderPropId] = rec1Order;
								
								sortDataList();
								transformArrayToDoubleLinkedList(scope.orderedDataList);
							} 
							
						}, function(errorData) {
							Notification.error({message: "Error re-ordering records", delay: enablix.errorMsgShowTime});
						});
						
					};
					
					var sortDataList = function() {
						scope.orderedDataList.sort(function(o1, o2) {
							return o1[orderPropId] - o2[orderPropId];
						});
					}
					
					scope.moveUp = function() {
						if (scope.currentRecord.previousNode != null) {
							swapOrder(scope.currentRecord.previousNode, scope.currentRecord);
						}
					};
					
					scope.moveDown = function() {
						if (scope.currentRecord.nextNode != null) {
							swapOrder(scope.currentRecord, scope.currentRecord.nextNode);
						}
					};
					
					scope.$watchCollection("orderedDataList", function(newValue, oldValue) {
						if (newValue != null && (oldValue == null || newValue.length != oldValue.length)) {
							transformArrayToDoubleLinkedList(newValue);
						}
					}, true)
					
				},
				templateUrl: "widgets/directive/reorder/reorder.html"
			}
	
		}
]);