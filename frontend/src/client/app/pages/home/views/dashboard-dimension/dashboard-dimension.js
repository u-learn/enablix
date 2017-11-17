import { dimensionData } from '../../../../helper/data/cards';
// 
class DashboardDimension {
    constructor ($scope, $state) {
        $scope.dimensionData = dimensionData.Products;
        $scope.dimensionTitle = "Products"
        
        //handle even spacing in last row by injecting placeholder card
        const remainderOf4CardsPerRow = dimensionData.Products.cards.length % 4;
        if(remainderOf4CardsPerRow !== 0){
            const numberOfPlaceholder = 4 - remainderOf4CardsPerRow;
            for(let i = 0; i < numberOfPlaceholder; i++){
                $scope.dimensionData.cards.push({title: "placeholder", companyProperties: []});
            }
        };

        $scope.addNewDim = () => {
            $scope.$broadcast('show-new-dim-form');
        }
        
    }
  }
  
  DashboardDimension.$inject = ['$scope', '$state'];
  
  module.exports = DashboardDimension;