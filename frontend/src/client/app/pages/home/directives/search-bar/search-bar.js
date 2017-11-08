import SearchBarTemplate from './search-bar.html';

/* @ngInject*/
export default class SearchBar {
    constructor () {
        this.template = SearchBarTemplate;
        this.restrict = 'E';
        this.scope = {
            data: '='
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

        const originalDataSource = dataSource(scope.data);
        const tempData = _.cloneDeep(originalDataSource);

        const calculateScopeData = (tempData, calculateRemainingItems) => {
            scope.differentFilters = tempData.filtersData;
            scope.dimensions = tempData.dimensionsData;
            scope.objects = truncateData('objects', tempData.objectsData, 2, calculateRemainingItems);
            scope.contentTypes = truncateData('contentTypes', tempData.contentTypesData, 4, calculateRemainingItems);
        };

        calculateScopeData(tempData, true);

        scope.showSearchBar = () => { scope.showFilteredResults = true; };
        scope.hideSearchBar = () => { scope.showFilteredResults = false; };
        scope.showFilteredResults = false;
 
        scope.updateFilters = () => {
            const searchQuery = scope.search.input;
            const allData = _.cloneDeep(originalDataSource);

            if(scope.search.input){
                const filteredData = {
                    dimensionsData: filterResults(allData.dimensionsData, searchQuery),
                    objectsData: filterResults(allData.objectsData, searchQuery),
                    contentTypesData: filterResults(allData.contentTypesData, searchQuery)
                };
                //find all filter results with empty arrays
                const validFilters = ['dimensionsData', 'objectsData', 'contentTypesData'].filter(type => {
                    return filteredData[type].length !== 0;
                });
                filteredData.filtersData = validFilters.map(dataType => dataFilterMap[dataType]);

                calculateScopeData(filteredData, true);
            } else {
                calculateScopeData(allData, true);
            }
        };
    };
}

//map of data to filter type
const dataFilterMap = {
    'dimensionsData': 'Dimensions',
    'objectsData': 'Objects',
    'contentTypesData': 'Content Types'
};

//below are just functions used to transform the data to be shown in the search bar results
const filterResults = (data, query) => {
    const transformedData = data.map(d => {
        if(d.title){
            if(d.title.toLowerCase().includes(query)){
                return d;
            }
        } else if(typeof d === 'string'){
            if(d.toLowerCase().includes(query)){
                return d;
            }
        }
    });
    return transformedData.filter(item => !!item);
};

const filtersData = ['Dimensions', 'Objects', 'Content Types'];
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

const dataSource = (data) => {
    return {
        filtersData,
        dimensionsData: dimensionsData(data),
        objectsData: objectsData(data),
        contentTypesData: contentTypesData(data)
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