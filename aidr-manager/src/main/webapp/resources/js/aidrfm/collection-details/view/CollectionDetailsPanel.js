Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout'
]);

Ext.define('AIDRFM.collection-details.view.CollectionDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.collection-details-view',

    fieldDefaults: {
        labelAlign: 'left',
        msgTarget: 'side'
    },

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="' + BASE_URL + '/protected/home">Home</a></div>',
            margin: 0,
            padding: 0
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            margin: '5 0 0 0',
            html: '<div class="horisontalLine"></div>'
        });

        this.collectionTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1',
            margin: '10 0 15 0',
            padding: 0,
            text: '',
            flex: 1
        });

        this.refreshButton = Ext.create('Ext.Button', {
            text: null,
            height: 32,
            width: 32,
            margin: '13 0 0 0',
            tooltip: 'Refresh',
            iconCls: 'refrashIcon',
            id: 'refreshBtn'
        });

        this.collectionHistoryTitle = Ext.create('Ext.form.Label', {
            hidden: true,
            padding: '10 0 0 0',
            cls: 'header-h1',
            text: 'Collection History'
        });

        this.statusL = Ext.create('Ext.form.Label', {padding: '0 10 0 0'});
        this.lastStartedL = Ext.create('Ext.form.Label', {flex: 1});
        this.lastStoppedL = Ext.create('Ext.form.Label', {flex: 1});
        this.codeL = Ext.create('Ext.form.Label', {flex: 1});
        this.keywordsL = Ext.create('Ext.form.Label', {
            flex: 1,
            cls: 'word-wrap-class'
        });
        this.geoL = Ext.create('Ext.form.Label', {flex: 1});
        this.followL = Ext.create('Ext.form.Label', {flex: 1});
        this.languageFiltersL = Ext.create('Ext.form.Label', {flex: 1});
        this.createdL = Ext.create('Ext.form.Label', {flex: 1});
        this.docCountL = Ext.create('Ext.form.Label', {flex: 1});
        this.totalDocCountL = Ext.create('Ext.form.Label', {flex: 1});
        this.lastDocL = Ext.create('Ext.form.Label', {flex: 1, cls:'tweet'});

        this.geoContainer = Ext.create('Ext.container.Container', {
            hidden: true,
            defaultType: 'label',
            layout: 'hbox',
            items: [
                {
                    width: 220,
                    text: 'Geographical regions:'
                },
                this.geoL
            ]
        });

        this.followContainer = Ext.create('Ext.container.Container', {
            hidden: true,
            xtype: 'container',
            defaultType: 'label',
            layout: 'hbox',
            items: [
                {
                    width: 220,
                    text: 'Follow specific users:'
                },
                this.followL
            ]
        });

        this.timeDurationL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Time period',
            padding: '15 0 0 0',
            cls: 'header-h2'
        });

        this.configurationsL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Configuration',
            padding: '15 0 0 0',
            cls: 'header-h2'
        });

        this.codeE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Code',
            name: 'code',
            allowBlank: false,
            width: 370,
            emptyText: 'e.g., Sandy2012 or EQJapan2011',
            maxLength: 64,
            maxLengthText: 'The maximum length for this field is 64',
            maskRe: /[^ ]/,
            disabled: true
        });

        this.nameE = Ext.create('Ext.form.field.Text', {
            width: 370,
            fieldLabel: 'Name',
            name: 'name',
            allowBlank: false,
            emptyText: 'e.g., Hurricane Sandy'
        });

        this.keywordsE = Ext.create('Ext.form.field.TextArea', {
            fieldLabel: 'Keywords',
            name: 'track',
            allowBlank: false,
            maxLength: 400,
            maxLengthText: 'The maximum length for this field is 400',
            flex: 1,
            rows: 4,
            emptyText: 'e.g., #sandy, #newyork,#joplin (max 400)'
        });

        this.geoE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Geographical regions',
            labelWidth: 130,
            name: 'geo',
            flex: 1,
            emptyText: 'e.g., 43.43, 22.44, 89.32, 56.43 (max 25)'
        });

        this.geoDescription = Ext.create('Ext.form.Label', {
            flex: 1,
            html: '<span class="redInfo">*</span> <a href="http://boundingbox.klokantech.com/">boundingbox.klokantech.com</a> ("Copy/paste CSV format of a boundingbox")',
            padding: '2 0 2 135'
        });

        this.followE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Follow specific users',
            name: 'follow',
            labelWidth: 130,
            flex: 1,
            emptyText: 'e.g., 47423744, 53324456 (max 5000)'
        });

        this.langComboStore = Ext.create('Ext.data.ArrayStore', {
            autoDestroy: true,
            storeId: 'langComboStore',
            idIndex: 0,
            fields: [
                'name',
                'code'
            ],
            data: LANG
        });

        this.langCombo = Ext.create('Ext.form.ComboBox', {
            store: this.langComboStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'code',
            multiSelect: true,
            fieldLabel: 'Language(s)',
            labelWidth: 100,
            name: 'langFilters',
            flex: 1,
            emptyText: 'e.g., en, ar, ja',
            tpl: '<tpl for=".">' +
                '<li role="option" class="x-boundlist-item">{name}</li>' +
                '<tpl if="xindex == 9"><hr/></tpl>' +
                '<tpl if="xindex == 56"><hr/>Other ...<hr/></tpl>' +
                '</tpl>'
        });
 
        this.startButton = Ext.create('Ext.Button', {
            text: 'Start',
            margin: 0,
            cls:'btn btn-green',
            id: 'collectionStart',
            hidden: true
        });

        this.stopButton = Ext.create('Ext.Button', {
            text: 'Stop',
            cls:'btn btn-red',
            id: 'collectionStop',
            margin: '0 0 0 10',
            hidden: true
        });

        this.configurationsEditTabL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Optional configuration',
            padding: '15 0 0 0',
            cls: 'header-h2'
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            margin: '0 10 0 10',
            cls:'btn btn-green',
            id: 'collectionUpdate'
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            cls:'btn btn-red',
            id: 'collectionEditCancel'
        });

        this.enableTaggerButton = Ext.create('Ext.Button', {
            text: 'Enable Tagger',
            cls:'btn btn-blue',
            id: 'enableTagger',
            margin: '0 0 0 10',
            disabled: true
        });

        this.gotoTaggerButton = Ext.create('Ext.Button', {
            text: 'Go To Tagger',
            cls:'btn btn-blue',
            id: 'goToTagger',
            margin: '0 0 0 10',
            hidden: true
        });

        this.crisesTypeStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'crisesTypeStore',
            fields: ['crisisTypeID', 'name'],
            proxy: {
                type: 'ajax',
                url: '',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: false
        });

        this.collectionLogStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'collectionLogStore',
            fields: ['id', 'collectionID', 'langFilters', 'startDate', 'endDate', 'count', 'track', 'geo', 'follow'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/collection-log/findAllForCollection.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    var id = me.currentCollectionId;
                    s.getProxy().extraParams = {
                        id: id
                    }
                },
                load: function (s) {
                    var count = s.getCount();
                    if (count > 0) {
                        me.collectionHistoryTitle.show();
                        me.collectionLogView.show();
                        me.collectionLogPaging.show();
                    } else {
                        me.collectionHistoryTitle.hide();
                        me.collectionLogView.hide();
                        me.collectionLogPaging.hide();
                    }
                }
            }
        });

        this.crisesTypeTpl = new Ext.XTemplate(
            '<div class="popup choose-crises">' +

            '<tpl if="xindex == 1">' +
                '<h2>Choose Crisis Type</h2>' +
                '<div class="crises-list">' +
                '<ul>' +
            '</tpl>' +
            '<tpl for=".">',

                '<li class="crisesItem"><a>{name}</a></li>' +

            '</tpl>',
            '<tpl if="xindex == 1">' +
                '</ul>' +
                '</div>' +
            '</tpl>' +

            '</div>'
        );

        this.crisesTypeView = Ext.create('Ext.view.View', {
            store: this.crisesTypeStore,
            id: 'crisesTypeViewId',
            tpl: this.crisesTypeTpl,
            itemSelector: 'li.crisesItem'
        });

        this.crisesTypeWin = Ext.create('Ext.window.Window', {
            bodyStyle: 'background:none; background-color: white;',
            height: 400,
            width: 700,
            layout: 'fit',
            id: 'crisesTypeWin',
            closeAction: 'hide',
            items: [
                this.crisesTypeView
            ]
        });

        this.collectionLogTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<div class="img">',
            '<img alt="Collection History image" height="70" src="/AIDRFetchManager/resources/img/twitter_icon2.png" width="70">',
            '</div>',

            '<div class="content">',

            '<div class="rightColumn">',
            '<div>Downloaded items:</div>',
            '<div>Start date:</div>',
            '<div>End date:</div>',
            '{[this.showGeoLabel(values.geo)]}',
            '{[this.showFollowLabel(values.follow)]}',
            '<div>Language(s):</div>',
            '<div>Keywords:</div>',
            '</div>',

            '<div class="leftColumn">',
            '<div>{[this.getDocNumber(values.count)]}</div>',
            '<div>{[this.getField(values.startDate)]}</div>',
            '<div>{[this.getField(values.endDate)]}</div>',
            '{[this.showValue(values.geo)]}',
            '{[this.showValue(values.follow)]}',
            '<div>{[this.getField(values.langFilters)]}</div>',
            '<div class="word-wrap-class">{[this.getField(values.track)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getField: function (r) {
                    return r ? r : "<span class='na-text'>Not specified</span>";
                },
                getDocNumber: function (r) {
                    return r ? Ext.util.Format.number(r,'0,000') : 0;
                },
                showGeoLabel: function (r) {
                    return r ? '<div>Geographical regions:</div>' : '';
                },
                showValue: function (r) {
                    return r ? '<div>' + r + '</div>' : '';
                },
                showFollowLabel: function (r) {
                    return r ? '<div>Follow specific users:</div>' : '';
                }
            }
        );

        this.collectionLogView = Ext.create('Ext.view.View', {
            hidden: true,
            store: this.collectionLogStore,
            tpl: this.collectionLogTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.collectionLogPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'collectionLogStore',
            displayInfo:true,
            hidden: true,
            displayMsg:'Collection history records {0} - {1} of {2}',
            emptyMsg:'No collection history records to display'
        });

        this.editForm = {
            xtype: 'form',
            border: false,
            id: 'collectionForm',
            items: [
                {
                    xtype: 'container',
                    defaults: {
                        width: 350,
                        labelWidth: 120
                    },
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.nameE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionNameInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.codeE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionCodeInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0 0 0',
                            padding: '0 0 8 0',
                            //cls: 'bordered-bottom',
                            items: [ 
                                this.keywordsE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionkeywordsInfo'
                                }
                            ]
                        },{
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0 0 0',
                            padding: '0 0 20 0',
                            cls: 'bordered-bottom',
                            items: [
                                this.langCombo,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionLangInfo'
                                }
                            ]
                        },
                        
                        this.configurationsEditTabL,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '0 0 5 0',
                            items: [
                                this.geoE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionGeoInfo'
                                }
                            ]
                        },
                        this.geoDescription,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.followE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionFollowInfo'
                                }
                            ]
                        },
                        
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            padding: '15 0 0 0',
                            items: [
                                this.saveButton,
                                this.cancelButton
                            ]
                        }
                    ]
                }
            ]
        };

        this.detailsPanel = Ext.create('Ext.container.Container', {
            defaults: {
                margin: '5 0'
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'container',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    defaults: {
                        margin: '5 0'
                    },
                    minHeight: 300,
                    items: [
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            padding: '10 0',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'container',
                                    padding: '0 20 0 0',
                                    html: '<img src="/AIDRFetchManager/resources/img/collection-icon.png"/>'
                                },
                                {
                                    xtype: 'container',
                                    flex: 1,
                                    defaultType: 'label',
                                    layout: 'vbox',
                                    defaults: {
                                        margin: '5 0'
                                    },
                                    items: [
                                        {
                                            xtype: 'container',
                                            defaultType: 'label',
                                            layout: 'hbox',
                                            items: [
                                                {
                                                    padding: '0 10 0 0',
                                                    text: 'Code:'
                                                },
                                                this.codeL
                                            ]
                                        },
                                        {
                                            xtype: 'container',
                                            defaultType: 'label',
                                            layout: 'hbox',
                                            items: [
                                                this.statusL
                                            ]
                                        }
                                    ]
                                },
                                {
                                    xtype: 'container',
                                    defaultType: 'label',
                                    padding: '22 0 0 0',
                                    layout: 'hbox',
                                    items: [
                                        this.startButton,
                                        this.stopButton,
                                        this.enableTaggerButton,
                                        this.gotoTaggerButton
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    html: 'Downloaded items <br/> (since last re-start):'
                                },
                                this.docCountL
                            ]
                        },
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Total downloaded items:'
                                },
                                this.totalDocCountL
                            ]
                        },
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            cls: 'bordered-bottom',
                            items: [
                                {
                                    width: 220,
                                    text: 'Last downloaded item:'
                                },
                                this.lastDocL
                            ]
                        },
                        this.timeDurationL,
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Created on:'
                                },
                                this.createdL
                            ]
                        },
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Last started on:'
                                },
                                this.lastStartedL
                            ]
                        },
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            cls: 'bordered-bottom',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Last stopped on:'
                                },
                                this.lastStoppedL
                            ]
                        },
                        this.configurationsL,
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Keywords:'
                                },
                                this.keywordsL
                            ]
                        },
                        this.geoContainer,
                        this.followContainer,
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            cls: 'bordered-bottom',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 220,
                                    text: 'Language(s):'
                                },
                                this.languageFiltersL
                            ]
                        }

                    ]
                }
            ]
        });

        this.CSVLink = Ext.create('Ext.form.Label', {
            flex: 1,
            padding: '5 5 5 5',
            html: ''
        });
        this.tweetsIdsLink = Ext.create('Ext.form.Label', {
            flex: 1,
            margin: '5 5 5 5',
            html: ''
        });

        this.generateCSVButton = Ext.create('Ext.Button', {
            text: 'Export tweets (.csv) (Last 100k tweets)',
            margin: 5,
            cls:'btn btn-blue download-button',
            id: 'generateCSVLink'
        });

        this.generateTweetIdsButton = Ext.create('Ext.Button', {
            text: 'Export tweet-ids only (.csv) (All tweets)',
            margin: 5,
            cls:'btn btn-blue download-button',
            id: 'generateTweetIdsLink'
        });
        
        this.downloadText = Ext.create('Ext.form.Label', {
            flex: 1,
            html: ''
        });

        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    title: 'Details',
                    items: [
                        this.detailsPanel
                    ]
                },
                {
                    title: 'Edit',
                    padding: '10 0 0 0',
                    defaults: {
                        anchor: '100%'
                    },
                    items: [
                        this.editForm
                    ]
                },
                {
                    title: 'Download',
                    padding: '10 0 0 0',
                    items: [
                        {
                            xtype: 'container',
                            layout: 'vbox',
                            items: [
                                {
                                    xtype: 'container',
                                    padding: '15 0 0 0',
                                    defaultType: 'label',
                                    layout: 'hbox',
                                    items: [
                                        this.generateCSVButton,
                                        this.CSVLink
                                    ]
                                },
                                {
                                    xtype: 'container',
                                    defaultType: 'label',
                                    layout: 'hbox',
                                    items: [
                                        this.generateTweetIdsButton,
                                        this.tweetsIdsLink
                                    ]
                                },
                                this.downloadText
                            ]
                        }
                    ]
                }
            ],
            listeners: {
                'tabchange': function (tabPanel, tab) {
                    var tabIndex = tabPanel.items.findIndex('id', tab.id);
                    if (tabIndex == 0){
                        var collectionHistoryCount = me.collectionLogStore.count();
                        if (collectionHistoryCount > 0) {
                            me.collectionHistoryTitle.show();
                            me.collectionLogView.show();
                            me.collectionLogPaging.show();
                        }
                    } else {
                        me.collectionHistoryTitle.hide();
                        me.collectionLogView.hide();
                        me.collectionLogPaging.hide();
                    }
                }
            }
        });

        this.items = [
            this.breadcrumbs,
            this.horisontalLine,
            {
                xtype: 'container',
                layout: 'hbox',
                padding: '10 0',
                items: [
                    this.collectionTitle,
                    this.refreshButton
                ]
            },
            this.tabPanel,
            this.collectionHistoryTitle,
            this.collectionLogView,
            this.collectionLogPaging
        ];

        this.callParent(arguments);
    }

})