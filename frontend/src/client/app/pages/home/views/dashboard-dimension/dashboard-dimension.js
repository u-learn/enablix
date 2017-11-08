import { dimensionData } from '../../../../helper/data/cards';
// 
class DashboardDimension {
    constructor ($scope, $state) {
        $scope.dimensionData = dimensionData.Products;
        $scope.dimensionTitle = 'Products';

        $scope.addNewDim = () => {
            $scope.$broadcast('show-new-dim-form');
        }
        
    }
  }
  
  DashboardDimension.$inject = ['$scope', '$state'];
  
  module.exports = DashboardDimension;