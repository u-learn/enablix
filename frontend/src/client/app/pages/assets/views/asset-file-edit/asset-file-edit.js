class AssetFileEditController {
    constructor ($scope, $state) {
      this.addProperties = () => {
        
      }
      this.fileUpload = {title: 'Chartered Case Study'};
      this.newProperty = '';

      this.matchedProperties = [];
      const assetFileNestedArray = definedProperties.map(sector => {
        return sector.types.map(type => {
          type.sector = sector.title;
          return type;
        });
      });

      const assetFileArray = assetFileNestedArray[0].concat(...assetFileNestedArray.slice(1));

      //each keydown => call this function
      this.searchMatch = (event) => {
        this.matchedProperties = assetFileArray.filter(asset => {
          if(asset.title.includes(event.target.value)){
            return asset;
          }
        })
      }

    }
  }
  
  AssetFileEditController.$inject = ['$scope', '$state'];
  
  module.exports = AssetFileEditController;

const definedProperties = [
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