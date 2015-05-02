/*
	@license Angular Treeview version 0.1.6
	ⓒ 2013 AHN JAE-HA http://github.com/eu81273/angular.treeview
	License: MIT


	[TREE attribute]
	angular-treeview: the treeview directive
	tree-id : each tree's unique id.
	tree-model : the tree model on $scope.
	node-id : each node's id
	node-label : each node's label
	node-children: each node's children

	<div
		data-list-group-tree="true"
		data-tree-id="tree"
		data-list-group-tree-model="roleList"
		data-node-id="roleId"
		data-node-label="roleName"
		data-node-children="children"
		data-node-level="0">
	</div>
*/

(function ( angular ) {
	'use strict';

	angular.module( 'listGroupTreeview', [] ).directive( 'listGroupTreeModel', ['$compile', function( $compile ) {
		return {
			restrict: 'A',
			link: function ( scope, element, attrs ) {
				//tree id
				var treeId = attrs.treeId;
			
				//tree model
				var treeModel = attrs.listGroupTreeModel;

				//node id
				var nodeId = attrs.nodeId || 'id';

				//node label
				var nodeLabel = attrs.nodeLabel || 'label';

				//children
				var nodeChildren = attrs.nodeChildren || 'children';

				var nodeLevel = attrs.nodeLevel || '0';
				nodeLevel = parseInt(nodeLevel, 10);
				
				var ulClass = nodeLevel == 0 ? "list-group" : "list-group inner";
				
				var listItemPadding = (nodeLevel + 1) * 15;
				
				//tree template
				var template =
					'<ul class="' + ulClass + '">' +
						'<li class="list-group-item" data-ng-repeat="node in ' + treeModel + '" data-ng-init="node.collapsed = true">' +
							'<span class="tree-node-data" data-ng-class="{\'active\': node.selected == \'selected\'}" data-ng-click="' + treeId + '.selectNodeLabel(node)">' 
								+ '<span class="tree-node-label" style="padding-left: ' + listItemPadding + 'px" data-ng-class="node.uiClass">{{node.' + nodeLabel + '}}</span>'
								+ '<i ng-class="{\'glyphicon-chevron-left\' : node.collapsed, \'glyphicon-chevron-down\' : !node.collapsed}" ' 
										+ ' data-ng-show="node.' + nodeChildren + '.length" data-ng-click="' + treeId + '.selectNodeHead(node, $event)" class="glyphicon pull-right">&nbsp;</i>'
						 + '</span>' +
							'<div data-ng-hide="node.collapsed" data-tree-id="' + treeId + '" data-list-group-tree-model="node.' + nodeChildren + '" data-node-id=' + nodeId 
								+ ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' data-node-level=' + (nodeLevel + 1) + '></div>' +
						'</li>' +
					'</ul>';


				//check tree id, tree model
				if( treeId && treeModel ) {

					//root node
					if( attrs.listGroupTreeview ) {
					
						//create tree object if not exists
						scope[treeId] = scope[treeId] || {};

						//if node head clicks,
						scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function( selectedNode, $event ){

							//Collapse or Expand
							selectedNode.collapsed = !selectedNode.collapsed;
							
							$event.stopPropagation();
						};

						//if node label clicks,
						var defaultSelectNode = function( selectedNode ){

							//remove highlight from previous node
							if( scope[treeId].currentNode && scope[treeId].currentNode.selected ) {
								scope[treeId].currentNode.selected = undefined;
							}

							//set highlight to selected node
							selectedNode.selected = 'selected';

							//set currentNode
							scope[treeId].currentNode = selectedNode;
						};
						
						var customSelectNodeFn = scope[treeId].selectNodeCallback;
						scope[treeId].selectNodeLabel = customSelectNodeFn ? function(selectedNode) {
							defaultSelectNode(selectedNode);
							customSelectNodeFn(selectedNode);
						} : defaultSelectNode; 
					
					}

					//Rendering template.
					element.html('').append( $compile( template )( scope ) );
				}
			}
		};
	}]);
})( angular );
