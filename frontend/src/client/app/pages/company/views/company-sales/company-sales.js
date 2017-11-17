class SalesFunnel {
    constructor (_) {
        const contentTypes = [
        {title: 'Data Sheet', active: false},
        {title: 'Blog', active: false},
        {title: 'White Paper', active: false},
        {title: 'Case Study', active: false},
        {title: 'FAQ', active: false},
        {title: 'Discovery Question', active: false},
        {title: 'Battle Card', active: false},
        {title: 'Sales Deck', active: false},
        {title: 'Feature Sheet', active: false},
        {title: 'Demo Environment', active: false},
        {title: 'NDA', active: false},
        {title: 'Demo Script', active: false},
        {title: 'Proposal', active: false}
        ];

        const customizeContentTypes = (contentTypesIndexArray) => {
            const newContentTypes = _.cloneDeep(contentTypes).map((item, index) => {
                if (contentTypesIndexArray.indexOf(index) > -1) {
                    item.active = true;
                } else {
                    item.false;
                }
                return item;
            });
            return newContentTypes;
        };

        this.salesFunnel = [
        {
            funnelName: 'Prospecting',
            contentTypes: customizeContentTypes([0, 1, 2])
        }, {
            funnelName: 'Qualification',
            contentTypes: customizeContentTypes([3, 4, 5])
        }, {
            funnelName: 'Needs Analysis',
            contentTypes: customizeContentTypes([0, 3, 5, 6])
        }, {
            funnelName: 'Value Proposition',
            contentTypes: customizeContentTypes([0, 7, 9, 11])
        }, {
            funnelName: 'Proposal',
            contentTypes: customizeContentTypes([8, 10, 12])
        }, {
            funnelName: 'Negotiation',
            contentTypes: customizeContentTypes([0, 1, 6])
        }];

        this.newSaleStage = {
            funnelName: '',
            contentTypes
        };

        this.showCreateNewStageForm = false;
        this.addNewStage = () => {
            this.showCreateNewStageForm = true;
        }

        this.pushNewStage = () => {
            this.showCreateNewStageForm = false;

            //functionality for saving new stage data
        }
    }
}

SalesFunnel.$inject = ['_'];

module.exports = SalesFunnel;