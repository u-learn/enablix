import { dimensionData, cardsData, otherSalesFunnelCardsData } from '../../../../helper/data/cards';
import { productSalesFunnelData } from '../../../../helper/data/product-data';

 
class DashboardSalesFunnel {
    constructor ($scope, $state) {
        $scope.cardsData = cardsData;
        $scope.salesFunnelData = productSalesFunnelData;

        $scope.goToListView = () => {
            $state.go('home.salesFunnelList');
        }
    }
}

DashboardSalesFunnel.$inject = ['$scope', '$state'];

module.exports = DashboardSalesFunnel;