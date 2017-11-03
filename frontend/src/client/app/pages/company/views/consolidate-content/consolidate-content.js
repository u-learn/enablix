class ConsolidateContent {
    constructor ($state, $scope) {
        this.state = $state;
        this.consolidateContentTabs = consolidateContentTabs;

        $scope.$on('change-consolidate-content-tab', (event, data) => {
            this.filterConsolidateContent(data);
        });

        this.selectedAsset = (assetId) => {
            $state.go('consolidate-content.edit', { params: { assetId } });
        };
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

const consolidateContentTabs = [{
  title: 'All',
  sref: 'consolidate-content'
}, {
  title: 'My Drafts',
  sref: 'consolidate-content.drafts'
}, {
  title: 'Pending',
  sref: 'consolidate-content.pending'
}, {
  title: 'Quality Check',
  sref: 'consolidate-content.quality-check'
}];

const consolidateContentData = [
  {
    'id': 1,
    'description': 'How to Boost Your Traffic Of Your Blog And Destroy The Competition',
    'thumbanil': 'assets/images/covers/consolidate-content-thumbnail-1.jpg',
    'date': 'March 12, 2017 11:02am',
    'type': 'Data Sheet',
    'status': 'Draft',
    'creator': 'Dan Mazig'
  },
  {
    'id': 2,
    'description': 'What Is The Big R For Marketing Your Business',
    'thumbanil': 'assets/images/covers/consolidate-content-thumbnail-2.png',
    'date': 'March 12, 2017 11:02am',
    'type': 'Blog',
    'status': 'Pending',
    'creator': 'Lester Carson'
  },
  {
    'id': 3,
    'description': 'Importance Of The Custom Company Logo Design',
    'thumbanil': 'assets/images/covers/consolidate-content-thumbnail-3.jpg',
    'date': 'March 12, 2017 11:02am',
    'type': 'Case Study',
    'status': 'Pending',
    'creator': 'Leo Gomez'
  },
  {
    'id': 4,
    'description': 'Writing A Good Headline For Your Advertisement?',
    'thumbanil': 'assets/images/covers/consolidate-content-thumbnail-4.jpg',
    'date': 'March 12, 2017 11:02am',
    'type': 'FAQ',
    'status': 'Approved',
    'creator': 'Lester Carson'
  },
  {
    'id': 5,
    'description': 'Trade Show Promotions',
    'thumbanil': 'assets/images/covers/consolidate-content-thumbnail-4.jpg',
    'date': 'March 12, 2017 11:02am',
    'type': 'FAQ',
    'status': 'Quality Check',
    'creator': 'Lester Carson'
  }

];
