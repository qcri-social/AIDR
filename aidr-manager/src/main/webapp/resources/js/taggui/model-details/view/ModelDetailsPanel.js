Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer',
    'TAGGUI.attribute-details.view.AttributeDetailsMain'
]);

Ext.define('TAGGUI.model-details.view.ModelDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.model-details-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">My Classifiers</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;Classifier details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Classifier "' + MODEL_NAME + '"',
            flex: 1
        });

        this.modelDetails = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            html: 'Machine has tagged <b>0</b> item.&nbsp;<a href="' + BASE_URL +  '/protected/'
                + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID + '/' + ATTRIBUTE_ID +
                + '/training-data">Go to human-tagged items &raquo;</a>',
            flex: 1
        });

        this.aucHint = Ext.create('Ext.container.Container', {
            html: '<span class="redInfo">*</span>If AUC is lower than 0.8-0.9, or AUC is 1.0, you urgently need more human tagged items.'
        });

        this.modelLabelsStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'modelLabelsStore',
            fields: ['value', 'classifiedDocumentCount', 'labelAuc', 'labelPrecision', 'labelRecall', 'trainingDocumentsCount'],
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

        this.modelLabelsTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<div class="img">',
            '<tpl if="xindex != xcount">' +
                '<img alt="Collection History image" src="/AIDRFetchManager/resources/img/AIDR/tag.png" width="70" height="79">',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div class="no-image"></div>',
            '</tpl>',
            '</div>',

            '<div class="content">',

            '<div class="rightColumn">',
            '<tpl if="xindex != xcount">' +
                '<div class="styled-text-17">Classifier:</div>',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div class="styled-text-17">Summary:</div>',
            '</tpl>',
            
            '<div>Human-tagged items:</div>',
            '<div>Machine-tagged items:</div>',
            '<div>Precision:</div>',
            '<div>Recall:</div>',
            '<tpl if="xindex != xcount">' +
                '<div>AUC:</div>',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div>Quality (AUC)<span class="redInfo">*</span>:</div>',
            '</tpl>',
            '</div>',

            '<div class="leftColumn">',

            '<tpl if="xindex != xcount">' +
                '<div class="styled-text-17">{[this.getField(values.value)]}</div>',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div class="styled-text-17">&nbsp;</div>',
            '</tpl>',

            '<div>{[this.getNumber(values.trainingDocumentsCount)]}</div>',
            '<div>{[this.getNumber(values.classifiedDocumentCount)]}</div>',
            '<div>{[this.getNumber(values.labelPrecision)]}</div>',
            '<div>{[this.getNumber(values.labelRecall)]}</div>',
            '<div>{[this.getNumber(values.labelAuc)]}</div>',

            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getField: function (r) {
                    return r ? r : "<span class='na-text'>Not specified</span>";
                },
                getNumber: function (r) {
                    return r ? r : 0;
                }
            }
        );

        this.modelLabelsView = Ext.create('Ext.view.View', {
            store: this.modelLabelsStore,
            tpl: this.modelLabelsTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.modelHistoryTitle = Ext.create('Ext.form.Label', {
            hidden: true,
            margin: '5 0',
            cls: 'header-h1',
            text: 'Classifier History'
        });

        this.modelHistoryStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'modelHistoryStore',
            fields: ['modelID', 'avgPrecision', 'avgRecall', 'avgAuc', 'trainingCount', 'trainingTime'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/modelHistory.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    s.getProxy().extraParams = {
                        id: MODEL_FAMILY_ID
                    }
                },
                load: function (s) {
                    var count = s.getCount();

                    if (count > 0 ) {
                        me.modelHistoryTitle.show();
                        me.modelHistoryView.show();
                        me.modelHistoryPaging.show();
                    } else {
                        me.modelHistoryTitle.hide();
                        me.modelHistoryView.hide();
                        me.modelHistoryPaging.hide();
                    }
                }
            }
        });

        this.modelHistoryTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<div class="img">',
            '<img alt="Collection History image" src="/AIDRFetchManager/resources/img/AIDR/tag.png" width="70" height="79">',
            '</div>',

            '<div class="content">',

            '<div class="rightColumn">',
            '<div>Precision:</div>',
            '<div>Recall:</div>',
            '<div>AUC:</div>',
            '<div>Human-tagged items:</div>',
            '<div>Date/Time:</div>',
            '</div>',

            '<div class="leftColumn">',
            '<div>{[this.getDocNumber(values.avgPrecision)]}</div>',
            '<div>{[this.getDocNumber(values.avgRecall)]}</div>',
            '<div>{[this.getDocNumber(values.avgAuc)]}</div>',
            '<div>{[this.getTotal(values.trainingCount)]}</div>',
            '<div>{[this.getDateField(values.trainingTime)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getDateField: function (r) {
                   // var s = new Date(r);
                    return r ? moment(r).calendar() : "<span class='na-text'>Not specified</span>";
                    //return r ? Ext.Date.format(new Date(r), "Y-m-d H:i:s") : "<span class='na-text'>Not specified</span>";

                },
                getTotal: function (r) {
                    return r ? r.format() : 0;
                },
                getDocNumber: function (r) {
                    return r ? r.toFixed(2) : 0.00;
                }
            }
        );

        this.modelHistoryView = Ext.create('Ext.view.View', {
            hidden: true,
            store: this.modelHistoryStore,
            tpl: this.modelHistoryTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.modelHistoryPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'modelHistoryStore',
            displayInfo:true,
            hidden: true,
            displayMsg:'Classifier history records {0} - {1} of {2}',
            emptyMsg:'No classifier history records to display'
        });

        this.attributeDetails = Ext.create('TAGGUI.attribute-details.view.AttributeDetailsMain',{
            showDeleteButton: false
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
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '15 0 0 0',
                            items: [
                                this.modelDetails
                            ]
                        },
                        {
                            xtype: 'container',
                            width: '100%',
                            html: '<div class="horizontalLine"></div>'
                        },
                        this.modelLabelsView,
                        {
                            xtype: 'container',
                            layout:  'hbox',
                            margin: '25 0 0 0',
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
                    title: 'History',
                    padding: '10 0 0 0',
                    defaults: {
                        anchor: '100%'
                    },
                    items: [
                        this.modelHistoryTitle,
                        this.modelHistoryView,
                        this.modelHistoryPaging
                    ]
                },
                {
                    title: 'Edit/Remove',
                    padding: '10 0 0 0',
                    items: [
                        this.attributeDetails
                    ]
                }
            ]
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 0 0',
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