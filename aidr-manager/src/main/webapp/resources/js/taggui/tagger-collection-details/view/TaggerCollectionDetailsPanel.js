Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.tagger-collection-details.view.TaggerCollectionDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.tagger-collection-details-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;Details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Tagger for "' + CRISIS_NAME + '"',
            flex: 1
        });

        this.classifiersTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Classifiers',
            margin: '17 0 5 0',
            flex: 1
        });

        this.gotoCollectorButton = Ext.create('Ext.Button', {
            text: 'Go To Collector',
            cls:'btn btn-blue',
            id: 'goToCollector',
            width: 150,
            margin: '0 0 0 0'
        });

        this.addNewClassifierButton = Ext.create('Ext.Button', {
            text: 'Add a new classifier',
            cls:'btn btn-blue',
            id: 'addNewClassifier',
            width: 150,
            margin: '27 0 0 0'
        });

        this.publicLink = Ext.create('Ext.container.Container', {
            html: 'Public link for volunteers (in MicroMappers):',
            margin: '6 15 0 0'
        });

        this.socialIcons = Ext.create('Ext.container.Container', {
            flex: 1,
            layout: 'hbox',
            defaults: {
                margin: '0 5 0 0'
            },
            items: [
                {
                    xtype: 'image',
                    src: '/AIDRFetchManager/resources/img/icons/twitter-icon.png'
                },{
                    xtype: 'image',
                    src: '/AIDRFetchManager/resources/img/icons/facebook-icon.png'
                },{
                    xtype: 'image',
                    src: '/AIDRFetchManager/resources/img/icons/google-icon.png'
                },{
                    xtype: 'image',
                    src: '/AIDRFetchManager/resources/img/icons/pinterest-icon.png'
                }
            ]
        });

        this.pyBossaLink = Ext.create('Ext.form.Label', {
            html: '<div class="gray-backgrpund"><i>Initializing crowdsourcing task. Please come back in a few minutes.</i></div>',
            margin: '5 0 5 0'
        });

        this.aucHint = Ext.create('Ext.container.Container', {
            html: '<span class="redInfo">*</span>If AUC is lower than 0.8-0.9, or AUC is 1.0, you urgently need more training examples.',
            margin: 0
        });

        this.crisisTypesStore = Ext.create('Ext.data.Store', {
            pageSize: 30,
            storeId: 'crisisTypesStore',
            fields: ['crisisTypeID', 'name'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getAllCrisisTypes.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (s) {
                    me.crysisTypesCombo.setValue(CRISIS_TYPE_ID);
                }
            }
        });

        this.crisisModelsStore = Ext.create('Ext.data.Store', {
            pageSize: 30,
            storeId: 'crisisModelsStore',
            fields: ['attribute', 'auc', 'classifiedDocuments', 'modelID', 'status', 'trainingExamples', 'modelFamilyID'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getModelsForCrisis.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    s.getProxy().extraParams = {
                        id: CRISIS_ID
                    }
                }
            }
        });

        this.crisisModelsTpl = new Ext.XTemplate(
            '<table width="100%" class="crisis-table">',
            '<tpl for=".">',

            '<tr class="crisis-item">',

            '<td class="img">',
            '<img alt="Collection History image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70">',
            '</td>',

            '<td class="content"><table><tr>',


            '<td class="styled-text-17">Name:</td>',
            '<td class="styled-text-17">{[this.getModelName(values.modelID, values.attribute)]}</td></tr>',


            '<tr><td>Status:</td>',
            '<td>{[this.getStatus(values.modelID)]}</td></tr>',

            '<tr><td>Training examples:</td>',
            '<td>{[this.getNumber(values.trainingExamples)]} &mdash; <a href="' + BASE_URL +  '/protected/'
                + CRISIS_CODE + '/{modelID}/{modelFamilyID}/training-data">Manage training examples &raquo;</a></td></tr>',


            '<tr><td>Classified elements:</td>',
            '<td>{[this.getNumber(values.classifiedDocuments)]} (since last change of the classifier)</td></tr>',

            '<tr><td>Quality (AUC)<span class="redInfo">*</span>:</td>',
            '<td>{[AIDRFMFunctions.getAucNumberWithColors(values.auc)]}</td>',

            '</td></tr></table></td>',

            '</tr>',


            '<tr><td colspan="2">',
            '<div class="horizontalLine"></div>',
            '</td></tr>',

            '</tpl>',
            '</table>',
            {
                getNumber: function (r) {
                    return r ? r.format() : 0;
                },
                getModelName: function (modelId, modelName) {
                    if (modelId && modelId != 0) {
                        return '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/' + modelId + '/model-details">' + modelName + '</a>';
                    } else {
                        return modelName;
                    }
                },
                getStatus: function (modelId) {
                    if (modelId && modelId != 0) {
                        return 'Running';
                    } else {
                        return 'Waiting training examples';
                    }
                }
            }
        );

        this.crisisModelsView = Ext.create('Ext.view.View', {
            store: this.crisisModelsStore,
            tpl: this.crisisModelsTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.crysisTypesCombo = Ext.create('Ext.form.ComboBox', {
            hideLabel: true,
            store: this.crisisTypesStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'crisisTypeID',
            width: 280,
            listeners: {
                change: function(combo, newValue, oldValue, eOpts) {
                    if (newValue == CRISIS_TYPE_ID) {
                        me.saveButton.hide();
                    } else {
                        me.saveButton.show();
                    }
                }
            }
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'crisisSave',
            hidden: true
        });

        this.deleteButton = Ext.create('Ext.Button', {
            text: 'Delete',
            cls:'btn btn-red',
            id: 'crisisDelete',
            margin: '0 0 0 7'
        });

        this.detailsBlock = Ext.create('Ext.container.Container', {
            flex: 1,
            layout: 'vbox',
            items: [
                {
                    xtype: 'container',
                    defaultType: 'label',
                    padding: '0 10',
                    flex: 1,
                    layout: 'vbox',
                    defaults: {
                        margin: '5 0'
                    },
                    height: 120,
                    items: [
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Code:'
                                },
                                {
                                    text: CRISIS_CODE
                                }
                            ]
                        },{
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Name:'
                                },
                                {
                                    text: CRISIS_NAME
                                }
                            ]
                        },{
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Type:'
                                },
                                this.crysisTypesCombo
                            ]
                        },{
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'container',
                                    layout: 'hbox',
                                    margin: '0 0 0 75',
                                    width: 290,
                                    items: [
                                        this.saveButton
                                    ]
                                },
                                this.deleteButton
                            ]
                        }
                    ]
                },
                {
                    xtype: 'container',
                    width: '100%',
                    html: '<div class="horizontalLine"></div>'
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

        this.downloadsBlock = Ext.create('Ext.container.Container', {
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
        });

//        this.feedsBlock = Ext.create('Ext.container.Container', {
//            flex: 1,
//            layout: 'vbox',
//            items: [
//                {
//                    xtype: 'container',
//                    defaultType: 'label',
//                    padding: '0 10',
//                    flex: 1,
//                    layout: 'vbox',
//                    defaults: {
//                        margin: '5 0'
//                    },
//                    height: 100,
//                    items: [
//                        {
//                            xtype: 'container',
//                            defaultType: 'label',
//                            layout: 'hbox',
//                            items: [
//                                {
//                                    width: 75,
//                                    html: '<b>Data feed</b>'
//                                },
//                                {
//                                    html: '<b>URL</b>'
//                                }
//                            ]
//                        },{
//                            xtype: 'container',
//                            defaultType: 'label',
//                            layout: 'hbox',
//                            items: [
//                                {
//                                    width: 75,
//                                    text: 'Tweet-ids:'
//                                },
//                                {
//                                    html: '<a href="http://aidr.qcri.org/predict/public/"' + CRISIS_CODE + '>http://aidr.qcri.org/predict/public/' + CRISIS_CODE + '</a>'
//                                }
//                            ]
//                        },{
//                            xtype: 'container',
//                            defaultType: 'label',
//                            layout: 'hbox',
//                            items: [
//                                {
//                                    width: 75,
//                                    text: 'Full:'
//                                },
//                                {
//                                    html: '<a href="http://aidr.qcri.org/predict/protected/"' + CRISIS_CODE + '>http://aidr.qcri.org/predict/protected/' + CRISIS_CODE + '</a><br>'
//                                }
//                            ]
//                        },{
//                            xtype: 'container',
//                            defaultType: 'label',
//                            layout: 'hbox',
//                            items: [
//                                {
//                                    width: 75,
//                                    text: ''
//                                },
//                                {
//                                    html: 'User: <span class="na-text">N/A</span> Pass: <span class="na-text">N/A</span>'
//                                }
//                            ]
//                        }
//                    ]
//                }
//            ]
//        });

        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    title: 'Details',
                    items: [
                        {
                            xtype: 'container',
                            flex: 1,
                            layout: 'hbox',
                            margin: '25 0 5 0',
                            items: [
                                this.publicLink,
                                this.socialIcons,
                                this.gotoCollectorButton
                            ]
                        },
                        this.pyBossaLink,
                        {
                            xtype: 'container',
                            margin: '15 0 0 0',
                            html: '<div class="horizontalLine"></div>'
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                this.classifiersTitle,
                                this.addNewClassifierButton
                            ]
                        },
                        this.crisisModelsView,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            padding: '15 0 0 0',
                            items: [
                                {
                                    xtype: 'container',
                                    flex: 1
                                },
                                this.aucHint
                            ]
                        }
                    ]
                },
                {
                    title: 'Edit',
                    padding: '10 0 0 0',
                    defaults: {
                        anchor: '100%'
                    },
                    items: [
                        this.detailsBlock
                    ]
                },
                {
                    title: 'Download/Export',
                    padding: '10 0 0 0',
                    items: [
//                        this.feedsBlock
                        this.downloadsBlock
                    ]
                }
            ]
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 5 0',
                html: '<div class="horizontalLine"></div>'
            },
            {
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    this.taggerTitle
                ]
            },
            this.tabPanel
        ];

        this.callParent(arguments);
    }

});