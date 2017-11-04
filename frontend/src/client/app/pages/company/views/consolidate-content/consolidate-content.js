import { consolidateContentData, consolidateContentTabs } from '../../../../helper/data/consolidate-content';

class ConsolidateContent {
    constructor ($state, $scope) {
        this.state = $state;
        this.consolidateContentTabs = consolidateContentTabs;

        $scope.$on('change-consolidate-content-tab', (event, data) => {
            this.filterConsolidateContent(data);
        });

        this.selectedAsset = (assetId) => {
            $state.go('consolidate-content.edit', { params: { assetId } });
            this.editState = true;
        };

        this.editState = this.state.includes('consolidate-content.edit');

        this.showRadioButtonOption = (idx) => {
            this.activeRadionBtn = idx;
        }

        this.hideRadioButtonOption = () => {
            this.activeRadionBtn = null;
        }
    }

    filterConsolidateContent (data) {
        if (data.includes('pending')) {
            this.consolidateContentData = consolidateContentData.filter(data => {
                if (data.status === 'Pending') return data;
            });
        } else if (data.includes('drafts')) {
            this.consolidateContentData = consolidateContentData.filter(data => {
                if (data.status === 'Draft') return data;
            });
        } else if (data.includes('quality-check')) {
            this.consolidateContentData = consolidateContentData.filter(data => {
                if (data.status === 'Quality Check') return data;
            });
        } else {
            this.consolidateContentData = consolidateContentData;
        }
    }
}

ConsolidateContent.$inject = ['$state', '$scope'];

module.exports = ConsolidateContent;