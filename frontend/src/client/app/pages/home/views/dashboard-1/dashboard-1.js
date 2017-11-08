import { cardsData, dimensionData } from '../../../../helper/data/cards';
import { recentData } from '../../../../helper/data/recent';

class Dashboard {
    constructor ($scope, $state) {
        $scope.cardsData = cardsData.slice(0, 5);
        $scope.dimensionData = dimensionData;
        $scope.recentData = recentData;
        
        $scope.navigateToRecentActivity = () => {

        }

        $scope.sideBarData = {
            'title': 'Recent',
            'subheader': 'What assets have been uploaded and approved recently by your team mates'
        }
    }
}

Dashboard.$inject = ['$scope', '$state'];

module.exports = Dashboard;