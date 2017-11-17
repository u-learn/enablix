import { dimensionData, cardsData, otherSalesFunnelCardsData } from '../../../../helper/data/cards';
import { productSalesFunnelData } from '../../../../helper/data/product-data';

 
class DashboardSalesFunnel {
    constructor ($scope, $state) {
        $scope.cardsData = cardsData;
        $scope.salesFunnelData = productSalesFunnelData;

        console.log(cardsData)

        const numberOfPlaceholderes = 5 - (cardsData.length % 5);
        if(numberOfPlaceholderes) {
            for(var i = 0; i < numberOfPlaceholderes; i++){
                $scope.cardsData.push({title: 'placeholder'});
            }
        }

        $scope.goToListView = () => {
            $state.go('home.salesFunnelList');
        }

        $scope.showHoverState = (type) => {
            $scope.actionBtnState[type] = true;
        }   

        $scope.hideHoverState = (type) => {
            $scope.actionBtnState[type] = false;
        }   

        $scope.actionBtnState = {
            email: false,
            edit: false,
            attach: false,
            trash: false,

        }
    }
}

DashboardSalesFunnel.$inject = ['$scope', '$state'];

module.exports = DashboardSalesFunnel;