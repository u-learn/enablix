class CompanyProperties {
    constructor () {
        this.defaultTags = [
            {title: 'Data Sheet'},
            {title: 'Blog'},
            {title: 'White Paper'},
            {title: 'Case Study'},
            {title: 'FAQ'},
            {title: 'Discovery Question'},
            {title: 'Battle Card'},
            {title: 'Sales Deck'},
            {title: 'Feature Sheet'},
            {title: 'Demo Environment'},
            {title: 'NDA'},
            {title: 'Demo Script'},
            {title: 'Proposal'}
          ];
          
        this.sectors = [
            {
                title: 'Market Tiers',
                types: [
                    {title: 'Market Tier 1'},
                    {title: 'Market Tier 2'},
                    {title: 'Market Tier 3'}
                ]
            }, {
                title: 'Regions',
                types: [
                    {title: 'North America'},
                    {title: 'Scandinavia'},
                    {title: 'Balkan'},
                    {title: 'Middle East'}
                ]
            }, {
                title: 'Buyer Personas',
                types: [
                    {title: 'Technical'},
                    {title: 'Financial'},
                    {title: 'Sales Oriented'}
                ]
            }, {
                title: 'Industries',
                types: [
                    {title: 'Healthcare Payer & Providers'},
                    {title: 'Insurance'},
                    {title: 'Energy'},
                    {title: 'Financial Servies, Banking & Capital Markets'},
                    {title: 'Other'},
                    {title: 'Life Sciences and Pharma'}
                ]
            }, {
                title: 'Partner Types',
                types: [
                    {title: 'Managed Service Providers'},
                    {title: 'OEM Partner'},
                    {title: 'Reseller'},
                    {title: 'value Added Reseller'},
                    {title: 'Technology Integrator'}
                ]
            }
        ];
    }
}

CompanyProperties.$inject = [];

module.exports = CompanyProperties;