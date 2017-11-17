const searchBarData = {};
searchBarData.dashboard = [
    {
        dimension: 'Products',
        contentTypes: ['Case Study', 'Blog'],
        color: 'purple',
        objects: [
            {
                name: 'Authenticated Cloud Service',
                properties: ['Region', 'Market'],
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Needs Analysis'
                    }
                ]
            }, {
                name: 'IdentityGuard',
                properties: ['Region', 'Market', 'Personal'],
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Proposal'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Qualification'
                    }
                ]
            }]
    }, {
        dimension: 'Competitors',
        contentTypes: ['Case Study', 'Discovery Question', 'FAQ'],
        color: 'red',
        objects: [
            {
                name: 'Automated Cloud Service',
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Does your platform sclae?',
                        'contentType': 'FAQ',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Does your platform sclae?',
                        'contentType': 'FAQ',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Discovery Question',
                        'salesFunnel': 'Qualification'
                    }
                ]
            }]
    }, {
        dimension: 'Industries',
        contentTypes: ['Data Sheet', 'White Paper', 'Case Study'],
        color: 'gold',
        objects: [
            {
                name: 'Forrita IndentityGuard Mobile',
                properties: ['Region', 'Market'],
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'White Paper',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Data Sheet',
                        'salesFunnel': 'Qualification'
                    }
                ]
            }, {
                name: 'IdentityGuard',
                properties: ['Region'],
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'White Paper',
                        'salesFunnel': 'Qualification'
                    }
                ]
            }]
    }, {
        dimension: 'Partners',
        properties: ['Region', 'Market', 'Personal'],
        contentTypes: ['Blog', 'Case Study'],
        color: 'blue',
        objects: [
            {
                name: 'Forrita TransactionGuard',
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Qualification'
                    }
                ]
            },
            {
                name: 'Forrita GetAccess',
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Qualification'
                    }
                ]
            },
            {
                name: 'Forrita TransactionGuard',
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Qualification'
                    }
                ]
            },
            {
                name: 'Forrita GetAccess',
                assets: [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Case Study',
                        'salesFunnel': 'Qualification'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'contentType': 'Blog',
                        'salesFunnel': 'Qualification'
                    }
                ]
            }]
    }
];

searchBarData.dimensions = {
    Products: {
        'Market': {
            dimensionTags: [
                {color: 'lightBlue', title: 'Mobile'},
                {color: 'lightBlue', title: 'Security'},
                {color: 'lightBlue', title: 'SSL Certificate'}
            ]
        },
        'BuyerPersona': {
            dimensionTags: [
                {color: 'lightBlue', title: 'Technical'},
                {color: 'lightBlue', title: 'Financial'},
                {color: 'lightBlue', title: 'Management'}
            ]
        },
        'Regions': {
            dimensionTags: [
                {color: 'lightBlue', title: 'North America'},
                {color: 'lightBlue', title: 'Western Europe'},
                {color: 'lightBlue', title: 'Middle East'}
            ]
        }
    }
};

searchBarData.objects = {
    'AuthenticationCloudService': {
        'ContentTypes': {
            objectTags: ["Data Sheet", "Blog", "White Paper", "Sales Kit", "+4"] //"FAQ", "Case Study",
        },
        'SalesActivity': {
            objectTags: [
                {color: 'lightBlue', title: 'Prospecting'},
                {color: 'lightBlue', title: 'Qualification'},
                {color: 'lightBlue', title: 'Need Analysis'},
                // {color: 'lightBlue', title: 'Value Propposition'},
                {color: 'white', title: '+2'}
            ]
        }, 
        'BuyerPersona': {
            objectTags: [
                {color: 'lightBlue', title: 'Technical'},
                {color: 'lightBlue', title: 'Financial'},
                {color: 'lightBlue', title: 'Management'}
            ]
        }
    }
};


module.exports = {
    searchBarData
}