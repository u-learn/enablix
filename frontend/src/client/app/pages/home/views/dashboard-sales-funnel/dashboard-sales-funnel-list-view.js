import { productContentTypeData } from '../../../../helper/data/product-data';

class DashboardSalesFunnelList {
    constructor ($scope, $state) {
        $scope.productContentTypeData = productContentTypeData;

        $scope.goToFunnelView = () => {
            $state.go('home.salesFunnel')
        }

        //show only 3 cards for each content type
        (() => {
            $scope.productContentTypeData.forEach(product => {
                return product.contentTypes.map(c => {
                    if(c.cards.length > 3) {
                        c.cards = c.cards.slice(0, 3);
                    }
                    
                    c.cards.forEach(card => {
                        card.contentType = c.contentType;
                    })

                    return c.cards;
                })
            })
        })();
        
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

DashboardSalesFunnelList.$inject = ['$scope', '$state'];

module.exports = DashboardSalesFunnelList;