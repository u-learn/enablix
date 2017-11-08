const cardsData = [
    {
        'id': 1,
        'title': 'Mobile Security for First Republic Bank',
        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
        'contentType': 'Case Study',
        'companyProperties': 'Authentication Cloud Se… +2'
    }, {
        'id': 2,
        'title': 'Does your platform scale?',
        'text': 'Yes. Our product has been proven to scale to thousands of...',
        'contentType': 'FAQ',
        'companyProperties': 'IdentityGuard, +5'
    }, {
        'id': 3,
        'title': 'Identity Guard Standard Battle Card',
        'thumbnail': 'assets/images/company/cards-thumbnail-3.png',
        'contentType': 'Battle Card',
        'companyProperties': 'Forrita TransactionGuard, +3'
    }, {
        'id': 4,
        'title': 'Premature Scaling: Why It Kills Startups...',
        'thumbnail': 'assets/images/company/cards-thumbnail-4.png',
        'contentType': 'Blog',
        'companyProperties': 'Forrita GetAccess, +4'
    }, {
        'id': 5,
        'title': 'Types of Paper In Catalog Printing',
        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
        'contentType': 'Sales Kit',
        'companyProperties': 'Forrita GetAccess, +4'
    },
    {
        'id': 1,
        'title': 'Mobile Security for First Republic Bank',
        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
        'contentType': 'Case Study',
        'companyProperties': 'Authentication Cloud Se… +2'
    }, {
        'id': 2,
        'title': 'Does your platform scale?',
        'text': 'Yes. Our product has been proven to scale to thousands of...',
        'contentType': 'FAQ',
        'companyProperties': 'IdentityGuard, +5'
    }, {
        'id': 3,
        'title': 'Identity Guard Standard Battle Card',
        'thumbnail': 'assets/images/company/cards-thumbnail-3.png',
        'contentType': 'Battle Card',
        'companyProperties': 'Forrita TransactionGuard, +3'
    }
];

const dimensionData = {
    'Products': {
        color: 'purple',
        cards: [
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'Forrita IdentityGuard Mobile',
                companyProperties: ['Region', 'Market', 'Personal', 'A', 'B', 'C']
                
            }, {
                title: 'Authentication Cloud Service',
                companyProperties: ['Region', 'Market', 'Personal', 'A', 'B', 'C', 'D', 'E', 'F']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'Forrita IdentityGuard Mobile',
                companyProperties: ['Region', 'Market', 'Personal', 'A', 'B', 'C']
                
            }, {
                title: 'Authentication Cloud Service',
                companyProperties: ['Region', 'Market', 'Personal', 'A', 'B', 'C', 'D', 'E', 'F']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }, {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }
        ]
    },
    'Competitors': {
        color: 'red',
        cards: [
            {
                title: 'Forrita TransactionGuard',
                companyProperties: ['Region', 'Market', 'Personal', 1, 2, 3, 4]
            },
            {
                title: 'Forrita GetAccess',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'Forrita TransactionGuard',
                companyProperties: ['Region', 'Market', 'Personal', 1, 2, 3, 4, 5]
            },
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },{
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market', 'Personal']
            }
        ]
    },
    'Industries': {
        color: 'gold',
        cards: [
            {
                title: 'IdentityGuard',
                companyProperties: ['Region', 'Market']
            },
            {
                title: 'Forrita IdentityGuard Mobile',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'Authentication Cloud Service',
                companyProperties: ['Region', 'Market', 'Personal', 1, 2, 3, 4, 5]
            }
        ]
    },
    'Partners': {
        color: 'blue',
        cards: [
            {
                title: 'Forrita TransactionGuard',
                companyProperties: ['Region', 'Market', 'Personal', 1, 2, 3, 4]
            },
            {
                title: 'Forrita GetAccess',
                companyProperties: ['Region', 'Market', 'Personal']
            },
            {
                title: 'Forrita TransactionGuard',
                companyProperties: ['Region', 'Market', 'Personal', 1, 2, 3, 4, 5]
            },
            {
                title: 'Forrita GetAccess',
                companyProperties: ['Region', 'Market', 'Personal']
            }
        ]
    }
};

module.exports = {
    cardsData,
    dimensionData
};