import { recentActivityData } from '../../../../helper/data/recent';

class RecentActivity {
    constructor ($state) {
        this.state = $state;
        this.recentActivityData = recentActivityData;
    }
}

RecentActivity.$inject = ['$state'];

module.exports = RecentActivity;