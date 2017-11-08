const productSalesFunnelData = [
    {
        name: 'Authentication Cloud Service',
        salesStage: [{
            'name': 'Prospecting',
            'assetsCount': 20,
            'description': 'Data Sheet, Blog, White Paper'
        }, {
            'name': 'Qualification',
            'description': 'FAQ, Discovery Questions, Case Studies'
        }, {
            'name': 'Need Analysis',
            'description': 'Data Sheets, Battle Cards, Case Studies, Discovery Questions'
        }, {
            'name': 'Value Proposition',
            'description': 'Case Studies, Demo Enviroments, Demo Scripts, Sales Decks'
        }, {
            'name': 'Proposal',
            'description': 'Feature Sheets, Proposals, NDA'
        }, {
            'name': 'Negotiation',
            'description': 'Data Sheet, Blog, White Paper'
        }]
    }
];

const productContentTypeData = [
    {
        name: 'Authentication Cloud Service',
        contentTypes: [
            {
                'contentType': 'Case Studies',
                'cardsCount': 5,
                'cards': [
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
                        'companyProperties': 'Authentication Cloud Se… +2'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
                        'companyProperties': 'Authentication Cloud Se… +2'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
                        'companyProperties': 'Authentication Cloud Se… +2'
                    },{
                        'title': 'Mobile Security for First Republic Bank',
                        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
                        'companyProperties': 'Authentication Cloud Se… +2'
                    },
                    {
                        'title': 'Mobile Security for First Republic Bank',
                        'thumbnail': 'assets/images/company/cards-thumbnail-1.png',
                        'companyProperties': 'Authentication Cloud Se… +2'
                    }
                ]
            }, 
            {
                'contentType': 'Data Sheets',
                'cardsCount': 9,
                'cards': [
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'companyProperties': 'IdentityGuard, +5'
                    }
                ]
            },             
            {
                'contentType': 'Battle Cards',
                'cardsCount': 3,
                'cards': [
                    {
                        'title': 'Does your platform scale?',
                        'text': 'Yes. Our product has been proven to scale to thousands of...',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'text': 'Yes. Our product has been proven to scale to thousands of...',
                        'companyProperties': 'IdentityGuard, +5'
                    },
                    {
                        'title': 'Does your platform scale?',
                        'text': 'Yes. Our product has been proven to scale to thousands of...',
                        'companyProperties': 'IdentityGuard, +5'
                    }
                ]
            },
            {
                'contentType': 'Blogs',
                'cardsCount': 6,
                'cards': [
                    {
                        'title': 'Premature Scaling: Why It Kills Startups...',
                        'thumbnail': 'assets/images/company/cards-thumbnail-4.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    }, {
                        'title': 'Types of Paper In Catalog Printing',
                        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    },
                    {
                        'title': 'Premature Scaling: Why It Kills Startups...',
                        'thumbnail': 'assets/images/company/cards-thumbnail-4.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    }, {
                        'title': 'Types of Paper In Catalog Printing',
                        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    },
                    {
                        'title': 'Premature Scaling: Why It Kills Startups...',
                        'thumbnail': 'assets/images/company/cards-thumbnail-4.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    }, {
                        'title': 'Types of Paper In Catalog Printing',
                        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    }
                ]
            },             
            {
                'contentType': 'White Papers',
                'cardsCount': 2,
                'cards': [
                    {
                        'title': 'Types of Paper In Catalog Printing',
                        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    },
                    {
                        'title': 'Types of Paper In Catalog Printing',
                        'thumbnail': 'assets/images/company/cards-thumbnail-5.png',
                        'companyProperties': 'Forrita GetAccess, +4'
                    }
                ]
            }
        ]
    }
];

module.exports = {
    productSalesFunnelData,
    productContentTypeData
};