import SearchBarTemplate from './search-bar.html';

/* @ngInject*/
export default class SearchBar {
    constructor () {
        this.template = SearchBarTemplate;
        this.restrict = 'E';
        this.scope = {
            data: '=',
            state: '='
        };
        this.transclude = true;
    }

    // optional compile function
    compile (tElement) {
        return this.link.bind(this);
    }

    // optional link function
    link (scope, element, attributes) {
        scope.search = {};

        const searchBarPage = currentStateOfFilter(scope.state);
        const originalDataSource = dataSource(scope.data, searchBarPage);
        const tempData = _.cloneDeep(originalDataSource);

        const isDimensionState = scope.state.current.name.includes('dimension');
        const isSalesFunnel = scope.state.current.name.includes('salesFunnel');
        const isDashboard = scope.state.current.name.includes('dashboard');
     
        const calculateScopeData = (tempData, calculateRemainingItems) => {
            scope.differentFilters = tempData.filtersData;
            if (isDashboard) {
                scope.rowData1 = tempData.rowData1;
                scope.rowData2 = truncateData('objects', tempData.rowData2, 2, calculateRemainingItems);
                scope.rowData3 = truncateData('contentTypes', tempData.rowData3, 4, calculateRemainingItems);
            } else if (isSalesFunnel) {
                scope.rowData1 = tempData.rowData1;
                scope.rowData2 = tempData.rowData2;
                scope.rowData3 = tempData.rowData3;
            } else if (isDimensionState) {
                scope.rowData1 = tempData.rowData1;
                scope.rowData2 = tempData.rowData2;
                scope.rowData3 = tempData.rowData3;
            }
        };

        calculateScopeData(tempData, true);

        scope.showSearchBar = () => { scope.showFilteredResults = true; };
        scope.hideSearchBar = () => {            
            scope.showFilteredResults = false; 
        };
        scope.showFilteredResults = false;
 
        scope.updateFilters = () => {
            const searchQuery = scope.search.input;
            const allData = _.cloneDeep(originalDataSource);

            if(scope.search.input){
                scope.showFilteredResults = true;
                const filteredData = {
                    rowData1: filterResults(allData.rowData1, searchQuery),
                    rowData2: filterResults(allData.rowData2, searchQuery),
                    rowData3: filterResults(allData.rowData3, searchQuery)
                };

                //find all filter results with empty arrays
                const validFilters = ['rowData1', 'rowData2', 'rowData3'].filter(type => {
                    return filteredData[type].length !== 0;
                });

                filteredData.filtersData = validFilters.map(dataType => dataFilterMap[searchBarPage][dataType]);

                calculateScopeData(filteredData, true);
            } else {
                scope.showFilteredResults = false;
                calculateScopeData(allData, true);
            }
        };

        // console.log("rowData3", scope.rowData3)
        // console.log("rowData1", scope.rowData1)
        // console.log("objects", scope.rowData2)
        
        //container holding selected types
        scope.selectedContentTypes = [];
        scope.selectedDimOrObj = [];

        if(isDimensionState){
            scope.selectedDimOrObj = [{
                color: 'purple',
                title: 'Product'
            }]
        } else if (isSalesFunnel){
            scope.selectedDimOrObj = [{
                color: 'purple',
                title: 'Authentication Cloud Service'
            }]
        }

        //event listeners to remove or add tags from tags components
        scope.$on('dimOrObjSelected', (event, data) => {
            if(data.title.includes("+")){
                //expand the options for that type
                scope.rowData2 = originalDataSource.rowData2;
            } else {
                scope.selectedDimOrObj.push(data);
                scope.search.input = '';
                
                if(isDimensionState){
                    scope.rowData3 = scope.rowData3.filter((item) => { return item.title !== data.title});
                }
                
                scope.rowData1 = scope.rowData1.filter((item) => { return item.title !== data.title});                    
                scope.rowData2 = scope.rowData2.filter((item) => { return item.title !== data.title});
            }   
        })

        scope.$on('contentTypeSelected', (event, data) => {
            if(data.includes("+")){
                //expand the options for that type
                scope.rowData3 = originalDataSource.rowData3;
            } else{
                scope.selectedContentTypes.push(data);
                scope.search.input = '';
                scope.rowData3 = scope.rowData3.filter((item) => { return item !== data});
            };

            //keep dropdown open
            scope.showFilteredResults = true;
        })

        scope.$on('removeContentTypeSelected', (event, removeTag) => {
            scope.selectedContentTypes = scope.selectedContentTypes.filter((item) => { return item !== removeTag});
            scope.rowData3.unshift(removeTag);
        });

        scope.$on('removedimOrObjSelected', (event, removeTag) => {
            scope.selectedDimOrObj = scope.selectedDimOrObj.filter((item) =>  { return item.title !== removeTag.title });            
            const isDimension  = originalDataSource.rowData1.find(d => d.title === removeTag.title);
            const isObjects = originalDataSource.rowData2.find(a => a.title === removeTag.title);
            const isContentTypes = originalDataSource.rowData3.find(a => a.title === removeTag.title);

            if(isDimensionState){
                if(isDimension){
                    scope.rowData1.unshift(removeTag);
                } else if(isObjects){
                    scope.rowData2.unshift(removeTag);
                } else if(isContentTypes){
                    scope.rowData3.unshift(removeTag);
                }
            } else if (isDashboard || isSalesFunnel){
                if(isDimension){
                    scope.rowData1.unshift(removeTag);
                } else if(isObjects){
                    scope.rowData2.unshift(removeTag);
                }
            }
        });

        //for search icon active/unactive states
        scope.searchFunnelIconActive = false;
        scope.searchInputActive = () => {
            scope.searchFunnelIconActive = true;
        }

        scope.searchInputUnactive = () => {
            scope.searchFunnelIconActive = false;
        }
    };
}

//map of data to filter type
const dataFilterMap = {
    'dashboard': {
        'rowData1': 'Dimensions',
        'rowData2': 'Objects',
        'rowData3': 'Content Types'
    },
    'object': {
        'rowData1': 'Sales Activity',
        'rowData2': 'Buyer Persona',
        'rowData3': 'Content Types'
    },
    'dimension': {
        'rowData1': 'Market',
        'rowData2': 'Buyer Persona',
        'rowData3': 'Regions'
    }
};

//below are just functions used to transform the data to be shown in the search bar results
const filterResults = (data, query) => {
    const queryLowercase = query.toLowerCase();
    const transformedData = data.map(d => {
        if(d.title){
            if(d.title.toLowerCase().includes(queryLowercase)){
                return d;
            }
        } else if(typeof d === 'string'){
            if(d.toLowerCase().includes(queryLowercase)){
                return d;
            }
        }
    });
    return transformedData.filter(item => !!item);
};

const filtersData = {
    'dashboard': ['Dimensions', 'Objects', 'Content Types'],
    'object': ['Content Types', 'Sales Activity', 'Buyer Persona'], //'File type', 'Business Role'
    'dimension': ['Market', 'Regions', 'Buyer Persona']
};

const dimensionsData = (data) => {
    return data.map(d => {
        return {
            title: d.dimension,
            color: d.color
        };
    });
};

const uniqueObjects = (data) => {
    return data.map(d => {
        const objectsForParticularDimension = d.objects.map(a => a.name);
        return {
            color: d.color,
            objects: Array.from(new Set(objectsForParticularDimension, []))
        };
    });
};

const formattedObjs = (data) => {
    return uniqueObjects(data).map(obj => {
        return obj.objects.map(o => {
            return {
                color: obj.color,
                title: o
            }
        })
    })
};

const objectsData = (data) => formattedObjs(data).reduce((c, n) => c.concat(n));

const formattedContentTypes = (data) => {
    return data.map(d => d.contentTypes);
};

const contentTypesData = (data) => Array.from(new Set(formattedContentTypes(data).reduce((current, next) => { return current.concat(next); }, [])));

const dataSource = (data, type) => {
    if(type === 'dashboard'){
        return {
            filtersData: filtersData[type],
            rowData1: dimensionsData(data),
            rowData2: objectsData(data),
            rowData3: contentTypesData(data)
        }
    } else if (type === 'dimension'){
        const d = data.Products;
        return {
            filtersData: filtersData[type],
            rowData1: d.Market.dimensionTags,
            rowData2: d.BuyerPersona.dimensionTags,
            rowData3: d.Regions.dimensionTags
        }
    } else if (type === 'object'){
        const d = data.AuthenticationCloudService;
        return {
            filtersData: filtersData[type],
            rowData1: d.SalesActivity.objectTags,
            rowData2: d.BuyerPersona.objectTags,
            rowData3: d.ContentTypes.objectTags
        }
    }
};

const truncateData = (type, data, length, calculateRemainingItems) => {
    if(data.length > length){
        const t = data.slice(0,length);
        if(calculateRemainingItems){
            if(type === 'objects'){
                t.push({title: `+${data.length - length}`, color: 'white'});
            } else if (type === 'contentTypes') {
                t.push(`+${data.length - length}`);
            }
        }
        return t;
    } else {
        return data;
    }
};

const currentStateOfFilter = (state) => {
    const currentState = state.current.name;
    if(currentState.includes('dashboard')){
        return 'dashboard';
    } else if (currentState.includes('salesFunnel')){
        return 'object';
    } else if (currentState.includes('dimension')){
        return 'dimension';
    }
}